package panes;

import java.awt.image.BufferedImage;

public class PaintArea
{
	public Integer baseX;
	public Integer baseY;
	public Integer paintedX;
	public Integer paintedY;
	public CellImage cellImage;
	
	public PaintArea(Integer X, Integer Y)
	{
		this.baseX = X;
		this.baseY = Y;
	}
	
	public BufferedImage getImage()
	{
		if (this.cellImage != null)
			return this.cellImage.getImage();
		else
			return null;
	}
}
