package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel
{
	public ButtonPanel(ActionListener actionListener)
	{
		super();
		this.setLayout(new GridLayout(3, 5));
		for (AssemblerButtonType buttonType : AssemblerButtonType.values())
		{
			this.add(new AssemblerButton(buttonType, actionListener));
		}
	}
}
