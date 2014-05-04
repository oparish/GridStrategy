package ai;

import java.util.ArrayList;
import java.util.List;

import ai.Action.ActionFieldName;
import main.FileOperations;
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
		return super.toString() + ", " + this.getColumnSearchCondition().name();
	}
}
