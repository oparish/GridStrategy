package ai;

import data.UnitType;

public class Action
{
	private final int columnPos;
	private final UnitType unitType;

	public Action(int columnPos, UnitType unitType)
	{
		this.columnPos = columnPos;
		this.unitType = unitType;
	}
	
	public String toString()
	{
		return ("		Action: " + unitType.toString() + ", " + columnPos);
	}
	
	public int getColumnPos() {
		return columnPos;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
}
