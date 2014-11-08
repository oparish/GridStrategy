package assembler;

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
	public ListPanel(ActionListener actionListener, CPlayer cPlayer)
	{
		super();
		this.setLayout(new GridLayout(2, 1));
		ArrayList<Rule> rules = cPlayer.getRules();
		JList<Rule> testList = new JList<Rule>(rules.toArray(new Rule[rules.size()]));
		testList.setCellRenderer(new RuleListCellRenderer());
		this.add(new JScrollPane(testList));
		this.add(new ButtonPanel(actionListener));
	}
	
	private class RuleListCellRenderer implements ListCellRenderer<Rule>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Rule> arg0, Rule arg1, int index, boolean arg3,
				boolean arg4) {
			return new JLabel(String.valueOf(index));
		}
		
	}
}
