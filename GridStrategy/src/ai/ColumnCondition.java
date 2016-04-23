package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.Condition.ConditionFieldName;
import ai.headers.ColumnConditionHeader;
import ai.headers.ConditionHeader;
import ai.headers.SpecificColumnConditionHeader;
import main.FileOperations;
import main.Main;
import data.Unit;
import data.UnitType;

public class ColumnCondition extends UnitCountCondition
{		
	public ColumnCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}
	
	public ColumnCondition(ConditionType conditionType, int number, boolean isPlayer1)
	{
		super(conditionType, number, isPlayer1);
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
	
	public int runCheck(ObservationBatch observationBatch, int checkStartPos)
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
