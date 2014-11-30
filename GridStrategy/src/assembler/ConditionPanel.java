package assembler;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.List;

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
	private ConditionFieldPanel conditionFieldPanel;
	private AssemblerList<Condition> hierarchyList;
	private AssemblerList<Condition> gateList;
	
	public ConditionPanel(Assembler assembler, ConditionFieldPanel conditionFieldPanel)
	{
		super();
		this.setLayout(new GridLayout(1, 3));
		this.hierarchyList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.CONDITION);
		this.gateList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.CONDITION);
		this.gateList.setCellRenderer(new GateListCellRenderer());
		this.gateList.addListSelectionListener(assembler);
		this.add(new JScrollPane(this.hierarchyList));
		this.add(new JScrollPane(this.gateList));
		this.add(conditionFieldPanel);
	}
	
	public void updateGateList(GateCondition gateCondition)
	{
		this.gateList.setListData(new Condition[]{gateCondition.getCondition1(), gateCondition.getCondition2()});
	}
	
	public void clearGateList()
	{
		this.gateList.setListData(new Condition[]{});
	}
	
	private class GateListCellRenderer implements ListCellRenderer<Condition>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Condition> arg0, Condition condition, int index, boolean selected,
				boolean arg4)
		{
			String text = condition.getConditionClassName();
			if (selected)
			{
				return AssemblerList.getSelectedLabel(text);
			}
			else
			{
				return new JLabel(text);
			}
		}
	}
}
