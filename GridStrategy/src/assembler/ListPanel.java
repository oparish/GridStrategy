package assembler;

import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class ListPanel extends JPanel
{
	public ListPanel(ActionListener actionListener)
	{
		super();
		this.setLayout(new GridLayout(2, 1));
		List testList = new List();
		testList.add("One");
		this.add(testList);
		this.add(new ButtonPanel(actionListener));
	}
}
