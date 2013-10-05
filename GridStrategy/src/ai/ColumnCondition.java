package ai;

import java.util.ArrayList;

import main.Main;
import data.Unit;

public class ColumnCondition extends Condition
{
	private final ConditionType conditionType;
	private final int number;
	private Integer column;
	private Unit unit;
	private Integer row;
	
	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}
	public Unit getUnit() {
		return this.unit;
	}

	public int getNumber() {
		return number;
	}
	
	public ConditionType getConditionType() {
		return conditionType;
	}
	
	public void setColumn(Integer column) {
		this.column = column;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public ColumnCondition(ConditionType conditionType, int number)
	{
		this.conditionType = conditionType;
		this.number = number;
	}
	
	public String toString(int depth)
	{
		String unitString = (this.unit == null) ? null : 
			this.unit.toString();
		return "		" + depth + " - Column Condition: " + this.conditionType + 
				", Number: " + this.number + ", Column: " + this.column + ", " + 
				unitString + ", Row: " + this.row;
	}

	@Override
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits = this.findUnits(observationBatch);
		this.filterTypes(this.unit, conditionUnits);
		return this.checkNumber(observationBatch, conditionUnits);	
	}
	
	private boolean checkNumber(ObservationBatch observationBatch, 
			ArrayList<Unit> conditionUnits)
	{
		int conditionNumber = this.number;
		int unitNumber = conditionUnits.size();
		
		if (!observationBatch.isPlayer1())
			conditionNumber = Main.GRIDHEIGHT - 1 - conditionNumber;
		
		switch(this.conditionType)
		{
		case GREATER_THAN:
			return unitNumber > conditionNumber;
		case SMALLER_THAN:
			return unitNumber < conditionNumber;
		case EQUAL_TO:
			return unitNumber == conditionNumber;
		default:
			return false;
		}
	}
	
	private void filterTypes(Unit filterType, ArrayList<Unit> conditionUnits)
	{
		if (filterType == null)
		{
			return;
		}
		else
		{
			ArrayList<Unit> removeList = new ArrayList<Unit>();
			for (Unit unit : conditionUnits)
			{
				if (Unit.match(unit, filterType))
					removeList.add(unit);
			}
			for (Unit unit : removeList)
			{
				conditionUnits.remove(unit);
			}
		}
	}
	
	private ArrayList<Unit> findUnits(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits;
		if (this.column == null && this.row == null)
		{
			conditionUnits = findUnitsOnGrid(observationBatch);
		}
		else if (this.column == null)
		{
			conditionUnits = findUnitsPastRow(observationBatch, this.row);
		}
		else if (this.row == null)
		{
			conditionUnits = findUnitsInColumn(observationBatch, this.column);
		}
		else
		{
			conditionUnits = findUnitsInColumnPastRow(observationBatch, 
					this.column, this.row);
		}
		return conditionUnits;
	}
	
	private ArrayList<Unit> findUnitsOnGrid(ObservationBatch observationBatch)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				units.add(observationBatch.getUnits()[i][j]);
			}
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsPastRow(ObservationBatch observationBatch, 
			int row)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				if (j > row)
				{
					units.add(observationBatch.getUnits()[i][j]);
				}
			}
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsInColumn(ObservationBatch observationBatch, 
			int column)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int j = 0; j < Main.GRIDHEIGHT; j++)
		{
			units.add(observationBatch.getUnits()[column][j]);
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsInColumnPastRow
	(ObservationBatch observationBatch, int column, int row)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int j = 0; j < Main.GRIDHEIGHT; j++)
		{
			if (j > row)
			{
				units.add(observationBatch.getUnits()[column][j]);
			}
		}
		return units;
	}
}
