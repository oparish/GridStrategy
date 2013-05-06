package buttons;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class MyButton extends JButton
{
	ButtonType buttonType;
	
	public MyButton(ButtonType buttonType)
	{
		super(buttonType.getText());
		this.buttonType = buttonType;
	}
	
	public ButtonType getButtonType() {
		return buttonType;
	}
}
