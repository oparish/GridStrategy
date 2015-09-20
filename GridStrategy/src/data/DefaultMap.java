package data;

import java.util.ArrayList;

import main.Main;

public class DefaultMap implements Map
{
	Terrain[][] gridTerrain;
	
	public DefaultMap()
	{
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.gridTerrain[i][j] = Terrain.GRASS;
			}
		}
	}
	
	@Override
	public int getPlayer1InitialLife() {
		return Main.PLAYER1_DEFAULTHP;
	}

	@Override
	public int getPlayer2InitialLife() {
		return Main.PLAYER2_DEFAULTHP;
	}

	@Override
	public int getPlayer1Credits() {
		return Main.PLAYER1_DEFAULTCREDITS;
	}

	@Override
	public int getPlayer2Credits() {
		return Main.PLAYER2_DEFAULTCREDITS;
	}

	@Override
	public ArrayList<UnitType> getPlayer1Types() {
		return UnitType.getDeployableUnitTypes();
	}

	@Override
	public ArrayList<UnitType> getPlayer2Types() {
		return UnitType.getDeployableUnitTypes();
	}

	@Override
	public Terrain[][] getGridTerrain() {
		return this.gridTerrain;
	}

}
