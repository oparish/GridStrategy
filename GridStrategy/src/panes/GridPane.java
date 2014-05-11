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
		this.gridInfo.baseY = this.gridInfo.rowNumbers[Main.GRIDHEIGHT - 1] 
				+ Main.CELLHEIGHT + 1;
		
		this.gridInfo.base1RowNumber = this.gridInfo.gridHeight + Main.CELLHEIGHT + 1;
		this.gridInfo.base2RowNumber = 1;
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
		cell.unit = unit;
		new MyEvent(this, EventType.DEPLOYING_UNIT);
	}
	
	public void deleteCellContent(Cell cell)
	{
		cell.unit = null;
	}
	
	public void repaintCell(Cell cell)
	{
		this.cellPanel.repaintCell(cell);
	}
	
	public void paintEffect(Cell cell, Effect effect)
	{
		this.cellPanel.paintEffect(cell, effect);
	}
	
	public void paintEffect(Cell cell, Effect effect, EffectPosition effectPosition)
	{
		this.cellPanel.paintEffect(cell, effect, effectPosition);
	}
	
	public Cell getCell(int x, int y)
	{
		return this.cellPanel.getCell(x, y);
	}
	
	public Cell getBaseCell(int x, boolean player1)
	{
		return this.cellPanel.getBaseCell(x, player1);
	}
	
	private void setupBoundaries()
	{
		this.gridInfo.gridWidth = Main.GRIDWIDTH * (Main.CELLWIDTH + 1) + 1;
		this.gridInfo.gridHeight = Main.GRIDHEIGHT * (Main.CELLHEIGHT + 1) + 1;
	}
}
