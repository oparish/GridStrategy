package panes;

import java.awt.image.BufferedImage;

public class PaintArea
{
	public Integer baseX;
	public Integer baseY;
	public Integer paintedX;
	public Integer paintedY;
	public CellImage overlayImage;
	
	public PaintArea(Integer X, Integer Y)
	{
		this.baseX = X;
		this.baseY = Y;
	}
	
	public BufferedImage getImage()
	{
		if (this.overlayImage != null)
			return this.overlayImage.getImage();
		else
			return null;
	}
	public BufferedImage getImage2()
	{
		return this.getImage();
	}
}
