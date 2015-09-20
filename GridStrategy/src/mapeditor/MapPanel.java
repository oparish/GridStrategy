package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.Terrain;
import data.UnitType;
import animation.Animator;
import main.Main;
import panes.Cell;
import panes.LinesPanel;
import panes.PaintArea;
import screens.GameScreenState;

public class MapPanel extends LinesPanel
{
	public static final int STARTX = 0;
	public static final int STARTY = 0;
	private Terrain[][] gridTerrain;
	public void setGridTerrain(Terrain[][] gridTerrain)
	{
		this.gridTerrain = gridTerrain;
		this.repaint();
	}

	public Terrain[][] getGridTerrain() {
		return gridTerrain;
	}

	private Graphics2D g2d;
		
	public MapPanel(MapEditor mapEditor)
	{
		super();
		this.setupImages();
		int width = (Main.CELLWIDTH * Main.GRIDWIDTH) + Main.GRIDWIDTH + 1;
		int height = (Main.CELLHEIGHT * Main.GRIDHEIGHT) + Main.GRIDHEIGHT + 1;
		this.setupLines(width, height);
		this.setPanelBounds(width, height);
		this.addMouseListener(mapEditor);
	}
	
	private void setupImages()
	{
		this.gridTerrain = new Terrain[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridTerrain[i][j] = Terrain.GRASS;
			}
		}
	}
	
	public void clearMap()
	{
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridTerrain[i][j] = Terrain.GRASS;
			}
		}
		this.repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		this.g2d = (Graphics2D) g;
		this.drawLines(this.lines, g2d, STARTX, STARTY);
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.g2d.drawImage(this.gridTerrain[i][j].getImage(), 1 + (Main.CELLWIDTH * i) + i, 1 + (Main.CELLHEIGHT * j) + j, this);
			}
		}
	}
	
	public void updateCell(int x, int y, Terrain terrain)
	{
		this.gridTerrain[x][y] = terrain;
		this.repaint();
	}
}
