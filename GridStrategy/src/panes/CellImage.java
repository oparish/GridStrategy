package panes;

import java.awt.image.BufferedImage;

import main.Main;

public enum CellImage
{
	ARROW("Arrow"), ARROW2("Arrow2");
	
	private BufferedImage image;
	
	public BufferedImage getImage() {
		return image;
	}

	private CellImage(String fileName)
	{
		this.image = Main.loadImage(fileName);
	}
}
