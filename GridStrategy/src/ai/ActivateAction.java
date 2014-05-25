package ai;

import java.util.ArrayList;
import java.util.List;

import ai.Action.ActionFieldName;
import main.FileOperations;
import data.GameGrid;
import data.UnitType;

public class ActivateAction extends DeployAction
{
	public ActivateAction(List<Integer> integers)
	{
		super(integers);
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
	
	public boolean attemptAction(GameGrid gameGrid, boolean isPlayer1)
	{
		boolean result = gameGrid.activateCplayerUnit(isPlayer1, this.getUnitType(), this.getColumnPos(), 
				this.getColumnSearchCondition());
		return result;
	}
}
