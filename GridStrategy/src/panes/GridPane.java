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

import buttons.ColumnButton;

import data.GameGrid;
import data.Unit;
import events.EventType;
import events.MyEvent;

@SuppressWarnings("serial")
public class GridPane extends JPanel
{	
	private ColumnButtonPanel columnButtonPanel;
	private CellPanel cellPanel;
	private GridInfo gridInfo;
	private GameScreen gameScreen;
	
	public GridPane(GameScreen gameScreen)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.gridInfo = new GridInfo();
		this.gameScreen = gameScreen;
		this.setupBoundaries();
		this.setupNumbers();
		this.setupCells();
		this.setupCellPanel();
		this.setupColumnButtonPanel();
	}
	
	private void setupCellPanel()
	{
		this.cellPanel = new CellPanel(this.gridInfo);
		this.cellPanel.addMouseListener(this.gameScreen);
		this.add(this.cellPanel);
	}
	
	private void setupColumnButtonPanel()
	{
		this.columnButtonPanel = new ColumnButtonPanel(this.gridInfo, 
				this.gameScreen);
		this.add(this.columnButtonPanel);
	}
	
	public void disableColumnButtons()
	{
		this.columnButtonPanel.disableColumnButtons();
	}
	
	public void enableValidColumnButtons()
	{
		this.columnButtonPanel.enableValidColumnButtons();
	}
	
	private void setupNumbers()
	{
		this.gridInfo.rowNumbers = new Integer[10];
		this.gridInfo.columnNumbers = new Integer[10];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.gridInfo.columnNumbers[i] = i + (i * Main.CELLWIDTH) + 1;
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridInfo.rowNumbers[j] = j + (j * Main.CELLHEIGHT) + 1;
			}
		}
		this.gridInfo.baseY = this.gridInfo.rowNumbers[Main.GRIDHEIGHT-1] 
				+ Main.CELLHEIGHT + 1;
	}
	
	private void setupCells()
	{
		this.gridInfo.cells = new Cell[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridInfo.cells[i][j] = 
						new Cell(this.gridInfo.columnNumbers[i], 
								this.gridInfo.rowNumbers[j]);
			}
		}
	}
	
	public void setCellContent(int x, int y, Unit unit)
	{
		Cell cell = this.gridInfo.cells[x][y];
		cell.unit = unit;
		new MyEvent(this, EventType.DEPLOYING_UNIT);
	}
	
	public void deleteCellContent(int x, int y)
	{
		Cell cell = this.gridInfo.cells[x][y];
		cell.unit = null;
	}
	
	public void repaintCell(int x, int y)
	{
		this.cellPanel.repaintCell(x, y);
	}
	
	public void paintEffect(int x, int y, Effect effect)
	{
		this.cellPanel.paintEffect(x, y, effect);
	}
	
	private void setupBoundaries()
	{
		this.gridInfo.gridWidth = Main.GRIDWIDTH * (Main.CELLWIDTH + 1);
		this.gridInfo.gridHeight = Main.GRIDHEIGHT * (Main.CELLHEIGHT + 1);
	}
}
