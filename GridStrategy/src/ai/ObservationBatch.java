package ai;

import java.util.ArrayList;

import main.Main;
import data.Unit;

public class ObservationBatch
{
	private final boolean isPlayer1;
	private final int credits;
	private final Integer[] player1DeploymentPoints;
	public Integer[] getPlayer1DeploymentPoints() {
		return player1DeploymentPoints;
	}

	private final Integer[] player2DeploymentPoints;
	
	public Integer[] getPlayer2DeploymentPoints() {
		return player2DeploymentPoints;
	}

	public int getCredits() {
		return credits;
	}

	public boolean isPlayer1() {
		return isPlayer1;
	}

	private final Unit[][] units;
	
	public Unit[][] getUnits() {
		return units;
	}

	public ObservationBatch(boolean isPlayer1, Unit[][] units, int credits, Integer[] player1DeploymentPoints, 
			Integer[] player2DeploymentPoints)
	{
		this.isPlayer1 = isPlayer1;
		this.units = units;
		this.credits = credits;
		this.player1DeploymentPoints = player1DeploymentPoints;
		this.player2DeploymentPoints = player2DeploymentPoints;
	}
	
	public Integer[] getPlayerDeploymentPoints(boolean isPlayer1)
	{
		if (isPlayer1)
			return this.getPlayer1DeploymentPoints();
		else
			return this.getPlayer2DeploymentPoints();
	}
	
	public ArrayList<Integer> getValidDeployColumns()
	{
		Integer[] deployPoints = this.getPlayerDeploymentPoints(this.isPlayer1);
		ArrayList<Integer> deployColumns = new ArrayList<Integer>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			if (units[i][deployPoints[i]] == null)
				deployColumns.add(i);
		}
		return deployColumns;
	}
}
