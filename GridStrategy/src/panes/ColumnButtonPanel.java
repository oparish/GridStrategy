package panes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import screens.GameScreen;

import data.GameGrid;

import main.Main;

import buttons.ColumnButton;

public class ColumnButtonPanel extends JPanel
{
	private ColumnButton[] columnButtons;
	private GridInfo gridInfo;
	private GameScreen gameScreen;
	
	public ColumnButtonPanel(GridInfo gridInfo, GameScreen gameScreen)
	{
		this.gameScreen = gameScreen;
		this.gridInfo = gridInfo;
		this.setLayout(null);
		this.setupColumnButtons();
		this.setPanelBounds();
		this.placeColumnButtons();
	}
	
	private void setPanelBounds()
	{
		Dimension size = new Dimension(gridInfo.gridWidth + 1, 
				gridInfo.gridHeight + 1);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	public void disableColumnButtons()
	{
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.columnButtons[i].setEnabled(false);
		}
	}
	
	public void enableValidColumnButtons()
	{
		Integer[] deploymentPoints = this.gameScreen.getPlayer1DeploymentPoints();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			int dep = deploymentPoints[i];

			if (dep == -1 || this.gridInfo.cells[i][dep].unit == null)
				this.columnButtons[i].setEnabled(true);
		}

	}
	
	private void setupColumnButtons()
	{
		ColumnButton.setWidth(Main.CELLWIDTH);
		ColumnButton.setHeight(Main.CELLHEIGHT);
		this.columnButtons = new ColumnButton[Main.GRIDWIDTH];
		
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.columnButtons[i] = new ColumnButton(i, this.gameScreen);
			this.add(this.columnButtons[i]);
		}
	}
	
	public void placeColumnButtons()
	{	
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			this.columnButtons[i].setLocation(this.gridInfo.columnNumbers[i], 0);
		}	
	}
}
