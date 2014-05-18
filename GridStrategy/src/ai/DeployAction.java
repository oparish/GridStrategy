package ai;

import java.util.ArrayList;
import java.util.List;

import ai.Action.ActionFieldName;
import main.FileOperations;
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
	
	public UnitType getUnitType() {
		int value = this.actionFields.get(ActionFieldName.UNITTYPE);
		return UnitType.values()[value];
	}
	
	public void setUnitType(UnitType unitType)
	{
		this.actionFields.put(ActionFieldName.UNITTYPE, unitType.ordinal());
	}
	
	public String toString()
	{
		return ("		Action: " + this.getUnitType().toString() + ", " + this.getColumnPos());
	}
	
	public boolean attemptAction(GameGrid gameGrid, boolean isPlayer1)
	{
		Unit unit = new Unit(isPlayer1, this.getUnitType());
		return gameGrid.deployUnit(unit, this.getColumnPos());
	}
}
