package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Main;
import data.Unit;
import ai.Condition.ConditionFieldName;
import ai.headers.ColumnConditionHeader;
import ai.headers.SpecificColumnConditionHeader;

public class SpecificColumnCondition extends ColumnCondition
{

	public SpecificColumnCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}
	
	public SpecificColumnCondition(ConditionType conditionType, int number, boolean isPlayer1)
	{
		super(conditionType, number, isPlayer1);
	}
	
	public SpecificColumnCondition(List<Integer> integers, boolean isPlayer1, SpecificColumnConditionHeader conditionHeader)
	{
		super(integers, isPlayer1, conditionHeader);
	}
	
	public Integer getColumn() {
		return this.conditionFields.get(ConditionFieldName.COLUMN);
	}
	
	public void setColumn(Integer column) {
		this.conditionFields.put(ConditionFieldName.COLUMN, column);
	}
	
	public int runCheck(ObservationBatch observationBatch, Action action, int checkStartPos)
	{
		System.out.println("SpecificColumnCondition being treated as ColumnCondition.");
		System.exit(0);
		return -1;
	}
	
	@Override
	protected int runCheck(ObservationBatch observationBatch, Action action)
	{
		Integer column = this.getColumn();
		ArrayList<Unit> conditionUnits = this.findUnits(observationBatch, column);
		this.filterTypes(this.getUnitType(), this.getUnitPlayer(), conditionUnits);
		if (this.checkNumber(observationBatch, conditionUnits))
			return column;
	
		return Main.GENERIC_CHECK_FAILURE;
	}
	
	public SpecificColumnCondition copyConditionWithNewColumn(int index)
	{
		SpecificColumnCondition newCondition = new SpecificColumnCondition((HashMap<Condition.ConditionFieldName, Integer>) this.conditionFields.clone(), this.employerIsPlayer1);
		newCondition.setColumn(index);
		return newCondition;
	}
	
	public String toString(int depth)
	{		
		return "		" + depth + " - Column Condition: " + this.getConditionType() + 
				", Number: " + this.getNumber() + ", Column: " + this.getColumn() + ", Unit Type: " + 
				this.getUnitType() + ", Unit Player: " + this.getUnitPlayer() + ", Row: " + this.getRow();
	}

}
