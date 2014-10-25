package assembler;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import buttons.ControlButtonType;

public class AssemblerButton extends JButton
{
	private static final int ASSEMBLERBUTTONWIDTH = 120;
	private static final int ASSEMBLERBUTTONHEIGHT = 30;
	
	private AssemblerButtonType buttonType;
	
	public AssemblerButton(AssemblerButtonType buttonType, ActionListener listener)
	{
		super(buttonType.getText());
		this.buttonType = buttonType;
		Dimension dimension = new Dimension(ASSEMBLERBUTTONWIDTH, ASSEMBLERBUTTONHEIGHT);
		this.setPreferredSize(dimension);
		this.addActionListener(listener);
	}
}
