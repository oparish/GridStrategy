package panes;

import static buttons.ButtonType.CANCEL;
import static buttons.ButtonType.DEPLOY_UNIT;
import static buttons.ButtonType.MAIN_MENU;
import static buttons.ButtonType.NEXT_TURN;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

import buttons.ButtonType;
import buttons.ControlButton;
import main.Main;
import screens.GameScreen;

@SuppressWarnings("serial")
public class ControlPane extends JPanel
{
	private ControlButton nextTurnButton;
	private ControlButton mainMenuButton;
	private ControlButton deployUnitButton;
	private ControlButton cancelButton;
	
	public ControlPane(GameScreen gameScreen)
	{
		super();
		this.setLayout(new GridBagLayout());
		this.setupButtons(gameScreen);
	}
	
	
	public void runningOperation(boolean value)
	{
		nextTurnButton.setEnabled(!value);
		mainMenuButton.setEnabled(!value);
		deployUnitButton.setEnabled(!value);
		cancelButton.setEnabled(value);
	}
	
	private void setupButtons(GameScreen gameScreen)
	{
		this.nextTurnButton = new ControlButton(NEXT_TURN, gameScreen);
		this.mainMenuButton = new ControlButton(MAIN_MENU, gameScreen);
		this.deployUnitButton = new ControlButton(DEPLOY_UNIT, gameScreen);
		this.cancelButton = new ControlButton(CANCEL, gameScreen);
		this.add(this.mainMenuButton, Main.getAnchoredConstraints(0, 0));
		this.add(this.deployUnitButton, Main.getAnchoredConstraints(0, 1));
		this.add(this.nextTurnButton, Main.getAnchoredConstraints(0, 2));
		this.add(this.cancelButton, Main.getAnchoredConstraints(0, 3));
	}
}
