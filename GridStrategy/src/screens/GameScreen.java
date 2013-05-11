package screens;

import static screens.GameScreenState.DEPLOYING_UNIT;
import static screens.GameScreenState.STANDARD;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import data.UnitType;
import dialogs.UnitDialog;

import buttons.ColumnButton;
import buttons.ControlButton;
import main.Main;

import panes.ControlPane;
import panes.GridPane;
import panes.MessagePane;

@SuppressWarnings("serial")
public class GameScreen extends JFrame implements ActionListener
{
	private GridPane gridPane;
	private ControlPane controlPane;
	private MessagePane messagePane;
	private GameScreenState screenState;
	
	private Integer[] player1DeploymentPoints;
	private Integer[] player2DeploymentPoints;

	private UnitType typeToDeploy;
	private int turnMoves;

	public GameScreen()
	{
		super();
		this.setupDeploymentPoints();
		this.setLayout(new GridBagLayout());
		this.gridPane = new GridPane(this);
		this.controlPane = new ControlPane(this);
		this.messagePane = new MessagePane();
		this.add(this.gridPane, Main.getFillConstraints(0,0,1,2));
		this.add(this.controlPane, Main.getFillConstraints(1,0,1,1));
		this.add(this.messagePane, Main.getFillConstraints(1,1,1,1));
		this.switchScreenState(STANDARD);
		this.newTurn();
	}
	
	public Integer[] getPlayer1DeploymentPoints() {
		return player1DeploymentPoints;
	}
	
	public Integer[] getPlayer2DeploymentPoints() {
		return player2DeploymentPoints;
	}
	
	private void newTurn()
	{
		turnMoves = 0;
	}
	
	private void setupDeploymentPoints()
	{
		int gridWidth = Main.getGridwidth();
		this.player1DeploymentPoints = new Integer[gridWidth];
		this.player2DeploymentPoints = new Integer[gridWidth];
		
		for (int i = 0; i < gridWidth; i++)
		{
			this.player1DeploymentPoints[i] = Main.getGridheight() - 1;
			this.player2DeploymentPoints[i] = 0;
		}
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
		default:
		}
	}
	
	private void switchToStandardScreen()
	{
		this.gridPane.disableColumnButtons();
		this.controlPane.runningOperation(false);
	}
	
	private void switchToDeployingScreen()
	{
		this.gridPane.enableValidColumnButtons();
		this.controlPane.runningOperation(true);
	}
	
	private void selectUnit()
	{
		new UnitDialog(this).setVisible(true);
	}
	
	public void readyToDeployUnit(UnitType unitType)
	{
		this.typeToDeploy = unitType;
		this.switchScreenState(DEPLOYING_UNIT);
	}
	
	public void deployUnit(ColumnButton columnButton)
	{
		int xpos = columnButton.getXpos();
		int dep = this.player1DeploymentPoints[xpos];
		this.gridPane.setCellContent(xpos, dep, this.typeToDeploy);
		this.gridPane.repaint();
		this.switchScreenState(STANDARD);
		this.noteMove();
	}
	
	private void nextTurn()
	{
		
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
	
	private void noteMove()
	{
		this.turnMoves++;
		this.checkTurnMoves();
	}
	
	private void checkTurnMoves()
	{
		if (this.turnMoves == Main.getMovesperturn())
		{
			changePlayer();
		}
	}
	
	private void changePlayer()
	{
		
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
				case CANCEL:
					this.cancelOperation();
					break;
				default:
			}
		}
		else if (button instanceof ColumnButton)
		{
			this.deployUnit((ColumnButton) button);
		}
	}
}
