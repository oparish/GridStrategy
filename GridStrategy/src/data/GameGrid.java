package data;

import static data.GameResult.PLAYER1_WINS;
import static data.GameResult.PLAYER2_WINS;
import static data.GameResult.TIMED_OUT;
import static data.TerrainCategory.BARRIER;
import static data.UnitCategory.FLYING;
import static data.UnitCategory.FRONTLINE;
import static data.UnitCategory.LOW;
import static data.UnitCategory.SHIELD;
import static data.UnitType.BUNKER;
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
import events.EventBase;
import events.EventCell;
import events.EventType;
import events.FinishActionEvent;
import events.MyEvent;
import events.MyEventListener;
import events.MyEventSpeaker;
import events.OneUnitEvent;
import events.TurnEvent;
import events.TwoPositionEvent;
import events.TwoUnitEvent;
import ai.Action;
import ai.ActivateAction;
import ai.CPlayer;
import ai.ColumnSearchCondition;
import ai.DeployAction;
import ai.ObservationBatch;
import ai.Spawner;
import main.Main;

public class GameGrid
{
	private static Random random;
	
	private final MyEventSpeaker speaker;
	private Map map;
	public Map getMap() {
		return map;
	}

	private int player1HP;
	private int player2HP;
	private int player1Credits;
	private int player2Credits;
	private Integer[] player1DeploymentPoints;
	private Integer[] player2DeploymentPoints;
	private Unit[][] gridUnits;

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
	
	public int getPlayer1Credits() {
		return player1Credits;
	}
	
	public int getPlayer2Credits() {
		return player2Credits;
	}

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
	
	public Integer[] getPlayerDeploymentPoints(boolean isPlayer1)
	{
		if (isPlayer1)
			return this.getPlayer1DeploymentPoints();
		else
			return this.getPlayer2DeploymentPoints();
	}
	
	public GameGrid(CPlayer cPlayer1, CPlayer cPlayer2, ArrayList<Integer> mapData)
	{
		this.cplayer1 = cPlayer1;
		this.cplayer2 = cPlayer2;
		this.speaker = new MyEventSpeaker();;
		this.eventRunnable = new EventRunnable();
		this.thread = new Thread(eventRunnable);
		this.thread.start();
		this.isPlayer1Turn = Main.PLAYER1STARTS;
		this.gridUnits = new Unit[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		if (mapData != null)
			this.map = new DataMap(mapData);
		else
			this.map = new DefaultMap();
		this.loadMap();
		this.setupDeploymentPoints();
	}
	
	private void loadMap()
	{
		this.player1HP = this.map.getPlayer1InitialLife();
		this.player2HP = this.map.getPlayer2InitialLife();
		this.player1Credits = this.map.getPlayer1Credits();
		this.player2Credits = this.map.getPlayer2Credits();
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
	
	public Unit getUnitAt(int x, int y)
	{
		return this.gridUnits[x][y];
	}
	
	public void activateAbility(int x, int y, AbilityType abilityType, Unit unit)
	{
		switch(abilityType)
		{
		case DEPLOYPOINT:
			this.newDeployPoint(x, y);
			break;
		case ARTILLERY:
			this.fireArtillery(x, y, unit);
			break;
		default:
		}
		this.noteMove();
	}
	
	private void fireArtillery(int x, int y, Unit unit)
	{
		Sound.ARTILLERY_FIRE.play();
		int direction;
		int endPoint;
		
		if (unit.isOwnedByPlayer1())
		{
			direction = -1;
			endPoint = 0;
		}
		else
		{
			direction = 1;
			endPoint = Main.GRIDHEIGHT - 1;
		}
		
		boolean shotStopped = false;
		
		for (int i = y + direction; (endPoint - i) * direction >= 0; i+=direction)
		{
			Unit unit2 = this.getUnitAt(x, i);
			if (unit2 != null && unit2.getUnitType().hasCategory(SHIELD))
			{
				this.considerEvent(new TwoUnitEvent(this, EventType.ARTILLERY_BLOCKED, x, y, unit, x, i, unit2));
				shotStopped = true;
				break;
			}
			else if (unit2 != null && !unit2.getUnitType().hasCategory(LOW))
			{
				this.artilleryHit(x, y, x, i, unit, unit2);
				shotStopped = true;
				break;
			}
			else if (this.map.getGridTerrain()[x][i].hasCategory(BARRIER))
			{
				this.considerEvent(new TwoPositionEvent(this, EventType.ARTILLERY_BLOCKED, x, y, unit, x, i));
				shotStopped = true;
				break;
			}
		}
		
		if (!shotStopped)
			this.considerEvent(new OneUnitEvent(this, EventType.ARTILLERY_WITHOUT_HIT, x, y, unit));
	}
	
	private void artilleryHit(int sourceX, int sourceY, int targetX, int targetY, Unit unit1, Unit unit2)
	{
		this.destroyUnitAt(targetX, targetY);
		this.considerEvent(new TwoUnitEvent(this, EventType.ARTILLERY_HIT, sourceX, sourceY, unit1, targetX, targetY, unit2));
	}
	
	private void newDeployPoint(int x, int y)
	{
		boolean ownedByPlayer1 = this.gridUnits[x][y].isOwnedByPlayer1();
		Unit unit = new Unit(ownedByPlayer1, BUNKER);
		this.gridUnits[x][y] = unit;
		this.updateDeployPoints(ownedByPlayer1, x);
		considerEvent(new OneUnitEvent(this, EventType.DEPLOYING_UNIT, x, y, unit));
	}
	
	private void updateDeployPoints(boolean ownedByPlayer1, int x)
	{
		int searchStart;
		int searchEnd;
		int direction;
		if (ownedByPlayer1)
		{
			searchStart = 0;
			searchEnd = Main.GRIDHEIGHT - 1;
			direction = 1;
		}
		else
		{
			searchStart = Main.GRIDHEIGHT - 1;
			searchEnd = 0;
			direction = -1;
		}
		for (int i = searchStart; ((searchEnd - i) * direction) >= 0; i += direction)
		{
			Unit unit = this.gridUnits[x][i];
			if (unit != null && unit.getUnitType().hasCategory(FRONTLINE))
			{
				if (ownedByPlayer1)
				{
					this.player1DeploymentPoints[x] = i - 1;
					return;
				}
				else
				{
					this.player2DeploymentPoints[x] = i + 1;
					return;
				}
			}
		}
		if (ownedByPlayer1)
		{
			this.player1DeploymentPoints[x] = searchEnd;
		}
		else
		{
			this.player2DeploymentPoints[x] = searchEnd;
		}
	}
	
	public void endOfTurn()
	{
		Main.debugOut("End of Turn");
		this.considerEvent(new TurnEvent(this, NEXT_TURN, this.isPlayer1Turn));
		this.movePlayerUnits();
		this.checkForStalemate();
		this.increaseCredits(this.isPlayer1Turn, Main.CREDITSPERTURN);
		this.isPlayer1Turn = !this.isPlayer1Turn;
		Main.debugOut("End of End of Turn");
	}
	
	private void increaseCredits(boolean isPlayer1, int amount)
	{
		if (isPlayer1)
			this.player1Credits += amount;
		else
			this.player2Credits += amount;
		this.considerEvent(new TurnEvent(this, EventType.CREDITS_CHANGE, isPlayer1));
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
		System.out.println("Stalemate");
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
		
		for (int i = 0 ; i < this.gridUnits.length; i++)
		{
			Unit[] column = this.gridUnits[i];
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
	
	private int deduceEndPos(int startPos, int column, int directionToWalk, UnitType unitType)
	{
		int distance = unitType.getSpeed();
		int endPos = startPos;
		while (distance > 0)
		{
			endPos += directionToWalk;
			distance -= this.map.getGridTerrain()[column][endPos].hasCategory(TerrainCategory.ROUGH) && 
					!unitType.hasCategory(FLYING) ? 2 : 1;
			if (this.map.getGridTerrain()[column][endPos].hasCategory(TerrainCategory.OBSTACLE) 
					&& !unitType.hasCategory(UnitCategory.FLYING))
			{
				endPos -= directionToWalk;
				break;
			}
		}
		return endPos;
	}
	
	private void tryMovingPlayerUnit(MoveAttempt moveAttempt, 
			int end, int start, int directionToWalk)
	{
		if (moveAttempt.unit.isJustDeployed())
		{
			moveAttempt.unit.setJustDeployed(false);
			return;
		}
		
		Unit[] column = this.gridUnits[moveAttempt.column];
		int speed = moveAttempt.unit.getUnitType().getSpeed();
		if (speed == 0)
			return;
		moveAttempt.potentialEndPos = this.deduceEndPos(moveAttempt.startPos, moveAttempt.column, directionToWalk, 
				moveAttempt.unit.getUnitType());
		
		if (moveAttempt.startPos == moveAttempt.potentialEndPos)
			return;
		
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
			if (currentScreenPos != end)
				this.moveUnit(moveAttempt.column, currentScreenPos, moveAttempt.column, end);
			unitBaseAttack(moveAttempt.unit);
			this.destroyUnitAt(moveAttempt.column, end);
			EventBase eventBase = new EventBase(moveAttempt.column, !moveAttempt.unit.isOwnedByPlayer1());
			EventCell eventCell = new EventCell(moveAttempt.column, end);
			considerEvent(new TwoPositionEvent(this, MOVING_UNIT, eventCell, moveAttempt.unit, eventBase));
			considerEvent(new OneUnitEvent(this, UNITBASEATTACK, eventBase, moveAttempt.unit));
		}
		else
		{
			this.moveUnit(moveAttempt.column, currentScreenPos, moveAttempt.column, moveAttempt.potentialEndPos);
		}
	}
	
	private void unitBaseAttack(Unit unit)
	{
		int damage = unit.getUnitType().getBaseDamage();
		if (damage == 0)
			return;
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
		System.out.println("Player 2 wins");
		this.result = PLAYER2_WINS;
		this.endGame();
	}
	
	private void player2Loss()
	{
		System.out.println("Player 1 wins");
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
		Main.debugOut("cplayerturn: " + isPlayer1);
		CPlayer currentCPlayer;
		int credits;
		if (isPlayer1)
		{
			currentCPlayer = this.cplayer1;
			credits = this.player1Credits;
		}
		else
		{
			currentCPlayer = this.cplayer2;
			credits = this.player2Credits;
		}
		ObservationBatch observationBatch = new ObservationBatch(isPlayer1, 
				this.gridUnits, credits, this.getPlayer1DeploymentPoints(), this.getPlayer2DeploymentPoints());
		
		boolean result = currentCPlayer.makeMove(observationBatch, this);
		if (!result)
		{
			this.considerEvent(new TurnEvent(this, EventType.SKIP_TURN, isPlayer1));
			this.noteMove();
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
			if (this.gridUnits[i][deploymentPoints[i]] == null)
				possiblePositions.add(i);
		}
		int actionNumber = GameGrid.random.nextInt(possiblePositions.size() + 1);
		ArrayList<UnitType> unitTypes = UnitType.getDeployableUnitTypes();
		int unitNumber = GameGrid.random.nextInt(unitTypes.size());
		if (actionNumber == Main.GRIDWIDTH)
		{
			this.endOfTurn();
		}
		else
		{
			DeployAction action = new DeployAction(actionNumber, unitTypes.get(unitNumber));
			boolean result = action.attemptAction(this, this.isPlayer1Turn, Main.GENERIC_CHECK_SUCCESS);
			if (!result)
				this.noteMove();
		}
	}
	
	public ArrayList<UnitType> getAvailableUnitTypes(boolean isPlayer1)
	{
		if (isPlayer1)
			return this.map.getPlayer1Types();
		else
			return this.map.getPlayer2Types();
	}
	
	public boolean activateCplayerUnit(boolean isPlayer1, UnitType unitType, int x, ColumnSearchCondition searchCondition)
	{
		int start;
		int end;
		int direction;	
		
		if ((searchCondition == ColumnSearchCondition.NEAREST_TO_START && isPlayer1) || 
				(searchCondition == ColumnSearchCondition.FURTHEST_FROM_START && !isPlayer1))
		{
			start = Main.GRIDHEIGHT - 1;
			end = 0;
			direction = -1;
		}
		else
		{
			start = 0;
			end = Main.GRIDHEIGHT - 1;
			direction = 1;
		}
		
		for (int i = start ; (end - i) * direction >= 0 ; i += direction)
		{
			Unit unit = this.gridUnits[x][i];
			if (unit != null && unit.isOwnedByPlayer1() == isPlayer1 && unit.getUnitType() == unitType)
			{
				this.activateAbility(x, i, unitType.getAbilityType(), unit);
				return true;
			}
		}
		
		return false;
	}
	
	private boolean attemptUnitPurchase(boolean isPlayer1, int cost)
	{
		if (isPlayer1)
		{
			if (cost > this.player1Credits)
				return false;
			else
			{
				this.player1Credits -= cost;
				return true;
			}
		}
		else
			if (cost > this.player2Credits)
				return false;
			else
			{
				this.player2Credits -= cost;
				return true;
			}
	}
	
	public boolean deployUnit(Unit unit, int columnPos)
	{
		int deployPoint;
		int end;
		boolean isPlayer1 = unit.isOwnedByPlayer1();
		if (isPlayer1)
		{
			deployPoint = this.player1DeploymentPoints[columnPos];
			end = -1;
		}
		else
		{
			deployPoint = this.player2DeploymentPoints[columnPos];
			end = Main.GRIDHEIGHT;
		}
			
		Main.debugOut("Trying to place: " + unit);
		
		if (deployPoint == end)
		{
			boolean purchaseResult = this.attemptUnitPurchase(isPlayer1, unit.getUnitType().getCost());	
			if (!purchaseResult)
				return false;
			this.unitBaseAttack(unit);
			EventBase eventBase = new EventBase(columnPos, !isPlayer1);
			considerEvent(new OneUnitEvent(this, DEPLOYING_UNIT, eventBase, unit));
			considerEvent(new OneUnitEvent(this, UNITBASEATTACK, eventBase, unit));
			this.noteMove();
			return true;
		}
		else if (this.gridUnits[columnPos][deployPoint] == null)
		{
			boolean purchaseResult = this.attemptUnitPurchase(isPlayer1, unit.getUnitType().getCost());	
			if (!purchaseResult)
				return false;
			considerEvent(new OneUnitEvent(this, DEPLOYING_UNIT, columnPos, 
					deployPoint, unit));
			this.gridUnits[columnPos][deployPoint] = unit;
			this.noteMove();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void moveUnit(int xPos1, int yPos1, int xPos2, int yPos2)
	{
		Unit unit = this.gridUnits[xPos1][yPos1];
		if (unit == null)
			System.out.println("Trying to move a null unit");
		this.considerEvent(new TwoPositionEvent(this, MOVING_UNIT, xPos1, 
				yPos1, unit, xPos2, yPos2));
		this.gridUnits[xPos2][yPos2] = unit;
		this.gridUnits[xPos1][yPos1] = null;
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
			this.destroyUnitAt(xPos1, yPos1);
			this.destroyUnitAt(xPos2, yPos2);
		}
		else if (combatResult == CombatResult.UNIT2DESTROYED)
		{
			this.destroyUnitAt(xPos2, yPos2);
		}
		else if (combatResult == CombatResult.UNIT1DESTROYED)
		{
			this.destroyUnitAt(xPos1, yPos1);
		}
		this.considerEvent(new CombatEvent(this, COMBAT, xPos1, yPos1, unit1, 
				unit2, xPos2, yPos2, BASIC, combatResult));
		
		if (combatResult == CombatResult.UNIT2DESTROYED)
			return true;
		else
			return false;
	}
	
	private void destroyUnitAt(int x, int y)
	{
		Unit unit = this.gridUnits[x][y];
		if (unit == null)
			System.out.println("Trying to destroy a null unit");
		if (unit.getUnitType().hasCategory(FRONTLINE))
		{
			this.gridUnits[x][y] = null;
			this.updateDeployPoints(unit.isOwnedByPlayer1(), x);
		}
		else
		{
			this.gridUnits[x][y] = null;
		}	
	}
	
	private void noteMove()
	{
		this.turnMoves++;
		this.checkTurnMoves();
	}
	
	private void checkTurnMoves()
	{
		Main.debugOut(this.isPlayer1Turn);
		Main.debugOut(this.turnMoves);
		if (this.turnMoves == Main.MOVESPERTURN)
		{
			if (!this.automatedMode && this.isPlayer1Turn)
				this.setNextTurn(true);
			else
				this.endOfTurn();
		}
		else
		{
			this.considerEvent(new FinishActionEvent(this));
		}
	}
	
	public GameResult startGame(GameScreen gameScreen, boolean automated)
	{
		this.automatedMode = automated;
		gameScreen.setVisible(true);
		this.gameRunning = true;
		while (this.gameRunning)
		{
			this.startOfTurn();
			Main.debugOut("Player Turn: " + this.isPlayer1Turn);
		}
		return this.result;
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
