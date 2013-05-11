package data;

import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Main;

public enum UnitType
{
	TEST_UNIT("TestG");
	
	private BufferedImage image;
	
	public BufferedImage getImage() {
		return image;
	}

	private UnitType(String filename)
	{
		try {
			this.image = Main.loadImage(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
