package panes;

import java.awt.image.BufferedImage;

import main.Main;

public enum Effect
{
	BATTLE("BattleCloud"), PROJECTILE("ArtilleryShot");
	
	private BufferedImage image;
	
	public BufferedImage getImage() {
		return image;
	}

	Effect(String fileName)
	{
		this.image = Main.loadImage(fileName);
	}
}
