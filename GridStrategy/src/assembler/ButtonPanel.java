package assembler;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel
{
	public ButtonPanel(ActionListener actionListener)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		for (AssemblerButtonType buttonType : AssemblerButtonType.values())
		{
			this.add(new AssemblerButton(buttonType, actionListener));
		}
	}
}
