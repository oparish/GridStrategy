package panes;

import static buttons.ButtonType.NEXT_TURN;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import buttons.MyButton;
import main.Main;
import screens.GameScreen;

@SuppressWarnings("serial")
public class ControlPane extends JPanel
{
	public ControlPane(GameScreen gameScreen)
	{
		super();
		this.setLayout(new GridBagLayout());
		this.setupButtons(gameScreen);
	}
	
	private void setupButtons(GameScreen gameScreen)
	{
		MyButton nextTurnButton = new MyButton(NEXT_TURN);
		this.add(nextTurnButton, Main.getBaseConstraints(0, 0));
		nextTurnButton.addActionListener(gameScreen);
	}
}
