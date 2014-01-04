package data;

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
	
	public void nextTurn()
	{
		this.considerEvent(new TurnEvent(this, NEXT_TURN, this.isPlayer1Turn));
		this.movePlayerUnits();
		this.isPlayer1Turn = !this.isPlayer1Turn;
		newTurn();
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
		int start;
		int end;
		int incrementor;
		
		if (player1)
		{
			start = 0;
			end = Main.GRIDHEIGHT - 1;
			incrementor = 1;
		}
		else
		{
			start = Main.GRIDHEIGHT - 1;
			end = 0;
			incrementor = -1;
		}
		
		for (int i = 0 ; i < this.gridContents.length; i++)
		{
			Unit[] column = this.gridContents[i];
			for(int j = start ; (end - j) * incrementor >= 0; j += incrementor)
			{
				Unit unit1 = column[j];
				if (unit1 == null || (unit1.isOwnedByPlayer1() != player1))
					continue;
				tryMovingPlayerUnit(unit1, i, j, start, end, incrementor);
			}
		}
	}
	
	private void tryMovingPlayerUnit(Unit unit1, int xPos, int yPos, 
			int start, int end, int incrementor)
	{
		Unit[] column = this.gridContents[xPos];
		int speed = unit1.getUnitType().getSpeed();
		int potentialEndPos = (yPos + (speed * incrementor * -1));
		for (int k = yPos - incrementor; 
				(start - k) * incrementor * -1 >= 0 && 
				(potentialEndPos - k) * incrementor * -1 >= 0; 
				k -= incrementor)
		{
			Unit unit2 = column[k];
			if (unit2 != null)
			{
				boolean combatResult = this.unitCombat(unit1, unit2, xPos, yPos, 
						xPos, k);
				if (!combatResult)
				{
					return;
				}
			}
		}
		if (((start - potentialEndPos) * incrementor) > 0)
		{
			unitBaseAttack(unit1);
			this.gridContents[xPos][yPos] = null;
			considerEvent(new OneUnitEvent(this, UNITBASEATTACK, xPos, 
					yPos, unit1));
		}
		else
		{
			this.moveUnit(xPos, yPos, xPos, potentialEndPos);
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
	}
		
	private void player1Loss()
	{
		Main.gameStops(false);
	}
	
	private void player2Loss()
	{
		Main.gameStops(true);
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
	
	private void newTurn()
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
			this.nextTurn();
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
			this.nextTurn();
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
			this.gridContents[xPos1][yPos1] = null;
			this.gridContents[xPos2][yPos2] = unit1;	
		}
		else if (combatResult == CombatResult.UNIT1DESTROYED)
		{
			this.gridContents[xPos1][yPos1] = null;
			this.gridContents[xPos2][yPos2] = unit2;
		}
		this.considerEvent(new CombatEvent(this, COMBAT, xPos1, yPos1, unit1, 
				unit2, xPos2, yPos2, BASIC, combatResult));
		
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
			nextTurn();
		}
	}
	
	public void startGame(GameScreen gameScreen)
	{
		gameScreen.setVisible(true);		
	}
	
	public void startGame()
	{
		this.newTurn();
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
}
