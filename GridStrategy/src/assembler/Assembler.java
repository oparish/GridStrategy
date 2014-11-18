package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ai.CPlayer;
import ai.Rule;
import main.FileOperations;

public class Assembler extends JFrame implements ActionListener, ChangeListener, ListSelectionListener
{
	ListPanel listPanel;
	RulePanel rulePanel;
	
	public Assembler() throws IOException
	{
		super();
		CPlayer cPlayer = FileOperations.loadCPlayer(this, true);
		this.setLayout(new GridLayout(1,2));
		this.listPanel = new ListPanel(this, cPlayer);
		this.add(this.listPanel);
		this.rulePanel = new RulePanel(this, this.listPanel);
		this.add(this.rulePanel);
		this.setSize(1200, 500);
	}
	
	
	public static void main(String args[])
	{
		Assembler assembler;
		try
		{
			assembler = new Assembler();
			assembler.setVisible(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		this.rulePanel.processPanelControlEvent(source);
	}


	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object source = e.getSource();
		this.rulePanel.processPanelControlEvent(source);	
	}


	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
		{
			AssemblerList<?> list = ((AssemblerList<?>) e.getSource());
			AssemblerListType type = list.getType();
			switch(type)
			{
				case RULE:
					this.rulePanel.changeSelectedRule((Rule) list.getSelectedValue(), list.getSelectedIndex());
					break;
			}
		}
	}
}
