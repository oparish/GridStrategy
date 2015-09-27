package panes;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.Unit;
import data.UnitType;
import animation.Animator;
import animation.AtomicAnimation;
import animation.EffectPosition;
import animation.EffectTask;
import main.Main;

public class CellPanel extends LinesPanel implements ActionListener
{
	private ArrayList<MyLine> endZoneLines;
	private GridInfo gridInfo;
	private BufferedImage g2dImage;
	private Graphics2D g2d;
	
	private final int fullHeight;
	private int animationCounter = 0;

	private Timer timer = null;
	boolean animationTicker = false;
	
	public CellPanel(GridInfo gridInfo)
	{
		super();
		this.gridInfo = gridInfo;
		this.fullHeight = gridInfo.gridHeight + Main.CELLHEIGHT + Main.CELLHEIGHT + 2;
		this.setupEndZoneLines();
		this.setupLines(gridInfo.gridWidth, gridInfo.gridHeight);
		this.setPanelBounds(gridInfo.gridWidth, this.fullHeight);
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
		MyLine thirdVertLine = 
				new MyLine(0, gridHeightPixels + Main.CELLHEIGHT + 1, 0, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		MyLine fourthVertLine = 
				new MyLine(gridWidthPixels, gridHeightPixels + Main.CELLHEIGHT + 1, gridWidthPixels, 
						gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		MyLine secondHorzLine = 
				new MyLine(0, gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2, gridWidthPixels, 
						gridHeightPixels + Main.CELLHEIGHT + Main.CELLHEIGHT + 2);
		this.endZoneLines.add(thirdVertLine);
		this.endZoneLines.add(fourthVertLine);
		this.endZoneLines.add(secondHorzLine);
	}
	
	private void drawCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (Cell[] cellColumn : this.gridInfo.cells)
		{
			for (Cell cell : cellColumn)
			{
				cell.paintedX = cell.baseX + xpos;
				cell.paintedY = cell.baseY + ypos;
				cell.setUnit(null);
				this.paintCell(cell, false, false);
			}
		}
	}
	
	private void drawEndZoneCells(Graphics2D g2d, Integer xpos, Integer ypos)
	{
		for (PaintArea[] cellColumn : new PaintArea[][]{this.gridInfo.player1BaseCells, this.gridInfo.player2BaseCells})
		{
			for (PaintArea paintArea : cellColumn)
			{
				paintArea.paintedX = paintArea.baseX + xpos;
				paintArea.paintedY = paintArea.baseY + ypos;
				if (paintArea instanceof Cell)
				{
					Cell cell2 = (Cell) paintArea;
					if (cell2.getUnit() != null)
					{
						g2d.drawImage(paintArea.getImage(), paintArea.paintedX, paintArea.paintedY, 
								this);
					}
				}
			}
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Rectangle bounds = g.getClipBounds();
		Double xDouble =(Double) (bounds.getWidth() - this.gridInfo.gridWidth)/2;
		Double yDouble =(Double) (bounds.getHeight() - (this.fullHeight))/2;
		Integer xpos = xDouble.intValue();
		Integer ypos = yDouble.intValue();
		
		if (this.g2dImage == null)
		{
			this.g2dImage = (BufferedImage) this.createImage(((Double)bounds.getWidth()).intValue(), ((Double)bounds.getHeight()).intValue());
			this.g2d = this.g2dImage.createGraphics();
			this.drawLines(this.lines, this.g2d, xpos, ypos + Main.CELLHEIGHT + 1);
			this.drawLines(this.endZoneLines, this.g2d, xpos, ypos);
			this.drawCells(this.g2d, xpos, ypos + Main.CELLHEIGHT + 1);
			this.drawEndZoneCells(this.g2d, xpos, ypos);
		}

		g.drawImage(this.g2dImage, 0, 0, this);

		if (this.timer == null)
		{
			this.timer = new Timer(Animator.UNIT_SECONDS, this);
			this.timer.start();
		}

	}
	
	public void paintCell(PaintArea cell, boolean image2, boolean repaint)
	{
		if (cell.paintedX != null)
		{
			BufferedImage image;
			
			if (image2)
				image = cell.getImage2();
			else
				image = cell.getImage();
			
			if (image != null)
			{
				this.g2d.clearRect(cell.paintedX, cell.paintedY, 
						Main.CELLWIDTH, Main.CELLHEIGHT);
				this.g2d.drawImage(image, cell.paintedX, cell.paintedY, 
						this);
			}
			else
				this.g2d.clearRect(cell.paintedX, cell.paintedY, 
						Main.CELLWIDTH, Main.CELLHEIGHT);
		}
		if (repaint)
			this.repaint();
	}
	
	public Cell getCell(int x, int y)
	{
		return this.gridInfo.cells[x][y];
	}
	
	public PaintArea getBaseCell(int x, boolean player1)
	{
		if (player1)
			return this.gridInfo.player1BaseCells[x];
		else
			return this.gridInfo.player2BaseCells[x];	
	}
	
	public void paintEffect(PaintArea paintArea, Effect effect)
	{
		BufferedImage image = effect.getImage();
		this.g2d.drawImage(image, paintArea.paintedX, paintArea.paintedY,
					this);
	}
	
	public void paintUnitImage(PaintArea paintArea, Unit unit)
	{
		BufferedImage image = unit.getImage();
		this.g2d.drawImage(image, paintArea.paintedX, paintArea.paintedY,
					this);
	}
	
	public void clearArea(PaintArea paintArea)
	{
		this.g2d.clearRect(paintArea.paintedX, paintArea.paintedY, 
				Main.CELLWIDTH, Main.CELLHEIGHT);
	}
	
	public void paintEffect(PaintArea paintArea, Effect effect, EffectPosition effectPosition)
	{
		BufferedImage image = effect.getImage();
		Double yValue = paintArea.paintedY + (effectPosition.getPositionNumber() * Main.CELLHEIGHT);
		this.g2d.drawImage(image, paintArea.paintedX, yValue.intValue(), this);
	}
	
	public void showDeployPoints(Integer[] deployPositions)
	{
		for (int i = 0; i < deployPositions.length; i++)
		{
			PaintArea paintArea = this.gridInfo.getDeployPointPaintArea(i, deployPositions[i]);
			if (!(paintArea instanceof Cell) || ((Cell) paintArea).getUnit() == null)
				this.paintImageIntoCell(paintArea, CellImage.ARROW);
		}
	}
	
	public void clearDeployPoints(Integer[] deployPositions)
	{
		for (int i = 0; i < deployPositions.length; i++)
		{
			PaintArea paintArea = this.gridInfo.getDeployPointPaintArea(i, deployPositions[i]);
			paintArea.overlayImage = null;
			this.paintCell(paintArea, false, true);
		}
	}
	
	private void paintImageIntoCell(PaintArea paintArea, CellImage image)
	{
		paintArea.overlayImage = image;
		this.paintCell(paintArea, false, true);
	}

	private void animationTick()
	{
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.paintCell(this.gridInfo.cells[i][j], this.animationTicker, false);
			}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.animationCounter % Animator.TICKER_PAUSE == 0)
			this.animationTicker = !this.animationTicker;
		this.animationTick();
		Animator.playAnimation(this.animationCounter);
		this.repaint();
		this.animationCounter++;
		if (this.animationCounter == Animator.CYCLE_TIME)
			this.animationCounter = 0;
	}
}
