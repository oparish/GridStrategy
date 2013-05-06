package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum UnitType
{
	TEST_UNIT("TestG");
	
	private static final String IMAGES = "Images\\";
	private static final String FILEEXTENSION = ".png";
	private BufferedImage image;
	
	public BufferedImage getImage() {
		return image;
	}

	private UnitType(String filename)
	{
		try {
			this.image = loadImage(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage loadImage(String filename) throws IOException
	{
		File testG = new File(IMAGES + filename + FILEEXTENSION);
		return ImageIO.read(testG);
	}
}
