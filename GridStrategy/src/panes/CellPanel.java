package panes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import animation.EffectPosition;
import animation.EffectTask;
import main.Main;

public class CellPanel extends JPanel
{
	private ArrayList<MyLine> lines;
	private ArrayList<MyLine> endZoneLines;
	private GridInfo gridInfo;
	
	private final int fullHeight;
	
	public CellPanel(GridInfo gridInfo)
	{
		super();
		this.gridInfo = gridInfo;
		this.fullHeight = gridInfo.gridHeight + Main.CELLHEIGHT + Main.CELLHEIGHT + 2;
		this.setLayout(null);
		this.setupEndZoneLines();
		this.setupLines();
		this.setPanelBounds();
	}
	
	private void setPanelBounds()
	{
		Dimension size = new Dimension(gridInfo.gridWidth, this.fullHeight);
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
			MyLine columnLine = new MyLine(xPos, 0, xPos, this.gridInfo.gridHeight - 1);
			this.lines.add(columnLine);
		}
		
		for (Integer yPos : lineYPositions)
		{
			MyLine rowLine = new MyLine(0, yPos, this.gridInfo.gridWidth - 1, yPos);
			this.lines.add(rowLine);
		}
	}
	
	private void setupEndZoneLines()
	{
		int gridWidthPixels = Main.GRIDWIDTH * (Main.CELLWIDTH + 1);
		int gridHeightPixels = Main.GRIDHEIGHT * (Main.CELLHEIGHT + 1);
		this.endZoneLines = new ArrayList<MyLine>();
		MyLine firstVertLine = new MyLine(0, 0, 0, Main.CELLHEIGHT);
		MyLine secondVertLine = new MyLine(gridWidthPixels, 0, gridWidthPixels, 0 + Main.CELLHEIGHT);
		MyLine firstHorzLine = new MyLine(0, 0, gridWidthPixels, 0);
		this.endZoneLines.add(firstVertLine);
		this.endZoneLines.add(secondVertLine);
		this.endZoneLines.add(firstHorzLine);
		MyLine thirdVertLine = new MyLine(0, gridHeightPixels + Main.CELLHEIGHT + 1, 0, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		MyLine fourthVertLine = new MyLine(gridWidthPixels, gridHeightPixels + Main.CELLHEIGHT + 1, gridWidthPixels, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		MyLine secondHorzLine = new MyLine(0, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2, gridWidthPixels, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		this.endZoneLines.add(thirdVertLine);
		this.endZoneLines.add(fourthVertLine);
		this.endZoneLines.add(secondHorzLine);
	}
	
	private ArrayList<Integer> setupLinePositions(int gridLength, int cellLength)
	{
		ArrayList<Integer> linePositions = new ArrayList<Integer>();
		for (int i = 0; i < gridLength; i += (cellLength + 1))
		{
			linePositions.add(i);
		}
		return linePositions;
	}
	
	private void drawLines(ArrayList<MyLine> lines, Graphics2D g2d, Integer xpos, Integer ypos)
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
	
	private void drawEndZoneCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (Cell[] cellColumn : new Cell[][]{this.gridInfo.player1BaseCells, this.gridInfo.player2BaseCells})
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
		Double yDouble =(Double) (bounds.getHeight() - (this.fullHeight))/2;
		Integer xpos = xDouble.intValue();
		Integer ypos = yDouble.intValue();
		
		this.drawLines(this.lines, g2d, xpos, ypos + Main.CELLHEIGHT + 1);
		this.drawLines(this.endZoneLines, g2d, xpos, ypos);
		this.drawCells(g2d, xpos, ypos + Main.CELLHEIGHT + 1);
		this.drawEndZoneCells(g2d, xpos, ypos);
	}
	
	public void repaintCell(Cell cell)
	{
		if (cell.paintedX != null)
		{
			BufferedImage image = cell.getImage();
			Graphics2D g2d = (Graphics2D) this.getGraphics();
			if (image != null)
				g2d.drawImage(image, cell.paintedX, cell.paintedY, 
					this);
			else
				g2d.clearRect(cell.paintedX, cell.paintedY, 
						Main.CELLWIDTH, Main.CELLHEIGHT);
		}
	}
	
	public Cell getCell(int x, int y)
	{
		return this.gridInfo.cells[x][y];
	}
	
	public Cell getBaseCell(int x, boolean player1)
	{
		if (player1)
			return this.gridInfo.player1BaseCells[x];
		else
			return this.gridInfo.player2BaseCells[x];	
	}
	
	public void paintEffect(Cell cell, Effect effect)
	{
		BufferedImage image = effect.getImage();
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		g2d.drawImage(image, cell.paintedX, cell.paintedY,
					this);
	}
	
	public void paintEffect(Cell cell, Effect effect, EffectPosition effectPosition)
	{
		BufferedImage image = effect.getImage();
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		Double yValue = cell.paintedY + (effectPosition.getPositionNumber() * Main.CELLHEIGHT);
		g2d.drawImage(image, cell.paintedX, yValue.intValue(), this);
	}
	
	public void showDeployPoints(Integer[] deployPositions)
	{
		for (int i = 0; i < deployPositions.length; i++)
		{
			Cell cell = this.gridInfo.cells[i][deployPositions[i]];
			if (cell.unit == null)
				this.paintImageIntoCell(cell, CellImage.ARROW);
		}
	}
	
	public void clearDeployPoints(Integer[] deployPositions)
	{
		for (int i = 0; i < deployPositions.length; i++)
		{
			Cell cell = this.gridInfo.cells[i][deployPositions[i]];
			cell.cellImage = null;
			this.repaintCell(cell);
		}
	}
	
	private void paintImageIntoCell(Cell cell, CellImage image)
	{
		cell.cellImage = image;
		this.repaintCell(cell);
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
