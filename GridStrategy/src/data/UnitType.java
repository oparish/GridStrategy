package data;

import static data.UnitCategory.INTERCEPTOR;

import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Main;

public enum UnitType
{
	TEST_UNIT("TestG", "TestE", 1, 1, new UnitCategory[]{}),
	DEMOLISHER("Player1Demolisher", "Player2Demolisher", 1, 2, 
			new UnitCategory[]{}),
	INTERCEPTOR("Player1Interceptor", "Player2Interceptor", 1, 1, 
			new UnitCategory[]{UnitCategory.INTERCEPTOR});
	
	private BufferedImage image1;
	private BufferedImage image2;
	private final int speed;
	private final int baseDamage;
	private final UnitCategory[] categories;
	
	public int getBaseDamage() {
		return baseDamage;
	}

	public int getSpeed() {
		return speed;
	}
	
	public boolean hasCategory(UnitCategory testCategory)
	{
		for (UnitCategory category : this.categories)
		{
			if (category == testCategory)
				return true;
		}
		return false;
	}

	public BufferedImage getImage(boolean player1) {
		if (player1)
			return this.image1;
		else
			return this.image2;
	}
	
	private UnitType(String filename1, String filename2, int speed, 
			int baseDamage, UnitCategory[] categories)
	{
		try {
			this.image1 = Main.loadImage(filename1);
			this.image2 = Main.loadImage(filename2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.speed = speed;
		this.baseDamage = baseDamage;
		this.categories = categories;
	}
	

}
