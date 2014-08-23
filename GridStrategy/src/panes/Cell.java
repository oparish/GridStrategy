package panes;

import java.awt.image.BufferedImage;

import data.Unit;

public class Cell extends PaintArea
{
	public Unit unit;
	
	public Cell(Integer X, Integer Y)
	{
		super(X, Y);
	}
	
	public BufferedImage getImage()
	{
		if (unit != null)
			return this.unit.getImage();
		else if (this.cellImage != null)
			return this.cellImage.getImage();
		else
			return null;
	}
}
