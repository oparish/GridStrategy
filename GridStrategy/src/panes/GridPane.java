package panes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.Main;

import screens.GameScreen;

import buttons.ColumnButton;

import data.UnitType;

@SuppressWarnings("serial")
public class GridPane extends JPanel
{
	private final static int CELLWIDTH = 40;
	private final static int CELLHEIGHT = 40;
	
	private GameScreen gameScreen;
	private ArrayList<MyLine> lines;
	private ColumnButton[] columnButtons;
	private Cell[][] cells;
	private Integer gridWidth;
	private Integer gridHeight;
	private Integer rowNumbers[];
	private Integer columnNumbers[];
	
	public GridPane(GameScreen gameScreen)
	{
		super();
		this.gameScreen = gameScreen;
		this.setLayout(null);
		this.setupBoundaries();
		this.setupNumbers();
		this.setupLines();
		this.setupCells();
		this.setupColumnButtons();
	}
	
	public void disableColumnButtons()
	{
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			this.columnButtons[i].setEnabled(false);
		}
	}
	
	public void enableValidColumnButtons()
	{
		Integer[] deploymentPoints = this.gameScreen.getPlayer1DeploymentPoints();
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			int dep = deploymentPoints[i];
			if (cells[i][dep].unitType == null)
				this.columnButtons[i].setEnabled(true);
		}
	}
	
	private void setupNumbers()
	{
		this.rowNumbers = new Integer[10];
		this.columnNumbers = new Integer[10];
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			this.columnNumbers[i] = i + (i * CELLWIDTH) + 1;
			for (int j = 0; j < Main.getGridheight(); j++)
			{
				this.rowNumbers[j] = j + (j * CELLHEIGHT) + 1;
			}
		}
	}
	
	private void setupColumnButtons()
	{
		ColumnButton.setWidth(CELLWIDTH);
		ColumnButton.setHeight(CELLHEIGHT);
		this.columnButtons = new ColumnButton[Main.getGridwidth()];
		
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			this.columnButtons[i] = new ColumnButton(i, this.gameScreen);
			this.add(this.columnButtons[i]);
		}
	}
	
	private void setupCells()
	{
		this.cells = new Cell[Main.getGridwidth()][Main.getGridheight()];
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			for (int j = 0; j < Main.getGridheight(); j++)
			{
				cells[i][j] = new Cell(this.columnNumbers[i], this.rowNumbers[j]);
			}
		}
	}
	
	public void setCellContent(int x, int y, UnitType unitType)
	{
		Cell cell = this.cells[x][y];
		cell.unitType = unitType;
	}
	
	public void deleteCellContent(int x, int y)
	{
		Cell cell = this.cells[x][y];
		cell.unitType = null;
	}
	
	private void setupBoundaries()
	{
		this.gridWidth = Main.getGridwidth() * (CELLWIDTH + 1);
		this.gridHeight = Main.getGridheight() * (CELLHEIGHT + 1);
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
		this.placeColumnButtons(xpos, ypos);
	}
	
	private void placeColumnButtons(Integer xpos, Integer ypos)
	{
		Integer yBase = this.rowNumbers[Main.getGridheight()-1] + CELLHEIGHT + 1 + ypos;
		
		for (int i = 0; i < Main.getGridwidth(); i++)
		{
			this.columnButtons[i].setLocation(this.columnNumbers[i] + xpos, yBase);
		}		
	}
	
	private void drawCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (Cell[] cellColumn : this.cells)
		{
			for (Cell cell : cellColumn)
			{
				if (cell.unitType != null)
				{
					g2d.drawImage(cell.getImage(), cell.X + xpos, cell.Y + ypos, this);
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
		public UnitType unitType;
		
		public Cell(Integer X, Integer Y)
		{
			this.X = X;
			this.Y = Y;
		}
		
		public BufferedImage getImage()
		{
			return this.unitType.getImage();
		}
	}
}
