package assembler;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

public class RulePanel extends JPanel
{
	public RulePanel(ChangeListener listener)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.add(new ConditionPanel(listener));
		this.add(new ActionPanel(listener));
	}
}
