package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Assembler extends JFrame implements ActionListener, ChangeListener
{
	public Assembler()
	{
		super();
		this.setLayout(new GridLayout(1,2));
		this.add(new ListPanel(this));
		this.add(new RulePanel(this));
		this.setSize(1200, 500);
	}
	
	
	public static void main(String args[])
	{
		Assembler assembler = new Assembler();
		assembler.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
