package panes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.UnitType;

@SuppressWarnings("serial")
public class GridPane extends JPanel
{
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 10;
	private final static int CELLWIDTH = 40;
	private final static int CELLHEIGHT = 40;
	
	private ArrayList<MyLine> lines;
	private Cell[][] cells;
	private Integer gridWidth;
	private Integer gridHeight;
	
	public GridPane()
	{
		super();
		this.setupBoundaries();
		this.setupLines();
		this.setupCells();
	}
	
	private void setupCells()
	{
		this.cells = new Cell[GRIDWIDTH][GRIDHEIGHT];
		Integer xpos;
		Integer ypos;
		for (int i = 0; i < GRIDWIDTH; i++)
		{
			xpos = i + (i * CELLWIDTH) + 1;
			for (int j = 0; j < GRIDHEIGHT; j++)
			{
				ypos = j + (j * CELLHEIGHT) + 1;
				cells[i][j] = new Cell(xpos, ypos);
			}
		}
	}
	
	public void setCellContent(int x, int y, UnitType unitType)
	{
		Cell cell = this.cells[x][y];
		cell.image = unitType.getImage();
	}
	
	public void deleteCellContent(int x, int y)
	{
		Cell cell = this.cells[x][y];
		cell.image = null;
	}
	
	private void setupBoundaries()
	{
		this.gridWidth = GRIDWIDTH * (CELLWIDTH + 1);
		this.gridHeight = GRIDHEIGHT * (CELLHEIGHT + 1);
	}
	
	private void setupLines()
	{
		this.lines = new ArrayList<MyLine>();
			
		ArrayList<Integer> lineXPositions = setupLinePositions(gridWidth, CELLWIDTH);
		ArrayList<Integer> lineYPositions = setupLinePositions(gridHeight, CELLHEIGHT);		
		
		for (Integer xPos : lineXPositions)
		{
			MyLine columnLine = new MyLine(xPos, 0, xPos, this.gridWidth);
			this.lines.add(columnLine);
		}
		
		for (Integer yPos : lineYPositions)
		{
			MyLine rowLine = new MyLine(0, yPos, this.gridHeight, yPos);
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
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = g2d.getClipBounds();
		Double xDouble =(Double) (bounds.getWidth() - this.gridWidth)/2;
		Double yDouble =(Double) (bounds.getHeight() - this.gridHeight)/2;
		Integer xpos = xDouble.intValue();
		Integer ypos = yDouble.intValue();
		
		this.drawLines(g2d, xpos, ypos);
		this.drawCells(g2d, xpos, ypos);
	}
	
	private void drawCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (Cell[] cellColumn : this.cells)
		{
			for (Cell cell : cellColumn)
			{
				if (cell.image != null)
				{
					g2d.drawImage(cell.image, cell.X + xpos, cell.Y + ypos, this);
				}
			}
		}
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
	
	private class Cell
	{
		public Integer X;
		public Integer Y;
		public BufferedImage image;
		
		public Cell(Integer X, Integer Y)
		{
			this.X = X;
			this.Y = Y;
		}
	}
}
