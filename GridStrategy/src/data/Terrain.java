package data;

import java.awt.image.BufferedImage;

import main.Main;

public enum Terrain {
	GRASS("Grass"), DESERT("Desert");
	
	BufferedImage image;
	
	public BufferedImage getImage() {
		return image;
	}

	private Terrain(String filename)
	{
		this.image = Main.loadImage(filename);
	}
}
