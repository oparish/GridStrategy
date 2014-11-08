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

import ai.CPlayer;
import main.FileOperations;

public class Assembler extends JFrame implements ActionListener, ChangeListener
{
	public Assembler() throws IOException
	{
		super();
		CPlayer cPlayer = FileOperations.loadCPlayer(this, true);
		this.setLayout(new GridLayout(1,2));
		this.add(new ListPanel(this, cPlayer));
		this.add(new RulePanel(this));
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
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
