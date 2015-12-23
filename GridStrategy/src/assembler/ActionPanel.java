package assembler;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ai.Action;
import ai.Condition;

public class ActionPanel extends JPanel
{	
	public ActionPanel(ActionFieldPanel actionFieldPanel, AssemblerList<Action> actionsList)
	{
		super();
		this.setLayout(new GridLayout(1, 2));
		this.add(actionFieldPanel);
		this.add(new JScrollPane(actionsList));
	}
}
