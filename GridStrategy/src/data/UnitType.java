package data;

import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Main;

public enum UnitType
{
	TEST_UNIT("TestG", "TestE", 1);
	
	private BufferedImage image1;
	private BufferedImage image2;
	private final int speed;
	
	public int getSpeed() {
		return speed;
	}

	public BufferedImage getImage(boolean player1) {
		if (player1)
			return this.image1;
		else
			return this.image2;
	}

	private UnitType(String filename1, String filename2, int speed)
	{
		try {
			this.image1 = Main.loadImage(filename1);
			this.image2 = Main.loadImage(filename2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.speed = speed;
	}
	

}
