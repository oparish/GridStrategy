package panes;

import java.awt.image.BufferedImage;

import data.Unit;

public class Cell
{
	public Integer baseX;
	public Integer baseY;
	public Integer paintedX;
	public Integer paintedY;
	public Unit unit;
	
	public Cell(Integer X, Integer Y)
	{
		this.baseX = X;
		this.baseY = Y;
	}
	
	public BufferedImage getImage()
	{
		if (unit == null)
			return null;
		else
			return this.unit.getImage();
	}
}
