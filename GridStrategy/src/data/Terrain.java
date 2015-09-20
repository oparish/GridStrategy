package data;

import static data.TerrainCategory.ROUGH;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Main;

public enum Terrain {
	GRASS("Grass", new TerrainCategory[]{}), DESERT("Desert", new TerrainCategory[]{ROUGH});
	
	BufferedImage image;
	TerrainCategory[] terrainCategories; 
	
	public BufferedImage getImage() {
		return image;
	}

	private Terrain(String filename, TerrainCategory[] categories)
	{
		this.image = Main.loadImage(filename);
		this.terrainCategories = categories;
	}
	
	public boolean hasCategory(TerrainCategory testCategory)
	{
		for (TerrainCategory category : this.terrainCategories)
		{
			if (category == testCategory)
				return true;
		}
		return false;
	}
}
