package panes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Main;
import data.Terrain;
import data.Unit;

public class Cell extends PaintArea
{
	private Unit unit;
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit)
	{
		this.unit = unit;
		BufferedImage terrainImage = this.terrain.getImage();
		if (unit == null)
		{
			terrainImage = this.terrain.getImage();
			this.image1 = terrainImage;
			this.image2 = terrainImage;
		}
		else
		{
			terrainImage = this.terrain.getImage();
			this.image1 = this.createCellImage(terrainImage, unit.getImage());
			this.image2 = this.createCellImage(terrainImage, unit.getImage2());
		}
	}
	
	private BufferedImage createCellImage(BufferedImage terrainImage, BufferedImage unitImage)
	{
		BufferedImage newImage = new BufferedImage(Main.CELLWIDTH, Main.CELLHEIGHT, terrainImage.getType());
		Graphics newGraphics = newImage.getGraphics();
		newGraphics.drawImage(terrainImage, 0, 0, null);
		newGraphics.drawImage(unitImage, 0, 0, null);
		return newImage;
	}

	private Terrain terrain;
	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	private BufferedImage image1;
	private BufferedImage image2;
	
	public Cell(Integer X, Integer Y)
	{
		super(X, Y);
	}
	
	public BufferedImage getImage()
	{
		return this.image1;
	}
	
	public BufferedImage getImage2()
	{
		return this.image2;
	}
}
