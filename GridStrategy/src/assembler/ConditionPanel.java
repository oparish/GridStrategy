package assembler;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeListener;

import ai.ColumnCondition;
import ai.Condition;
import ai.GateCondition;
import ai.NoCondition;
import ai.Rule;

public class ConditionPanel extends JPanel
{	
	public ConditionPanel(ConditionFieldPanel conditionFieldPanel, AssemblerList<Condition> hierarchyList, AssemblerList<Condition> gateList)
	{
		super();
		this.setLayout(new GridLayout(1, 3));
		this.add(new JScrollPane(hierarchyList));
		this.add(new JScrollPane(gateList));
		this.add(conditionFieldPanel);
	}
}
