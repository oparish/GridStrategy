package screens;

import static java.awt.GridBagConstraints.BOTH;
import static screens.GameScreenState.ACTIVATING_ABILITY;
import static screens.GameScreenState.COMPUTER_PLAYING;
import static screens.GameScreenState.DEPLOYING_UNIT;
import static screens.GameScreenState.MOVING_UNITS;
import static screens.GameScreenState.STANDARD;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import events.EventBase;
import events.EventCell;
import events.EventLocation;
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
import panes.GridInfo;
import panes.GridPane;
import panes.InfoPane;
import panes.MessagePane;
import panes.PaintArea;

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
		this.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        }); 
		this.gameGrid = gameGrid;
		this.setLayout(new GridLayout(1,1));
		this.gridPane = new GridPane(this, this.gameGrid.getMap().getGridTerrain());
		this.cellPanel = this.gridPane.getCellPanel();
		Animator.setGridPane(this.gridPane);
		this.controlPane = new ControlPane(this);
		this.messagePane = new MessagePane();
		this.infoPane = new InfoPane(this.gameGrid);
		JScrollPane jScrollPane = new JScrollPane(this.messagePane);
		this.add(this.gridPane);
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(3,1));
		containerPanel.add(this.controlPane);
		containerPanel.add(this.infoPane);
		containerPanel.add(jScrollPane);
		this.add(containerPanel);
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
		EventLocation eventLocation1 = null;
		Unit unit1 = null;
		EventLocation eventLocation2 = null;
		Unit unit2 = null;
		Boolean isPlayer1Turn = null;
		
		if (event instanceof TurnEvent)
			isPlayer1Turn = ((TurnEvent) event).isPlayer1();
		else if (event instanceof OneUnitEvent)
		{
			OneUnitEvent oneUnitEvent = (OneUnitEvent) event;
			unit1 = oneUnitEvent.getUnit();
			eventLocation1 = oneUnitEvent.getEventLocation();
			if (event instanceof TwoPositionEvent)
			{
				TwoPositionEvent twoPositionEvent = (TwoPositionEvent) event;
				eventLocation2 = twoPositionEvent.getEventLocation2();
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
				paintUnitDeploy(eventLocation1, unit1);
				break;
			case UNITBASEATTACK:
				paintBaseAttack(eventLocation1, unit1);
				break;
			case PLACE_DEPLOY_POINT:
				paintDeployPoint(eventLocation1, unit1);
				break;
			case MOVING_UNIT:
				paintUnitMove(eventLocation1, unit1, eventLocation2);
				break;
			case COMBAT:
				CombatEvent combatEvent = (CombatEvent) event;
				paintCombat(eventLocation1, unit1, eventLocation2, unit2, 
						combatEvent.getCombatResult(), 
						combatEvent.getCombatType());
				break;
			case FINISH_ACTION:
				this.finishAction();
				break;
			case ARTILLERY_HIT:
				this.paintArtilleryHit(eventLocation1, eventLocation2, unit1, unit2);
				break;
			case ARTILLERY_BLOCKED:
				this.paintArtilleryBlocked(eventLocation1, eventLocation2, unit1, unit2);
				break;
			case ARTILLERY_WITHOUT_HIT:
				this.paintArtilleryWithoutHit(eventLocation1, unit1);
				break;
		}
	}
	
	private void paintArtilleryWithoutHit(EventLocation eventLocation1, Unit unit1)
	{
		VerticalAnimationSeries fireAnimationSeries = Animator.getFiringAnimationSeries(unit1);
		fireAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), eventLocation1, new EventBase(eventLocation1.getColumn(), 
				!unit1.isOwnedByPlayer1()));
	}
	
	private void paintDeployPoint(EventLocation eventLocation1, Unit unit1)
	{
		
	}
	
	private void paintArtilleryBlocked(EventLocation eventLocation1, EventLocation eventLocation2, Unit unit1, Unit unit2)
	{
		int direction = unit1.isOwnedByPlayer1()?-1:1;
		VerticalAnimationSeries fireAnimationSeries = Animator.getFiringAnimationSeries(unit1);
		fireAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), eventLocation1, eventLocation2);
	}
	
	private void paintArtilleryHit(EventLocation eventLocation1, EventLocation eventLocation2, Unit unit1, Unit unit2)
	{
		VerticalAnimationSeries fireAnimationSeries = Animator.getFiringAnimationSeries(unit1);
		fireAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), eventLocation1, eventLocation2);
		Animation removeAnimation = Animator.getSimpleCombatUnit1DestroyedAnimation(unit2);
		PaintArea targetArea = this.getPaintAreaFromEventLocation(eventLocation2);
		removeAnimation.playAnimation(targetArea);
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
	
	private void paintUnitDeploy(EventLocation eventLocation, Unit unit1)
	{
		Animation deployAnimation = Animator.getDeployAnimation(unit1);
		PaintArea paintArea = this.getPaintAreaFromEventLocation(eventLocation);
		deployAnimation.playAnimation(paintArea);
	}
	
	private void paintBaseAttack(EventLocation eventLocation, Unit unit1)
	{
		Animation baseAttackAnimation = Animator.getBaseAttackAnimation(unit1);
		PaintArea paintArea = this.getPaintAreaFromEventLocation(eventLocation);
		baseAttackAnimation.playAnimation(paintArea);
	}
	
	private void paintUnitMove(EventLocation eventLocation1, Unit unit1, EventLocation eventLocation2)
	{
		AnimationSeries moveAnimationSeries = Animator.getMoveAnimationSeries(unit1);
		moveAnimationSeries.playAnimations(unit1.isOwnedByPlayer1(), eventLocation1, eventLocation2);
	}
	
	private PaintArea getPaintAreaFromEventLocation(EventLocation eventLocation)
	{
		GridInfo gridInfo = this.gridPane.getGridInfo();
		return gridInfo.getPaintAreaFromEventLocation(eventLocation);
	}
	
	private void paintCombat(EventLocation eventLocation1, Unit unit1, EventLocation eventLocation2, Unit unit2, CombatResult combatResult, CombatType combatType)
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
		
		PaintArea paintArea1 = this.getPaintAreaFromEventLocation(eventLocation1);
		PaintArea paintArea2 = this.getPaintAreaFromEventLocation(eventLocation2);
		combatAnimation.playTwoCellAnimation(paintArea1, paintArea2);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int column = this.getColumnFromPosition(e.getX());
		int row = this.getRowFromPosition(e.getY());
		PaintArea paintArea = this.gridPane.getGridInfo().getDeployPointPaintArea(column, row);
		if (this.screenState == GameScreenState.ACTIVATING_ABILITY && paintArea instanceof Cell)
		{
			this.checkForAbility((Cell) paintArea, column, row);
		}
		else if (this.screenState == GameScreenState.DEPLOYING_UNIT)
		{
			this.checkForDeployPoint(column, row);
		}
	}
	
	private void checkForDeployPoint(int x, int y)
	{
		if (this.gameGrid.getPlayer1DeploymentPoints()[x] == y && (y == -1 || this.cellPanel.getCell(x, y).getUnit() == null))
		{			
			this.cellPanel.clearDeployPoints(this.getPlayer1DeploymentPoints());
			this.gameGrid.deployUnit(this.unitToDeploy, x);
		}
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
	
	public void checkForAbility(Cell cell, int x, int y)
	{
		if (cell.getUnit()== null)
			return;
		AbilityType abilityType = cell.getUnit().getUnitType().getAbilityType();
		if (abilityType != null)
		{
			this.switchScreenState(GameScreenState.STANDARD);
			this.gameGrid.activateAbility(x, y, abilityType, cell.getUnit());
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
