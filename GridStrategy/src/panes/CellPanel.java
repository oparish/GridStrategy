package panes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JPanel;

import animation.EffectTask;
import main.Main;

public class CellPanel extends JPanel
{
	private ArrayList<MyLine> lines;
	private GridInfo gridInfo;
	
	public CellPanel(GridInfo gridInfo)
	{
		super();
		this.gridInfo = gridInfo;
		this.setLayout(null);
		this.setupLines();
		this.setPanelBounds();
	}
	
	private void setPanelBounds()
	{
		Dimension size = new Dimension(gridInfo.gridWidth + 1, 
				gridInfo.gridHeight + 1);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	private void setupLines()
	{
		this.lines = new ArrayList<MyLine>();
			
		ArrayList<Integer> lineXPositions = 
				setupLinePositions(this.gridInfo.gridWidth, Main.CELLWIDTH);
		ArrayList<Integer> lineYPositions = 
				setupLinePositions(this.gridInfo.gridHeight, Main.CELLHEIGHT);		
		
		for (Integer xPos : lineXPositions)
		{
			MyLine columnLine = new MyLine(xPos, 0, xPos, this.gridInfo.gridWidth);
			this.lines.add(columnLine);
		}
		
		for (Integer yPos : lineYPositions)
		{
			MyLine rowLine = new MyLine(0, yPos, this.gridInfo.gridHeight, yPos);
			this.lines.add(rowLine);
		}
	}
	
	private ArrayList<Integer> setupLinePositions(int gridLength, int cellLength)
	{
		ArrayList<Integer> linePositions = new ArrayList<Integer>();
		for (int i = 0; i < gridLength+1; i += (cellLength+1))
		{
			linePositions.add(i);
		}
		return linePositions;
	}
	
	private void drawLines(Graphics2D g2d, Integer xpos, Integer ypos)
	{	
		for (MyLine line : lines)
		{
			Integer X1 = line.X1 + xpos;
			Integer X2 = line.X2 + xpos;
			Integer Y1 = line.Y1 + ypos;
			Integer Y2 = line.Y2 + ypos;
			g2d.drawLine(X1, Y1, X2, Y2);
		}
	}
	
	private void drawCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (Cell[] cellColumn : this.gridInfo.cells)
		{
			for (Cell cell : cellColumn)
			{
				cell.paintedX = cell.baseX + xpos;
				cell.paintedY = cell.baseY + ypos;
				if (cell.unit != null)
				{
					g2d.drawImage(cell.getImage(), cell.paintedX, cell.paintedY, 
							this);
				}
			}
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = g2d.getClipBounds();
		Double xDouble =(Double) (bounds.getWidth() - this.gridInfo.gridWidth)/2;
		Double yDouble =(Double) (bounds.getHeight() - this.gridInfo.gridHeight)/2;
		Integer xpos = xDouble.intValue();
		Integer ypos = yDouble.intValue();
		
		this.drawLines(g2d, xpos, ypos);
		this.drawCells(g2d, xpos, ypos);
	}
	
	public void repaintCell(int x, int y)
	{
		Cell cell = this.gridInfo.cells[x][y];
		BufferedImage image = cell.getImage();
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		if (image != null)
			g2d.drawImage(image, cell.paintedX, cell.paintedY, 
				this);
		else
			g2d.clearRect(cell.paintedX, cell.paintedY, 
					Main.CELLWIDTH, Main.CELLHEIGHT);
	}
	
	public void paintEffect(int x, int y, Effect effect)
	{
		Cell cell = this.gridInfo.cells[x][y];
		BufferedImage image = effect.getImage();
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.drawImage(image, cell.paintedX, cell.paintedY,
					this);
	}
	
	private class MyLine
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
}
