package assembler;

import java.awt.GridLayout;
import java.awt.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

public class ConditionPanel extends JPanel
{
	public ConditionPanel(ChangeListener listener)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(new List());
		this.add(new List());
		this.add(new ConditionFieldPanel(listener));
	}
}
