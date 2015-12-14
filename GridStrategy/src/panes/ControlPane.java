package panes;

import static buttons.ControlButtonType.ACTIVATE_ABILITY;
import static buttons.ControlButtonType.CANCEL;
import static buttons.ControlButtonType.CLEAR_DEPLOY_POINT;
import static buttons.ControlButtonType.DEPLOY_UNIT;
import static buttons.ControlButtonType.MAIN_MENU;
import static buttons.ControlButtonType.NEXT_TURN;
import static buttons.ControlButtonType.QUIT;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import buttons.ControlButtonType;
import buttons.ControlButton;
import main.Main;
import screens.GameScreen;

@SuppressWarnings("serial")
public class ControlPane extends JPanel
{
	private ControlButton nextTurnButton;
	private ControlButton mainMenuButton;
	private ControlButton deployUnitButton;
	private ControlButton activateAbilityButton;
	private ControlButton cancelButton;
	private ControlButton quitButton;
	private ControlButton clearDeployPointButton;
	
	public ControlPane(GameScreen gameScreen)
	{
		super();
		this.setLayout(new GridBagLayout());
		this.setupButtons(gameScreen);
	}
	
	
	public void runningPlayerOperation(boolean value)
	{
		nextTurnButton.setEnabled(!value);
		mainMenuButton.setEnabled(!value);
		deployUnitButton.setEnabled(!value);
		activateAbilityButton.setEnabled(!value);
		clearDeployPointButton.setEnabled(!value);
		cancelButton.setEnabled(value);
	}
	
	public void disableControls()
	{
		nextTurnButton.setEnabled(false);
		mainMenuButton.setEnabled(false);
		deployUnitButton.setEnabled(false);
		activateAbilityButton.setEnabled(false);
		clearDeployPointButton.setEnabled(false);
		cancelButton.setEnabled(false);
	}
	
	private void setupButtons(GameScreen gameScreen)
	{
		this.nextTurnButton = new ControlButton(NEXT_TURN, gameScreen);
		this.mainMenuButton = new ControlButton(MAIN_MENU, gameScreen);
		this.deployUnitButton = new ControlButton(DEPLOY_UNIT, gameScreen);
		this.activateAbilityButton = new ControlButton(ACTIVATE_ABILITY, gameScreen);
		this.clearDeployPointButton = new ControlButton(CLEAR_DEPLOY_POINT, gameScreen);
		this.cancelButton = new ControlButton(CANCEL, gameScreen);
		this.quitButton = new ControlButton(QUIT, gameScreen);
		this.add(this.mainMenuButton, Main.getAnchoredConstraints(0, 0));
		this.add(this.deployUnitButton, Main.getAnchoredConstraints(0, 1));
		this.add(this.nextTurnButton, Main.getAnchoredConstraints(0, 2));
		this.add(this.activateAbilityButton, Main.getAnchoredConstraints(0, 3));
		this.add(this.clearDeployPointButton, Main.getAnchoredConstraints(0, 4));
		this.add(this.cancelButton, Main.getAnchoredConstraints(0, 5));
		this.add(this.quitButton, Main.getAnchoredConstraints(0, 6));
	}
}
