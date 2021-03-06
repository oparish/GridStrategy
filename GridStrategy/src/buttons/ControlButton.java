package buttons;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ControlButton extends JButton
{
	private static final int CONTROLBUTTONWIDTH = 240;
	private static final int CONTROLBUTTONHEIGHT = 30;
	ControlButtonType buttonType;
	
	public ControlButton(ControlButtonType buttonType, ActionListener listener)
	{
		super(buttonType.getText());
		this.buttonType = buttonType;
		Dimension dimension = new Dimension(CONTROLBUTTONWIDTH, CONTROLBUTTONHEIGHT);
		this.setPreferredSize(dimension);
		this.addActionListener(listener);
	}
	
	public ControlButtonType getButtonType() {
		return buttonType;
	}
}
