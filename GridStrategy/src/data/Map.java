package data;

import java.util.ArrayList;

public interface Map
{
	public int getPlayer1InitialLife();
	public int getPlayer2InitialLife();
	public int getPlayer1Credits();
	public int getPlayer2Credits();
	public ArrayList<UnitType> getPlayer1Types();
	public ArrayList<UnitType> getPlayer2Types();
	public Terrain[][] getGridTerrain();
}
