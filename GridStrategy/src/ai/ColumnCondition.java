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
	
	public void setConditionType(ConditionType type)
	{
		this.conditionFields.put(ConditionFieldName.CONDITION_TYPE, type.ordinal());
	}
	
	public void setNumber(Integer number)
	{
		this.conditionFields.put(ConditionFieldName.NUMBER, number);
	}
	
	public void setUnitType(UnitType unitType)
	{
		this.conditionFields.put(ConditionFieldName.UNIT_TYPE, unitType.ordinal());
	}

	public void setRow(Integer row) {
		this.conditionFields.put(ConditionFieldName.ROW, row);
	}
	
	public Boolean getUnitPlayer()
	{
		Integer player = this.conditionFields.get(ConditionFieldName.UNIT_PLAYER);
		if (player == null)
			return null;
		else
			return player == 1;
	}
	
	public void setUnitPlayer(boolean isPlayer1)
	{
		if (isPlayer1)
			this.conditionFields.put(ConditionFieldName.UNIT_PLAYER, 1);
		else
			this.conditionFields.put(ConditionFieldName.UNIT_PLAYER, 0);
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
	
	public String toString(int depth)
	{		
		return "		" + depth + " - Column Condition: " + this.getConditionType() + 
				", Number: " + this.getNumber() + ", Unit Type: " + 
				this.getUnitType() + ", Unit Player: " + this.getUnitPlayer() + ", Row: " + this.getRow();
	}

	@Override
	protected int runCheck(ObservationBatch observationBatch, Action action)
	{
		return this.runCheck(observationBatch, action, 0);
	}
	
	public int runCheck(ObservationBatch observationBatch, Action action, int checkStartPos)
	{
		int [] colList = Main.getRandomColumnList();
		for (int i = checkStartPos; i < Main.GRIDWIDTH; i++)
		{
			int pos = colList[i];
			ArrayList<Unit> conditionUnits = this.findUnits(observationBatch, pos);
			this.filterTypes(this.getUnitType(), this.getUnitPlayer(), conditionUnits);
			if (this.checkNumber(observationBatch, conditionUnits))
				return pos;
		}
	
		return Main.GENERIC_CHECK_FAILURE;
	}
	
	protected boolean checkNumber(ObservationBatch observationBatch, 
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
	
	protected void filterTypes(UnitType filterType, Boolean unitIsPlayer1, ArrayList<Unit> conditionUnits)
	{
		ArrayList<Unit> removeList = new ArrayList<Unit>();
		for (Unit unit : conditionUnits)
		{
			if (!Unit.match(unit, filterType, unitIsPlayer1 == null ? null : unitIsPlayer1 == this.employerIsPlayer1))
				removeList.add(unit);
		}
		for (Unit unit : removeList)
		{
			conditionUnits.remove(unit);
		}
	}
	
	private void addUnitToList(ArrayList<Unit> units, Unit unit)
	{
		if (unit != null)
			units.add(unit);
	}
	
	protected ArrayList<Unit> findUnits(ObservationBatch observationBatch, Integer column)
	{
		ArrayList<Unit> conditionUnits;
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
}
