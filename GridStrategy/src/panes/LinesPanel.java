package panes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.Main;


public abstract class LinesPanel extends JPanel
{
	protected ArrayList<MyLine> lines;
	
	public LinesPanel()
	{
		super();
		this.setLayout(null);
	}
	
	protected class MyLine
	{
		public Integer X1;
		public Integer X2;
		public Integer Y1;
		public Integer Y2;
		
		public MyLine(Integer X1, Integer Y1, Integer X2, Integer Y2)
		{
			this.X1 = X1;
			this.Y1 = Y1;
			this.X2 = X2;
			this.Y2 = Y2;
		}
	}
	
	protected void setPanelBounds(int width, int height)
	{
		Dimension size = new Dimension(width, height);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	protected void drawLines(ArrayList<MyLine> lines, Graphics g, Integer xpos, Integer ypos)
	{	
		for (MyLine line : lines)
		{
			Integer X1 = line.X1 + xpos;
			Integer X2 = line.X2 + xpos;
			Integer Y1 = line.Y1 + ypos;
			Integer Y2 = line.Y2 + ypos;
			g.drawLine(X1, Y1, X2, Y2);
		}
	}
	
	protected void setupLines(int width, int height)
	{
		this.lines = new ArrayList<MyLine>();
			
		ArrayList<Integer> lineXPositions = 
				this.setupLinePositions(width, Main.CELLWIDTH);
		ArrayList<Integer> lineYPositions = 
				this.setupLinePositions(height, Main.CELLHEIGHT);		
		
		for (Integer xPos : lineXPositions)
		{
			MyLine columnLine = new MyLine(xPos, 0, xPos, height - 1);
			this.lines.add(columnLine);
		}
		
		for (Integer yPos : lineYPositions)
		{
			MyLine rowLine = new MyLine(0, yPos, width - 1, yPos);
			this.lines.add(rowLine);
		}
	}
	
	protected ArrayList<Integer> setupLinePositions(int gridLength, int cellLength)
	{
		ArrayList<Integer> linePositions = new ArrayList<Integer>();
		for (int i = 0; i < gridLength; i += (cellLength + 1))
		{
			linePositions.add(i);
		}
		return linePositions;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
	}
}
