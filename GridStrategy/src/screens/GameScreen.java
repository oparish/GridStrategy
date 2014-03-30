package screens;

import static screens.GameScreenState.ACTIVATING_ABILITY;
import static screens.GameScreenState.COMPUTER_PLAYING;
import static screens.GameScreenState.DEPLOYING_UNIT;
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
import buttons.ColumnButton;
import buttons.ControlButton;
import main.Main;
import panes.ControlPane;
import panes.GridPane;
import panes.InfoPane;
import panes.MessagePane;

@SuppressWarnings("serial")
public class GameScreen extends JFrame implements ActionListener, MyEventListener, MouseListener
{
	private GridPane gridPane;
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
		this.gridPane.disableColumnButtons();
		this.controlPane.disableControls();
	}
	
	private void switchToStandardScreen()
	{
		this.gridPane.disableColumnButtons();
		this.controlPane.runningPlayerOperation(false);
	}
	
	private void switchToDeployingScreen()
	{
		this.gridPane.enableValidColumnButtons();
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
	
	public void deployUnit(ColumnButton columnButton)
	{
		int xpos = columnButton.getXPos();
		this.gameGrid.deployUnit(this.unitToDeploy, xpos);
		this.switchScreenState(STANDARD);
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
		else if (button instanceof ColumnButton)
		{
			this.deployUnit((ColumnButton) button);
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
		}
	}
	
	private void showNextTurn(boolean isPlayer1Turn)
	{
		
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
		deployAnimation.playAnimation(xPos, yPos);
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
		
		combatAnimation.playTwoCellAnimation(xPos, yPos, xPos2, yPos2);
	}
	
	private void paintDeployPoint(int xPos, int yPos, Unit unit1)
	{
		
	}
	
	private void paintBaseAttack(int xPos, int yPos, Unit unit1)
	{
		if (yPos != -1 && yPos != Main.GRIDHEIGHT)
		{
			Animation combatAnimation = Animator.getBaseAttackAnimation(unit1);
			combatAnimation.playAnimation(xPos, yPos);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.screenState == GameScreenState.ACTIVATING_ABILITY)
		{
			this.activateAbility(e.getX(), e.getY());
		}
	}
	
	private void activateAbility(int x, int y)
	{
		int cellX = (int) Math.floor(x / Main.CELLWIDTH);
		int cellY = (int) Math.floor(y / Main.CELLHEIGHT);
		this.checkForAbility(cellX, cellY);
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
			this.gameGrid.activateAbility(x, y, abilityType);
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
