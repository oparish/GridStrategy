package data;

import static events.CombatResult.DRAW;
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

import events.CombatEvent;
import events.CombatResult;
import events.CombatType;
import events.EventType;
import events.MyEvent;
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
	private boolean randomPlayer = false;

	static 
	{
		GameGrid.random = new Random();
	}
	
	public boolean isRandomPlayer() {
		return randomPlayer;
	}

	public void setRandomPlayer(boolean randomPlayer) {
		this.randomPlayer = randomPlayer;
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
	
	public GameGrid(MyEventSpeaker speaker)
	{
		this.cplayer2 = Main.getTestPlayer();
		this.speaker = speaker;
		if (speaker != null)
		{
			this.eventRunnable = new EventRunnable();
			this.thread = new Thread(eventRunnable);
			this.thread.start();
		}
		this.player1HP = Main.PLAYER1_MAXHP;
		this.player2HP = Main.PLAYER2_MAXHP;
		this.isPlayer1Turn = Main.PLAYER1STARTS;
		this.gridContents = new Unit[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		this.setupDeploymentPoints();
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
			if (this.player2HP >= 0)
				this.player2Loss();
		}
		else
		{
			this.player1HP -= damage;
			if (this.player1HP >= 0)
				this.player1Loss();
		}
	}
		
	private void player1Loss()
	{
		
	}
	
	private void player2Loss()
	{
		
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

		if (!this.isPlayer1Turn)
		{
			if (this.randomPlayer)
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
		int startPos;
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
		
		considerEvent(new OneUnitEvent(this, DEPLOYING_UNIT, columnPos, 
				deployPoint, unit));
		this.gridContents[columnPos][deployPoint] = unit;
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
	
	public boolean unitCombat(Unit unit1, Unit unit2, int xPos1, int yPos1, 
			int xPos2, int yPos2)
	{
		this.gridContents[xPos1][yPos1] = null;
		this.gridContents[xPos2][yPos2] = null;
		this.considerEvent(new CombatEvent(this, COMBAT, xPos1, yPos1, unit1, 
				unit2, xPos2, yPos2, BASIC, DRAW));
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
