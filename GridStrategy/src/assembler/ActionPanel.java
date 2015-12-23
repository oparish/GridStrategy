package assembler;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ai.Action;
import ai.Condition;

public class ActionPanel extends JPanel
{	
	boolean dirty;
	
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public ActionPanel(ActionFieldPanel actionFieldPanel, AssemblerList<Action> actionsList)
	{
		super();
		this.setLayout(new GridLayout(1, 2));
		this.add(actionFieldPanel);
		this.add(new JScrollPane(actionsList));
	}
	
	
}
