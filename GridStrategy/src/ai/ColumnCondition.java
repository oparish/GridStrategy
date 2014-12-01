package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.Condition.ConditionFieldName;
import ai.headers.ColumnConditionHeader;
import ai.headers.ConditionHeader;
import main.FileOperations;
import main.Main;
import data.Unit;
import data.UnitType;

public class ColumnCondition extends Condition implements NumberCondition
{		
	public Integer getRow() {
		return this.conditionFields.get(ConditionFieldName.ROW);
	}

	public Integer getColumn() {
		return this.conditionFields.get(ConditionFieldName.COLUMN);
	}
	
	public UnitType getUnitType()
	{
		Integer value = this.conditionFields.get(ConditionFieldName.UNIT_TYPE);
		if (value == null)
			return null;
		else
			return UnitType.values()[value];
	}

	public Integer getNumber() {
		return this.conditionFields.get(ConditionFieldName.NUMBER);
	}
	
	public ConditionType getConditionType() {
		Integer value = this.conditionFields.get(ConditionFieldName.CONDITION_TYPE);
		if (value == null)
			return null;
		else
			return ConditionType.values()[value];
	}
	
	public void setColumn(Integer column) {
		this.conditionFields.put(ConditionFieldName.COLUMN, column);
	}
	
	public void setUnitType(UnitType unitType)
	{
		this.conditionFields.put(ConditionFieldName.UNIT_TYPE, unitType.ordinal());
	}

	public void setRow(Integer row) {
		this.conditionFields.put(ConditionFieldName.COLUMN, row);
	}

	public ColumnCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}
	
	public ColumnCondition(ConditionType conditionType, int number, boolean isPlayer1)
	{
		super(isPlayer1);
		this.conditionFields.put(ConditionFieldName.CONDITION_TYPE, conditionType.ordinal());
		this.conditionFields.put(ConditionFieldName.NUMBER, number);
	}
	
	public ColumnCondition(List<Integer> integers, boolean isPlayer1, ColumnConditionHeader conditionHeader)
	{
		super(integers, isPlayer1, conditionHeader);
	}
	
	public ColumnCondition copyConditionWithNewColumn(int index)
	{
		ColumnCondition newCondition = new ColumnCondition((HashMap<Condition.ConditionFieldName, Integer>) this.conditionFields.clone(), this.isPlayer1);
		newCondition.setColumn(index);
		return newCondition;
	}
	
	public String toString(int depth)
	{
		UnitType unitType = this.getUnitType();
		String unitString;
		if (unitType != null)
			unitString = (new Unit(this.isPlayer1, unitType).toString());
		else
			unitString = "Null";	
			
		return "		" + depth + " - Column Condition: " + this.getConditionType() + 
				", Number: " + this.getNumber() + ", Column: " + this.getColumn() + ", Unit: " + 
				unitString + ", Row: " + this.getRow();
	}

	@Override
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits = this.findUnits(observationBatch);
		this.filterTypes(this.getUnitType(), this.isPlayer1, conditionUnits);
		return this.checkNumber(observationBatch, conditionUnits);	
	}
	
	private boolean checkNumber(ObservationBatch observationBatch, 
			ArrayList<Unit> conditionUnits)
	{
		int unitNumber = conditionUnits.size();
		int number = this.getNumber();
		
		switch(this.getConditionType())
		{
		case GREATER_THAN:
			return unitNumber > number;
		case SMALLER_THAN:
			return unitNumber < number;
		case EQUAL_TO:
			return unitNumber == number;
		default:
			return false;
		}
	}
	
	private void filterTypes(UnitType filterType, boolean unitIsPlayer1, ArrayList<Unit> conditionUnits)
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
				if (!Unit.match(unit, filterType, unitIsPlayer1))
					removeList.add(unit);
			}
			for (Unit unit : removeList)
			{
				conditionUnits.remove(unit);
			}
		}
	}
	
	private void addUnitToList(ArrayList<Unit> units, Unit unit)
	{
		if (unit != null)
			units.add(unit);
	}
	
	private ArrayList<Unit> findUnits(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits;
		Integer column = this.getColumn();
		Integer row = this.getRow();
		if (column == null && row == null)
		{
			conditionUnits = findUnitsOnGrid(observationBatch);
		}
		else if (column == null)
		{
			conditionUnits = findUnitsPastRow(observationBatch, row);
		}
		else if (row == null)
		{
			conditionUnits = findUnitsInColumn(observationBatch, column);
		}
		else
		{
			conditionUnits = findUnitsInColumnPastRow(observationBatch, 
					column, row);
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
				this.addUnitToList(units, observationBatch.getUnits()[i][j]);
			}
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsPastRow(ObservationBatch observationBatch, 
			int row)
	{
		int rowNumber;
		if (!observationBatch.isPlayer1())
			rowNumber = Main.GRIDHEIGHT - 1 - this.getRow();
		else
			rowNumber = this.getRow();
		
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				if (j > rowNumber)
				{
					this.addUnitToList(units, observationBatch.getUnits()[i][j]);
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
			this.addUnitToList(units, observationBatch.getUnits()[column][j]);
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
				this.addUnitToList(units, observationBatch.getUnits()[column][j]);
			}
		}
		return units;
	}

	@Override
	public String getConditionClassName()
	{
		return "Column Condition";
	}
}
