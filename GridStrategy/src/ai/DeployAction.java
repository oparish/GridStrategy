package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.Action.ActionFieldName;
import main.FileOperations;
import main.Main;
import data.GameGrid;
import data.Unit;
import data.UnitType;

public class DeployAction extends Action
{	
	public DeployAction(int columnPos, UnitType unitType)
	{
		super(columnPos);
		this.setUnitType(unitType);
	}
	
	public DeployAction(List<Integer> integers)
	{
		super(integers);
	}
	
	public DeployAction(HashMap<ActionFieldName, Integer> fields)
	{
		super(fields);
	}
	
	public String toString()
	{
		return ("		Deploy Action: " + this.getUnitType().toString() + ", " + this.getColumnPos());
	}
	
	public boolean attemptAction(GameGrid gameGrid, boolean isPlayer1, int checkResult)
	{
		Unit unit = new Unit(isPlayer1, this.getUnitType());
		if (!gameGrid.getAvailableUnitTypes(isPlayer1).contains(this.getUnitType()))
			return false;
		
		int columnPos = this.getColumnPos();
		if (columnPos == Main.NO_SPECIFIC_COLUMN)
			columnPos = checkResult;
		return gameGrid.deployUnit(unit, columnPos);
	}
	
	public boolean checkViability(ObservationBatch observationBatch, int column, boolean isPlayer1)
	{
		int deployPos = observationBatch.getPlayerDeploymentPoints(isPlayer1)[column];
		if ((isPlayer1 && deployPos == -1) || (!isPlayer1 && deployPos == Main.GRIDHEIGHT))
			return true;
		else
			return observationBatch.getUnits()[column][deployPos] == null;
	}
}
