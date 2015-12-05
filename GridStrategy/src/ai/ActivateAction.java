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

public class ActivateAction extends DeployAction
{	
	public ActivateAction(List<Integer> integers)
	{
		super(integers);
	}
	
	public ActivateAction(HashMap<ActionFieldName, Integer> fields)
	{
		super(fields);
	}
	
	public ColumnSearchCondition getColumnSearchCondition() {
		int value = this.actionFields.get(ActionFieldName.ACTIVATECONDITION);
		return ColumnSearchCondition.values()[value];
	}
	
	public void setColumnSearchCondition(ColumnSearchCondition columnSearchCondition)
	{
		this.actionFields.put(ActionFieldName.ACTIVATECONDITION, columnSearchCondition.ordinal());
	}

	public ActivateAction(int columnPos, UnitType unitType, ColumnSearchCondition columnSearchCondition)
	{
		super(columnPos, unitType);
		this.setColumnSearchCondition(columnSearchCondition);
	}
	
	public String toString()
	{
		return"		Activate Action: " + this.getUnitType().toString() + ", " + this.getColumnPos() + ", " + this.getColumnSearchCondition().name();
	}
	
	public boolean attemptAction(GameGrid gameGrid, boolean isPlayer1, int column)
	{
		int columnPos = this.getColumnPos();
		if (columnPos == Main.NO_SPECIFIC_COLUMN)
			columnPos = column;
		boolean result = gameGrid.activateCplayerUnit(isPlayer1, this.getUnitType(), columnPos, 
				this.getColumnSearchCondition(), null);
		return result;
	}
	
	public boolean checkViability(ObservationBatch observationBatch, int column, boolean isPlayer1)
	{
		for (int i = 0; i < Main.GRIDHEIGHT; i++)
		{
			Unit unit = observationBatch.getUnits()[column][i];
			if (unit != null && unit.getUnitType() == this.getUnitType())
				return true;
		}
		return false;
	}
}
