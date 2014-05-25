package buttons;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;

public class LabelledButtonPanel extends JPanel
{
	public LabelledButtonPanel(JButton button, String labelText)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(button);
		
		JLabel label = new JLabel(labelText);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(label);
	}
}
