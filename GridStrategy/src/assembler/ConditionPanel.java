package assembler;

import java.awt.GridLayout;
import java.awt.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ai.Condition;

public class ConditionPanel extends JPanel
{
	private ConditionFieldPanel conditionFieldPanel;
	
	public ConditionPanel(ConditionFieldPanel conditionFieldPanel)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(new List());
		this.add(new List());
		this.add(conditionFieldPanel);
	}
	
	public void changeCondition(Condition condition)
	{
		
	}
}
