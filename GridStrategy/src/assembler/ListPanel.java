package assembler;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import ai.CPlayer;
import ai.Rule;

public class ListPanel extends JPanel
{	
	private AssemblerList<Rule> ruleList;
	
	public ListPanel(Assembler assembler, CPlayer cPlayer)
	{
		super();
		this.setLayout(new GridLayout(2, 1));
		ArrayList<Rule> rules = cPlayer.getRules();
		this.ruleList = new AssemblerList<Rule>(rules.toArray(new Rule[rules.size()]), AssemblerListType.RULE);
		ruleList.setCellRenderer(new RuleListCellRenderer());
		ruleList.addListSelectionListener(assembler);
		this.add(new JScrollPane(ruleList));
		this.add(new ButtonPanel(assembler));
	}
	
	public void setListIndex(int index)
	{
		System.out.println(index);
		this.ruleList.setSelectedIndex(index);
	}
	
	private class RuleListCellRenderer implements ListCellRenderer<Rule>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Rule> arg0, Rule arg1, int index, boolean selected,
				boolean arg4)
		{
			String text = String.valueOf(index);
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
