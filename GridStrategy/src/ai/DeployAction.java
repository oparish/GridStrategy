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
	
	public int attemptAction(GameGrid gameGrid, boolean isPlayer1, int checkResult)
	{
		Unit unit = new Unit(isPlayer1, this.getUnitType());
		if (!gameGrid.getAvailableUnitTypes(isPlayer1).contains(this.getUnitType()))
			return Main.GENERIC_CHECK_FAILURE;
		
		int columnPos = this.getColumnPos();
		if (columnPos == Main.NO_SPECIFIC_COLUMN)
			columnPos = checkResult;
		
		if (gameGrid.deployUnit(unit, columnPos))
			return columnPos;
		else if (columnPos == Main.NO_SPECIFIC_COLUMN)
			return CPlayer.TRY_NEW_COLUMN;
		else
			return Main.GENERIC_CHECK_FAILURE;
	}
}
