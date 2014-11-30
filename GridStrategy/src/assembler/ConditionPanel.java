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
	private AssemblerList<Condition> hierarchyList;
	private AssemblerList<Condition> gateList;
	private List<Condition> hierarchyContents = new ArrayList<Condition>();
	
	public ConditionPanel(Assembler assembler, ConditionFieldPanel conditionFieldPanel)
	{
		super();
		this.setLayout(new GridLayout(1, 3));
		this.hierarchyList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.HIERARCHY);
		this.hierarchyList.setCellRenderer(new ConditionListCellRenderer());
		this.hierarchyList.addListSelectionListener(assembler);
		this.gateList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.GATE);
		this.gateList.setCellRenderer(new ConditionListCellRenderer());
		this.gateList.addListSelectionListener(assembler);
		this.add(new JScrollPane(this.hierarchyList));
		this.add(new JScrollPane(this.gateList));
		this.add(conditionFieldPanel);
	}
	
	public void addToHierarchyList(Condition condition)
	{
		hierarchyContents.add(condition);
		Condition[] conditions = new Condition[hierarchyContents.size()];
		this.hierarchyContents.toArray(conditions);
		this.hierarchyList.setListData(conditions);
	}
	
	public void truncateHierarchyList(int index)
	{
		Condition[] conditions = new Condition[index];
		this.hierarchyContents = hierarchyContents.subList(0, index);
		this.hierarchyContents.toArray(conditions);
		this.hierarchyList.setListData(conditions);
	}
	
	public void updateGateList(GateCondition gateCondition)
	{
		this.gateList.setListData(new Condition[]{gateCondition.getCondition1(), gateCondition.getCondition2()});
	}
	
	public void clearHierarchyList()
	{
		this.hierarchyContents.clear();
		this.hierarchyList.setListData(new Condition[]{});
	}
	
	public void clearGateList()
	{
		this.gateList.setListData(new Condition[]{});
	}
	
	private class ConditionListCellRenderer implements ListCellRenderer<Condition>
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
