package ai;

import java.util.HashMap;
import java.util.List;

import main.Main;
import data.GameGrid;
import data.UnitType;
import ai.Action.ActionFieldName;

public class FurtherInputActivateAction extends ActivateAction
{
	public FurtherInputActivateAction(List<Integer> integers)
	{
		super(integers);
	}
	
	public FurtherInputActivateAction(HashMap<ActionFieldName, Integer> fields)
	{
		super(fields);
	}
	
	public FurtherInputActivateAction(int columnPos, UnitType unitType, ColumnSearchCondition columnSearchCondition, int furtherInput)
	{
		super(columnPos, unitType, columnSearchCondition);
		this.setFurtherInput(furtherInput);
	}
	
	public void setFurtherInput(int value)
	{
		this.actionFields.put(ActionFieldName.FURTHERINPUT, value);
	}
	
	public int getFurtherInput()
	{
		return this.actionFields.get(ActionFieldName.FURTHERINPUT);
	}
	
	public String toString()
	{
		return"		Further Input Activate Action: " + this.getUnitType().toString() + ", " + this.getColumnPos() + ", " + 
				this.getColumnSearchCondition().name() + ", " + this.getFurtherInput();
	}
	
	public int attemptAction(GameGrid gameGrid, boolean isPlayer1, int column)
	{
		int columnPos = this.getColumnPos();
		if (columnPos == Main.NO_SPECIFIC_COLUMN)
			columnPos = column;
		int result = gameGrid.activateCplayerUnit(isPlayer1, this.getUnitType(), column, 
				this.getColumnSearchCondition(), this.getFurtherInput());
		if (result == Main.GENERIC_CHECK_FAILURE && columnPos == Main.NO_SPECIFIC_COLUMN)
			return CPlayer.TRY_NEW_COLUMN;
		else
			return result;
	}
}
