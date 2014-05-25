package screens;

import static screens.GameScreenState.ACTIVATING_ABILITY;
import static screens.GameScreenState.COMPUTER_PLAYING;
import static screens.GameScreenState.DEPLOYING_UNIT;
import static screens.GameScreenState.MOVING_UNITS;
import static screens.GameScreenState.STANDARD;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.AbilityType;
import data.GameGrid;
import data.Unit;
import data.UnitType;
import dialogs.UnitDialog;
import events.CombatEvent;
import events.CombatResult;
import events.CombatType;
import events.MyEvent;
import events.MyEventListener;
import events.MyEventSpeaker;
import events.OneUnitEvent;
import events.TurnEvent;
import events.TwoPositionEvent;
import events.TwoUnitEvent;
import animation.Animation;
import animation.AnimationSeries;
import animation.Animator;
import animation.VerticalAnimationSeries;
import buttons.ControlButton;
import main.Main;
import panes.Cell;
import panes.CellPanel;
import panes.ControlPane;
import panes.GridPane;
import panes.InfoPane;
import panes.MessagePane;

@SuppressWarnings("serial")
public class GameScreen extends JFrame implements ActionListener, MyEventListener, MouseListener
{
	private GridPane gridPane;
	private CellPanel cellPanel;
	private ControlPane controlPane;
	private MessagePane messagePane;
	private InfoPane infoPane;
	private GameScreenState screenState;
	private GameGrid gameGrid;

	private Unit unitToDeploy;

	public GameScreen(GameGrid gameGrid)
	{
		super();
		this.gameGrid = gameGrid;
		this.setLayout(new GridLayout(2,2));
		this.gridPane = new GridPane(this);
		this.cellPanel = this.gridPane.getCellPanel();
		Animator.setGridPane(this.gridPane);
		this.controlPane = new ControlPane(this);
		this.messagePane = new MessagePane();
		this.infoPane = new InfoPane(this.gameGrid);
		JScrollPane jScrollPane = new JScrollPane(this.messagePane);
		this.add(this.gridPane);
		this.add(this.controlPane);
		this.add(jScrollPane);
		this.add(this.infoPane);
		this.switchScreenState(STANDARD);
		this.gameGrid.addEventListener(this);
		this.gameGrid.addEventListener(this.messagePane);
		this.gameGrid.addEventListener(this.infoPane);
	}
	
	public GameGrid getGameGrid() {
		return gameGrid;
	}
	
	public Integer[] getPlayer1DeploymentPoints()
	{
		return this.gameGrid.getPlayer1DeploymentPoints();
	}
	
	private void nextTurn()
	{
		this.gameGrid.setNextTurn(true);
	}
	
	private void startHumanTurn()
	{
		switchScreenState(STANDARD);
	}
	
	private void startComputerTurn()
	{
		switchScreenState(COMPUTER_PLAYING);
	}
		
	public GameScreenState getScreenState() {
		return screenState;
	}
	
	public void switchScreenState(GameScreenState screenState)
	{
		this.screenState = screenState;
		switch(screenState)
		{
		case STANDARD:
			this.switchToStandardScreen();
			break;
		case DEPLOYING_UNIT:
			this.switchToDeployingScreen();
			break;
		case ACTIVATING_ABILITY:
			this.switchToActivatingAbilityScreen();
			break;
		case MOVING_UNITS:
			this.switchToComputerPlayingScreen();
			break;
		case COMPUTER_PLAYING:
			this.switchToComputerPlayingScreen();
			break;
		default:
		}
	}
	
	private void switchToActivatingAbilityScreen()
	{
		this.controlPane.runningPlayerOperation(true);
	}
	
	private void switchToComputerPlayingScreen()
	{
		this.controlPane.disableControls();
		this.cellPanel.clearDeployPoints(this.getPlayer1DeploymentPoints());
	}
	
	private void switchToStandardScreen()
	{
		this.controlPane.runningPlayerOperation(false);
		this.cellPanel.clearDeployPoints(this.getPlayer1DeploymentPoints());
	}
	
	private void switchToDeployingScreen()
	{
		this.cellPanel.showDeployPoints(this.getPlayer1DeploymentPoints());
		this.controlPane.runningPlayerOperation(true);
	}
	
	private void selectUnit()
	{
		new UnitDialog(this).setVisible(true);
	}
	
	public void readyToDeployUnit(UnitType unitType)
	{
		this.unitToDeploy = new Unit(true, unitType);
		this.switchScreenState(DEPLOYING_UNIT);
	}
	
	public GridPane getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}
	
	private void cancelOperation()
	{
		this.switchScreenState(STANDARD);
	}
	
	private void activateAbility()
	{
		this.switchScreenState(ACTIVATING_ABILITY);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{	
		Object button = ae.getSource();
		if (button instanceof ControlButton)
		{
			switch(((ControlButton) button).getButtonType())
			{
				case MAIN_MENU:
					Main.getMain().switchToMainMenu();
					break;
				case DEPLOY_UNIT:
					this.selectUnit();
					break;
				case NEXT_TURN:
					this.nextTurn();
					break;
				case ACTIVATE_ABILITY:
					this.activateAbility();
					break;
				case CANCEL:
					this.cancelOperation();
					break;
				case QUIT:
					System.exit(0);
				default:
			}
		}
	}

	@Override
	public void receiveEvent(MyEvent event)
	{
		Integer xPos = null;
		Integer yPos = null; 
		Unit unit1 = null;
		Integer xPos2 = null;
		Integer yPos2 = null;
		Unit unit2 = null;
		Boolean isPlayer1Turn = null;
		
		if (event instanceof TurnEvent)
			isPlayer1Turn = ((TurnEvent) event).isPlayer1();
		else if (event instanceof OneUnitEvent)
		{
			OneUnitEvent oneUnitEvent = (OneUnitEvent) event;
			unit1 = oneUnitEvent.getUnit();
			xPos = oneUnitEvent.getXpos1();
			yPos = oneUnitEvent.getYPos1();
			if (event instanceof TwoPositionEvent)
			{
				TwoPositionEvent twoPositionEvent = (TwoPositionEvent) event;
				xPos2 = twoPositionEvent.getXPos2();
				yPos2 = twoPositionEvent.getYPos2();
				if (event instanceof TwoUnitEvent)
					unit2 = ((TwoUnitEvent) event).getUnit2();
			}
		}
		
		switch(event.getType())
		{
			case NEXT_TURN:
				showNextTurn(isPlayer1Turn);
				break;
			case NEW_TURN:
				showNewTurn(isPlayer1Turn);
				break;
			case DEPLOYING_UNIT:
				paintUnitDeploy(xPos, yPos, unit1);
				break;
			case MOVING_UNIT:
				paintUnitMove(xPos, yPos, unit1, xPos2, yPos2);
				break;
			case PLACE_DEPLOY_POINT:
				paintDeployPoint(xPos, yPos, unit1);
				break;
			case COMBAT:
				CombatEvent combatEvent = (CombatEvent) event;
				paintCombat(xPos, yPos, unit1, xPos2, yPos2, unit2, 
						combatEvent.getCombatResult(), 
						combatEvent.getCombatType());
				break;
			case UNITBASEATTACK:
				paintBaseAttack(xPos, yPos, unit1);
				break;
			case FINISH_ACTION:
				this.finishAction();
				break;
			case ARTILLERY_FIRING:
				this.paintFiringArtillery(xPos, yPos, xPos2, yPos2, unit1, unit2);
				break;
		}
	}
	
	private void paintFiringArtillery(int sourceX, int sourceY, int targetX, int targetY, Unit unit1, Unit unit2)
	{
		int direction = unit1.isOwnedByPlayer1()?-1:1;
		VerticalAnimationSeries fireAnimationSeries = Animator.getFiringAnimationSeries(unit1);
		fireAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), sourceX, sourceY + direction, targetX, targetY);
		Animation removeAnimation = Animator.getSimpleCombatUnit1DestroyedAnimation(unit2);
		removeAnimation.playAnimation(this.cellPanel.getCell(targetX, targetY));
	}
	
	private void finishAction()
	{
		if (this.gameGrid.isPlayer1Turn())
			this.switchScreenState(STANDARD);
	}
	
	private void showNextTurn(boolean isPlayer1Turn)
	{
		this.switchScreenState(MOVING_UNITS);
	}
	
	private void showNewTurn(boolean isPlayer1Turn)
	{
		if (isPlayer1Turn)
			this.startHumanTurn();
		else
			this.startComputerTurn();
	}
	
	private void paintUnitDeploy(int xPos, int yPos, Unit unit1)
	{
		Animation deployAnimation = Animator.getDeployAnimation(unit1);
		Cell cell = this.cellPanel.getCell(xPos, yPos);
		deployAnimation.playAnimation(cell);
	}
	
	private void paintUnitMove(int xPos, int yPos, Unit unit1, int xPos2, int yPos2)
	{
		AnimationSeries moveAnimationSeries = Animator.getMoveAnimationSeries(unit1);
		moveAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), xPos, yPos, xPos2, yPos2);
	}
	
	private void paintCombat(int xPos, int yPos, Unit unit1, int xPos2, 
			int yPos2, Unit unit2, CombatResult combatResult, CombatType combatType)
	{
		Animation combatAnimation;
		switch(combatResult)
		{
		case UNIT1DESTROYED:
			combatAnimation = Animator.getSimpleCombatUnit1DestroyedAnimation(unit1);
			break;
		case UNIT2DESTROYED:
			combatAnimation = Animator.getSimpleCombatUnit2DestroyedAnimation(unit2);
			break;
		default:
			combatAnimation = Animator.getSimpleCombatDrawAnimation(unit1);	
		}
		
		Cell cell1 = this.cellPanel.getCell(xPos, yPos);
		Cell cell2 = this.cellPanel.getCell(xPos2, yPos2);
		combatAnimation.playTwoCellAnimation(cell1, cell2);
	}
	
	private void paintDeployPoint(int xPos, int yPos, Unit unit1)
	{
		
	}
	
	private void paintBaseAttack(int xPos, int yPos, Unit unit1)
	{
		if (yPos != -1 && yPos != Main.GRIDHEIGHT)
		{
			Animation combatAnimation = Animator.getBaseAttackAnimation(unit1);
			Cell cell = this.cellPanel.getCell(xPos, yPos);
			Cell baseCell = this.cellPanel.getBaseCell(xPos, !unit1.isOwnedByPlayer1());
			combatAnimation.playTwoCellAnimation(cell, baseCell);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.screenState == GameScreenState.ACTIVATING_ABILITY)
		{
			this.activateAbility(e.getX(), e.getY());
		}
		else if (this.screenState == GameScreenState.DEPLOYING_UNIT)
		{
			tryDeployingUnit(e.getX(), e.getY());
		}
	}
	
	private void tryDeployingUnit(int x, int y)
	{
		int cellX = this.getColumnFromPosition(x);
		int cellY = this.getRowFromPosition(y);
		if (cellX != -1 && cellY != -1)
			this.checkForDeployPoint(cellX, cellY);
	}
	
	private void checkForDeployPoint(int x, int y)
	{
		if (this.gameGrid.getPlayer1DeploymentPoints()[x] == y && this.cellPanel.getCell(x, y).unit == null)
		{
			this.gameGrid.deployUnit(this.unitToDeploy, x);
		}
	}
	
	private void activateAbility(int x, int y)
	{
		int cellX = this.getColumnFromPosition(x);
		int cellY = this.getRowFromPosition(y);
		if (cellX != -1 && cellY != -1)
			this.checkForAbility(cellX, cellY);
	}
	
	private int getColumnFromPosition(int x)
	{
		for(int i = 0; i < Main.GRIDWIDTH; i++)
		{
			int paintedX = this.cellPanel.getCell(i, 0).paintedX;
			if (x > paintedX && x < (paintedX + Main.CELLWIDTH))
				return i;
		}
		return -1;
	}
	
	private int getRowFromPosition(int y)
	{
		for(int i = 0; i < Main.GRIDHEIGHT; i++)
		{
			int paintedY = this.cellPanel.getCell(0, i).paintedY;
			if (y > paintedY && y < (paintedY + Main.CELLHEIGHT))
				return i;
		}
		return -1;
	}
	
	public void checkForAbility(int x, int y)
	{
		Unit unit = this.gameGrid.getUnitAt(x, y);
		if (unit == null)
			return;
		AbilityType abilityType = unit.getUnitType().getAbilityType();
		if (abilityType != null)
		{
			this.switchScreenState(GameScreenState.STANDARD);
			this.gameGrid.activateAbility(x, y, abilityType, unit);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
