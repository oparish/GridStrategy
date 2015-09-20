package data;

import java.util.ArrayList;

import main.Main;

public class DataMap implements Map
{
	private final int player1InitialLife;
	public int getPlayer1InitialLife() {
		return player1InitialLife;
	}

	public int getPlayer2InitialLife() {
		return player2InitialLife;
	}

	public int getPlayer1Credits() {
		return player1Credits;
	}

	public int getPlayer2Credits() {
		return player2Credits;
	}

	public ArrayList<UnitType> getPlayer1Types() {
		return player1Types;
	}

	public ArrayList<UnitType> getPlayer2Types() {
		return player2Types;
	}

	public Terrain[][] getGridTerrain() {
		return gridTerrain;
	}

	private final int player2InitialLife;
	private final int player1Credits;
	private final int player2Credits;
	private final ArrayList<UnitType> player1Types;
	private final ArrayList<UnitType> player2Types;
	private final Terrain[][] gridTerrain;
	
	public DataMap(ArrayList<Integer> mapData)
	{
		int i = 0;
		this.player1Credits = mapData.get(i);
		i++;
		this.player2Credits = mapData.get(i);
		i++;
		this.player1InitialLife = mapData.get(i);
		i++;
		this.player2InitialLife = mapData.get(i);
		i++;
		int size1 = mapData.get(i);
		i++;
		int j = 0;
		this.player1Types = new ArrayList<UnitType>();
		while (j < size1)
		{
			this.player1Types.add(UnitType.values()[mapData.get(i)]);
			j++;
			i++;
		}
		int size2 = mapData.get(i);
		i++;
		int k = 0;
		this.player2Types = new ArrayList<UnitType>();
		while (k < size2)
		{
			this.player2Types.add(UnitType.values()[mapData.get(i)]);
			k++;
			i++;
		}
		this.gridTerrain = new Terrain[Main.GRIDWIDTH][Main.GRIDHEIGHT];
		for (int s = 0; s < Main.GRIDWIDTH; s++)
		{
			for (int t = 0; t < Main.GRIDHEIGHT; t++)
			{
				this.gridTerrain[s][t] = Terrain.values()[mapData.get(i)];
				i++;
			}
		}
	}
}
