package panes;

import static main.Main.CELLHEIGHT;
import static main.Main.CELLWIDTH;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import main.Main;
import screens.GameScreen;
import animation.EffectPosition;
import data.GameGrid;
import data.Terrain;
import data.Unit;
import events.EventType;
import events.MyEvent;

@SuppressWarnings("serial")
public class GridPane extends JPanel
{	
	private CellPanel cellPanel;
	public CellPanel getCellPanel() {
		return cellPanel;
	}

	private GridInfo gridInfo;
	public GridInfo getGridInfo() {
		return gridInfo;
	}

	private GameScreen gameScreen;
	
	public GridPane(GameScreen gameScreen, Terrain[][] terrainArray)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.gridInfo = new GridInfo();
		this.gameScreen = gameScreen;
		this.setupBoundaries();
		this.setupNumbers();
		this.setupCells(terrainArray);
		this.setupCellPanel();
	}
	
	private void setupCellPanel()
	{
		this.cellPanel = new CellPanel(this.gridInfo);
		this.cellPanel.addMouseListener(this.gameScreen);
		this.add(this.cellPanel);
	}
	
	private void setupNumbers()
	{
		this.gridInfo.rowNumbers = new Integer[Main.GRIDHEIGHT];
		this.gridInfo.columnNumbers = new Integer[Main.GRIDWIDTH];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.gridInfo.columnNumbers[i] = i + (i * Main.CELLWIDTH) + 1;
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridInfo.rowNumbers[j] = j + (j * Main.CELLHEIGHT) + 1;
			}
		}
		this.gridInfo.baseY = this.gridInfo.rowNumbers[Main.GRIDHEIGHT - 1] 
				+ Main.CELLHEIGHT + 1;
		
		this.gridInfo.base1RowNumber = this.gridInfo.gridHeight + Main.CELLHEIGHT + 1;
		this.gridInfo.base2RowNumber = 1;
	}
	
	private void setupCells(Terrain[][] terrainArray)
	{
		this.gridInfo.cells = new Cell[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridInfo.cells[i][j] = 
						new Cell(this.gridInfo.columnNumbers[i], 
								this.gridInfo.rowNumbers[j]);
				this.gridInfo.cells[i][j].setTerrain(terrainArray[i][j]);
			}
		}
		
		this.gridInfo.player1BaseCells = new Cell[Main.GRIDWIDTH];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.gridInfo.player1BaseCells[i] = new Cell(this.gridInfo.columnNumbers[i], this.gridInfo.base1RowNumber);
		}
		this.gridInfo.player2BaseCells = new Cell[Main.GRIDWIDTH];	
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.gridInfo.player2BaseCells[i] = new Cell(this.gridInfo.columnNumbers[i], this.gridInfo.base2RowNumber);
		}
	}
	
	public void setCellContent(Cell cell, Unit unit)
	{
		cell.setUnit(unit);
		new MyEvent(this, EventType.DEPLOYING_UNIT);
	}
	
	public void deleteCellContent(Cell cell)
	{
		cell.setUnit(null);
	}
	
	private void setupBoundaries()
	{
		this.gridInfo.gridWidth = Main.GRIDWIDTH * (Main.CELLWIDTH + 1) + 1;
		this.gridInfo.gridHeight = Main.GRIDHEIGHT * (Main.CELLHEIGHT + 1) + 1;
	}
}
