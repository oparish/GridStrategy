package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Main;
import ai.Condition.ConditionFieldName;
import ai.headers.ColumnConditionHeader;
import ai.headers.UnitCountConditionHeader;
import data.Unit;
import data.UnitType;

public class UnitCountCondition extends Condition implements NumberCondition
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

	public UnitCountCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}
	
	public UnitCountCondition(ConditionType conditionType, int number, boolean isPlayer1)
	{
		super(isPlayer1);
		this.conditionFields.put(ConditionFieldName.CONDITION_TYPE, conditionType.ordinal());
		this.conditionFields.put(ConditionFieldName.NUMBER, number);
	}
	
	public UnitCountCondition(List<Integer> integers, boolean isPlayer1, UnitCountConditionHeader conditionHeader)
	{
		super(integers, isPlayer1, conditionHeader);
	}
	
	@Override
	protected int runCheck(ObservationBatch observationBatch)
	{
		return this.runCheck(observationBatch, 0);
	}
	
	public int runCheck(ObservationBatch observationBatch, int checkStartPos)
	{
		ArrayList<Unit> conditionUnits = this.findUnitsOnGrid(observationBatch);
		this.filterTypes(this.getUnitType(), this.getUnitPlayer(), conditionUnits);
		if (this.checkNumber(observationBatch, conditionUnits))
		{
			ArrayList<Integer> deployPoints = observationBatch.getValidDeployColumns();
			int rnd = RandomGen.randomInt(deployPoints.size());
			return deployPoints.get(rnd);
		}
	
		return Main.GENERIC_CHECK_FAILURE;
	}
	
	protected ArrayList<Unit> findUnitsOnGrid(ObservationBatch observationBatch)
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
	
	protected void addUnitToList(ArrayList<Unit> units, Unit unit)
	{
		if (unit != null)
			units.add(unit);
	}
	
	protected ArrayList<Unit> findUnitsPastRow(ObservationBatch observationBatch, 
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
	
	public String toString(int depth)
	{		
		return "		" + depth + " - Unit Count Condition: " + this.getConditionType() + 
				", Number: " + this.getNumber() + ", Unit Type: " + 
				this.getUnitType() + ", Unit Player: " + this.getUnitPlayer() + ", Row: " + this.getRow();
	}
}
