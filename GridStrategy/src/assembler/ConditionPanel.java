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
	
	public ConditionPanel(Assembler assembler)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(new List());
		this.add(new List());
		this.conditionFieldPanel = new ConditionFieldPanel(assembler);
		this.add(this.conditionFieldPanel);
	}
	
	public ConditionFieldPanel getConditionFieldPanel()
	{
		return this.conditionFieldPanel;
	}
	
	public void changeCondition(Condition condition)
	{
		
	}
}
