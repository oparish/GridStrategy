package data;

import static data.GameResult.PLAYER1_WINS;
import static data.GameResult.PLAYER2_WINS;
import static data.GameResult.TIMED_OUT;
import static events.CombatType.BASIC;
import static events.EventType.COMBAT;
import static events.EventType.DEPLOYING_UNIT;
import static events.EventType.MOVING_UNIT;
import static events.EventType.NEW_TURN;
import static events.EventType.NEXT_TURN;
import static events.EventType.UNITBASEATTACK;
import static screens.GameScreenState.STANDARD;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Random;

import screens.GameScreen;

import events.CombatEvent;
import events.CombatResult;
import events.CombatType;
import events.EventType;
import events.MyEvent;
import events.MyEventListener;
import events.MyEventSpeaker;
import events.OneUnitEvent;
import events.TurnEvent;
import events.TwoPositionEvent;
import ai.Action;
import ai.CPlayer;
import ai.ObservationBatch;
import ai.Spawner;
import buttons.ColumnButton;
import main.Main;

public class GameGrid
{
	private static Random random;
	
	private final MyEventSpeaker speaker;
	private int player1HP;
	private int player2HP;
	private Integer[] player1DeploymentPoints;
	private Integer[] player2DeploymentPoints;
	private Unit[][] gridContents;
	private int turnMoves;
	private boolean isPlayer1Turn;
	private EventRunnable eventRunnable;
	private Thread thread;
	private CPlayer cplayer1;
	private CPlayer cplayer2;
	private boolean gameRunning = false;
	private boolean automatedMode;
	private boolean nextTurn;
	private GameResult result;
	private int stalemateCounter = 0;
	private boolean firstBaseAttackMade = false;

	public synchronized boolean isNextTurn() {
		return nextTurn;
	}

	public synchronized void setNextTurn(boolean nextTurn) {
		this.nextTurn = nextTurn;
	}

	static 
	{
		GameGrid.random = new Random();
	}
	
	public int getPlayer2HP() {
		return player2HP;
	}
	
	public int getPlayer1HP() {
		return player1HP;
	}
	
	public boolean isPlayer1Turn() {
		return isPlayer1Turn;
	}
	
	public Integer[] getPlayer1DeploymentPoints() {
		return player1DeploymentPoints;
	}
	
	public Integer[] getPlayer2DeploymentPoints() {
		return player2DeploymentPoints;
	}
	
	public GameGrid(CPlayer cPlayer1, CPlayer cPlayer2)
	{
		this.cplayer1 = cPlayer1;
		this.cplayer2 = cPlayer2;
		this.speaker = new MyEventSpeaker();;
		this.eventRunnable = new EventRunnable();
		this.thread = new Thread(eventRunnable);
		this.thread.start();
		this.player1HP = Main.PLAYER1_MAXHP;
		this.player2HP = Main.PLAYER2_MAXHP;
		this.isPlayer1Turn = Main.PLAYER1STARTS;
		this.gridContents = new Unit[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		this.setupDeploymentPoints();
	}
	
	public void addEventListener(MyEventListener listener)
	{
		this.speaker.addEventListener(listener);
	}
	
	private void setupDeploymentPoints()
	{
		int gridWidth = Main.GRIDWIDTH;
		this.player1DeploymentPoints = new Integer[gridWidth];
		this.player2DeploymentPoints = new Integer[gridWidth];
		
		for (int i = 0; i < gridWidth; i++)
		{
			this.player1DeploymentPoints[i] = Main.GRIDHEIGHT - 1;
			this.player2DeploymentPoints[i] = 0;
		}
	}
	
	public void endOfTurn()
	{
		this.considerEvent(new TurnEvent(this, NEXT_TURN, this.isPlayer1Turn));
		this.movePlayerUnits();
		this.checkForStalemate();
		this.isPlayer1Turn = !this.isPlayer1Turn;
	}
	
	private void checkForStalemate()
	{
		this.stalemateCounter++;
		if (this.firstBaseAttackMade && 
				this.stalemateCounter > Main.SUBATTACKSTALEMATE)
			this.declareStalemate();
		else if (!this.firstBaseAttackMade && 
				this.stalemateCounter > Main.FIRSTATTACKSTALEMATE)
			this.declareStalemate();
	}
	
	private void declareStalemate()
	{
		this.result = TIMED_OUT;
		this.endGame();
	}
	
	private void movePlayerUnits()
	{
		if (this.isPlayer1Turn)
			this.movePlayerUnits(true);
		else
			this.movePlayerUnits(false);
	}
	
	private void movePlayerUnits(boolean player1)
	{
		int end;
		int start;
		int directionToWalk;
		
		if (player1)
		{
			end = 0;
			start = Main.GRIDHEIGHT - 1;
			directionToWalk = -1;
		}
		else
		{
			end = Main.GRIDHEIGHT - 1;
			start = 0;
			directionToWalk = 1;
		}
		
		ArrayList<MoveAttempt> moveList = this.getMoveList(player1, end, start, directionToWalk);
		
		for (MoveAttempt moveAttempt : moveList)
		{
			tryMovingPlayerUnit(moveAttempt, end, start, directionToWalk);
		}
		
	}
	
	private ArrayList<MoveAttempt> getMoveList(boolean player1, int end, int start, int directionToWalk)
	{
		ArrayList<MoveAttempt> moveList = new ArrayList<MoveAttempt>();
		
		for (int i = 0 ; i < this.gridContents.length; i++)
		{
			Unit[] column = this.gridContents[i];
			for(int j = end ; (start - j) * -directionToWalk >= 0; j -= directionToWalk)
			{
				Unit unit = column[j];
				if (unit == null || (unit.isOwnedByPlayer1() != player1))
					continue;
				moveList.add(new MoveAttempt(i, j, unit));
			}
		}
		return moveList;
	}
	
	private void tryMovingPlayerUnit(MoveAttempt moveAttempt, 
			int end, int start, int directionToWalk)
	{
		Unit[] column = this.gridContents[moveAttempt.column];
		int speed = moveAttempt.unit.getUnitType().getSpeed();
		moveAttempt.potentialEndPos = (moveAttempt.startPos + (speed * directionToWalk));
		
		int currentScreenPos = moveAttempt.startPos;
		
		for (int k = moveAttempt.startPos + directionToWalk; 
				(end - k) * directionToWalk >= 0 && 
				(moveAttempt.potentialEndPos - k) * directionToWalk >= 0; 
				k += directionToWalk)
		{
			Unit unit2 = column[k];
			if (unit2 != null)
			{
				int newPos = k - directionToWalk;
				if (newPos != currentScreenPos)
					this.moveUnit(moveAttempt.column, currentScreenPos, moveAttempt.column, newPos);
				if (moveAttempt.unit.isOwnedByPlayer1() != unit2.isOwnedByPlayer1())
				{
					boolean combatResult = this.unitCombat(moveAttempt.unit, unit2, moveAttempt.column, newPos, 
							moveAttempt.column, k);
					if (!combatResult)
					{
						return;
					}
					currentScreenPos = k - directionToWalk;
				}
				else
				{
					return;
				}			
			}
		}
		
		if (((end - moveAttempt.potentialEndPos) * -directionToWalk) > 0)
		{
			this.moveUnit(moveAttempt.column, currentScreenPos, moveAttempt.column, end);
			unitBaseAttack(moveAttempt.unit);
			this.gridContents[moveAttempt.column][moveAttempt.startPos] = null;
			considerEvent(new OneUnitEvent(this, UNITBASEATTACK, moveAttempt.column, 
					end, moveAttempt.unit));
		}
		else
		{
			this.moveUnit(moveAttempt.column, currentScreenPos, moveAttempt.column, moveAttempt.potentialEndPos);
		}
	}
	
	private void unitBaseAttack(Unit unit)
	{
		int damage = unit.getUnitType().getBaseDamage();
		if (unit.isOwnedByPlayer1())
		{
			this.player2HP -= damage;
			if (this.player2HP <= 0)
				this.player2Loss();
		}
		else
		{
			this.player1HP -= damage;
			if (this.player1HP <= 0)
				this.player1Loss();
		}
		this.firstBaseAttackMade = true;
		this.stalemateCounter = 0;
	}
		
	private void player1Loss()
	{
		System.out.println("Player 1 loses");
		this.result = PLAYER2_WINS;
		this.endGame();
	}
	
	private void player2Loss()
	{
		System.out.println("Player 2 loses");
		this.result = PLAYER1_WINS;
		this.endGame();
	}
	
	private void endGame()
	{
		if (this.automatedMode == false)
			System.exit(0);
		else
			this.gameRunning = false;
	}
	
	private void considerEvent(MyEvent event)
	{
		if (this.speaker != null && this.isPlayer1Turn)
		{
			this.speaker.fireEvent(event);
		}
		else if (this.speaker != null)
		{
			this.eventRunnable.addEvent(event);
		}
	}
	
	private void completeEvents()
	{
		while (!this.eventRunnable.checkEventsEmpty())
		{
			
		}
	}
	
	private void waitForPlayer()
	{
		this.setNextTurn(false);
		while (!this.isNextTurn())
		{
			
		}
		this.endOfTurn();
	}
	
	private void startOfTurn()
	{
		this.turnMoves = 0;
		this.completeEvents();
		considerEvent(new TurnEvent(this, NEW_TURN, this.isPlayer1Turn));
		if (this.isPlayer1Turn && this.cplayer1 != null)
		{
			this.cPlayerTurn(true);
		}
		else if (!this.isPlayer1Turn)
		{
			if (this.cplayer2 == null)
				this.randomPlayerTurn();
			else
				this.cPlayerTurn(false);
		}
		else
		{
			this.waitForPlayer();
		}
	}
	
	private void cPlayerTurn(boolean isPlayer1)
	{
		CPlayer currentCPlayer;
		if (isPlayer1)
			currentCPlayer = this.cplayer1;
		else
			currentCPlayer = this.cplayer2;
		ObservationBatch observationBatch = new ObservationBatch(isPlayer1, 
				this.gridContents);
		Action action = currentCPlayer.getAction(observationBatch);
		if (action != null)
		{
			this.takeAction(action, isPlayer1);
		}
		else
		{
			this.endOfTurn();
		}
	}
	
	private void randomPlayerTurn()
	{
		Integer[] deploymentPoints;
		
		if (this.isPlayer1Turn)
		{
			deploymentPoints = this.player1DeploymentPoints;
		}
		else
		{
			deploymentPoints = this.player2DeploymentPoints;
		}
		ArrayList<Integer> possiblePositions = new ArrayList<Integer>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			if (this.gridContents[i][deploymentPoints[i]] == null)
				possiblePositions.add(i);
		}
		int actionNumber = GameGrid.random.nextInt(possiblePositions.size() + 1);
		UnitType[] unitTypes = UnitType.values();
		int unitNumber = GameGrid.random.nextInt(unitTypes.length);
		if (actionNumber == Main.GRIDWIDTH)
		{
			this.endOfTurn();
		}
		else
		{
			Action action = new Action(actionNumber, unitTypes[unitNumber]);
			this.takeAction(action, this.isPlayer1Turn);
		}
	}
	
	private void takeAction(Action action, boolean isPlayer1)
	{
		Unit unit = new Unit(isPlayer1, action.getUnitType());
		this.deployUnit(unit, action.getColumnPos());
	}
	
	public void deployUnit(Unit unit, int columnPos)
	{
		int deployPoint;
		if (unit.isOwnedByPlayer1())
			deployPoint = this.player1DeploymentPoints[columnPos];
		else
			deployPoint = this.player2DeploymentPoints[columnPos];
			
		if (this.gridContents[columnPos][deployPoint] == null)
		{
			considerEvent(new OneUnitEvent(this, DEPLOYING_UNIT, columnPos, 
					deployPoint, unit));
			this.gridContents[columnPos][deployPoint] = unit;
		}	
		this.noteMove();
	}
	
	public void moveUnit(int xPos1, int yPos1, int xPos2, int yPos2)
	{
		Unit unit = this.gridContents[xPos1][yPos1];
		this.considerEvent(new TwoPositionEvent(this, MOVING_UNIT, xPos1, 
				yPos1, unit, xPos2, yPos2));
		this.gridContents[xPos2][yPos2] = unit;
		this.gridContents[xPos1][yPos1] = null;
	}
	
	private CombatResult checkCategoryCombat(UnitCategory unitCategory1, 
			UnitType unitType, boolean unit1HigherPriorityType)
	{
		switch(unitCategory1)
		{
		case INTERCEPTOR:
			if (!unitType.hasCategory(UnitCategory.INTERCEPTOR))
				if (unit1HigherPriorityType)
					return CombatResult.UNIT2DESTROYED;
				else
					return CombatResult.UNIT1DESTROYED;
			else
				return CombatResult.NORESULTYET;
		default:
			return CombatResult.NORESULTYET;
		}
	}
	
	public boolean unitCombat(Unit unit1, Unit unit2, int xPos1, int yPos1, 
			int xPos2, int yPos2)
	{	
		UnitType unit1Type = unit1.getUnitType();
		UnitType unit2Type = unit2.getUnitType();
		
		CombatResult combatResult = CombatResult.NORESULTYET;

		for (UnitCategory unitCategory : UnitCategory.values())
		{
			if (unit1Type.hasCategory(unitCategory))
				combatResult = this.checkCategoryCombat(unitCategory, unit2Type, 
						true);
			else if (unit2Type.hasCategory(unitCategory))
				combatResult = this.checkCategoryCombat(unitCategory, unit1Type, 
						false);
			if (combatResult != CombatResult.NORESULTYET)
				break;
		}
		if (combatResult == CombatResult.NORESULTYET || 
				combatResult == CombatResult.BOTHDESTROYED)
		{
			this.gridContents[xPos1][yPos1] = null;
			this.gridContents[xPos2][yPos2] = null;	
		}
		else if (combatResult == CombatResult.UNIT2DESTROYED)
		{
			this.gridContents[xPos1][yPos1] = unit1;
			this.gridContents[xPos2][yPos2] = null;	
		}
		else if (combatResult == CombatResult.UNIT1DESTROYED)
		{
			this.gridContents[xPos1][yPos1] = null;
			this.gridContents[xPos2][yPos2] = unit2;
		}
		this.considerEvent(new CombatEvent(this, COMBAT, xPos1, yPos1, unit1, 
				unit2, xPos2, yPos2, BASIC, combatResult));
		
		if (combatResult == CombatResult.UNIT2DESTROYED)
			return true;
		else
			return false;
	}
	
	private void noteMove()
	{
		this.turnMoves++;
		this.checkTurnMoves();
	}
	
	private void checkTurnMoves()
	{
		if (this.turnMoves == Main.MOVESPERTURN)
		{
			if (!this.automatedMode && this.isPlayer1Turn)
				this.setNextTurn(true);
			else
				this.endOfTurn();
		}
	}
	
	public void startGame(GameScreen gameScreen)
	{
		this.automatedMode = false;
		gameScreen.setVisible(true);
		this.gameRunning = true;
		while (this.gameRunning)
		{
			this.startOfTurn();
		}
	}
	
	public GameResult startGameWithoutScreen()
	{
		this.automatedMode = true;
		this.gameRunning = true;
		while (this.gameRunning)
		{
			this.startOfTurn();
		}
		return this.result;
	}	
	
	private class EventRunnable implements Runnable
	{
		ArrayList<MyEvent> events;
		
		public EventRunnable()
		{
			this.events = new ArrayList<MyEvent>();
		}
		
		public synchronized void addEvent(MyEvent event)
		{
			this.events.add(event);
		}
		
		public synchronized boolean checkEventsEmpty()
		{
			if (events.size() > 0)
				return false;
			else
				return true;
		}
		
		private synchronized void processEvents()
		{
			if (events.size() > 0)
			{
				MyEvent currentEvent = this.events.get(0);
				if (currentEvent != null)
					GameGrid.this.speaker.fireEvent(currentEvent);
				this.events.remove(currentEvent);
			}
		}
		
		public void run()
		{
			while(true)
			{
				this.processEvents();
			}
		}
	}
	
	private class MoveAttempt
	{
		public int startPos;
		public int column;
		public int potentialEndPos;
		public Unit unit;
		
		MoveAttempt(int column, int startPos, Unit unit)
		{
			this.startPos = startPos;
			this.column = column;
			this.unit = unit;
		}
	}
}
