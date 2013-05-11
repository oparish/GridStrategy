package buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ControlButton extends JButton
{
	ButtonType buttonType;
	
	public ControlButton(ButtonType buttonType, ActionListener listener)
	{
		super(buttonType.getText());
		this.buttonType = buttonType;
		this.addActionListener(listener);
	}
	
	public ButtonType getButtonType() {
		return buttonType;
	}
}
