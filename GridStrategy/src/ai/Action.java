package ai;

import static ai.Action.ActionFieldName.COLUMNPOS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ai.Condition.ConditionFieldName;
import ai.headers.ActionHeader;
import ai.headers.ColumnConditionHeader;
import ai.headers.ConditionHeader;
import ai.headers.GateConditionHeader;
import main.FileOperations;
import data.GameGrid;
import data.UnitType;

@SuppressWarnings("unchecked")
public abstract class Action
{	
	private static HashMap<Class<? extends Action>, ActionFieldName[]> actionFieldNames;
	
	protected HashMap<ActionFieldName, Integer> actionFields = new HashMap<ActionFieldName, Integer>();

	static
	{
		Action.actionFieldNames = new HashMap<Class<? extends Action>, ActionFieldName[]>();
		Action.actionFieldNames.put(DeployAction.class, new ActionFieldName[]{ActionFieldName.COLUMNPOS, ActionFieldName.UNITTYPE});
		Action.actionFieldNames.put(ActivateAction.class, new ActionFieldName[]{ActionFieldName.COLUMNPOS, ActionFieldName.UNITTYPE, 
			ActionFieldName.ACTIVATECONDITION});
	}
	
	public Action(int columnPos)
	{
		this.setColumnPos(columnPos);
	}
	
	public Action(List<Integer> integers)
	{
		int i = 0;
		for (ActionFieldName fieldName : this.getActionFieldNames())
		{
			this.actionFields.put(fieldName, integers.get(i));
			i++;
		}
	}
	
	public static Action getActionExample(Class<? extends Action> actionClass)
	{
		if (actionClass == DeployAction.class)
		{
			return new DeployAction(0, UnitType.ARTILLERY);
		}
		else if (actionClass == ActivateAction.class)
		{
			return new ActivateAction(0, UnitType.ARTILLERY, ColumnSearchCondition.FURTHEST_FROM_START);
		}
		else
		{
			return null;
		}
	}
	
	
	public UnitType getUnitType() {
		int value = this.actionFields.get(ActionFieldName.UNITTYPE);
		return UnitType.values()[value];
	}
	
	public void setUnitType(UnitType unitType)
	{
		this.actionFields.put(ActionFieldName.UNITTYPE, unitType.ordinal());
	}
	
	public static String getActionClassName(Class<? extends Action> actionClass)
	{
		if (actionClass == DeployAction.class)
		{
			return "Deploy Action";
		}
		else if (actionClass == ActivateAction.class)
		{
			return "Activate Action";
		}
		else
		{
			return null;
		}
	}
	
	public static ActionFieldName[] getActionFieldNames(Class<? extends Action> actionClass)
	{
		return Action.actionFieldNames.get(actionClass);
	}
	
	public static int getActionFieldNamesLength(Class<? extends Action> actionClass)
	{
		return Action.getActionFieldNames(actionClass).length;
	}
	
	public static Action makeAction(List<Integer> integers, boolean isPlayer1, ActionHeader actionHeader)
	{
		Class<? extends Action> actionClass = actionHeader.getActionClass();
		if (actionClass == DeployAction.class)
		{
			return new DeployAction(integers);
		}
		else
		{
			return new ActivateAction(integers);
		}
	}
	
	public ActionFieldName[] getActionFieldNames()
	{
		return Action.getActionFieldNames(this.getClass());
	}
	
	public int getColumnPos() {
		return this.actionFields.get(COLUMNPOS);
	}
	
	public void setColumnPos(int columnPos) {
		this.actionFields.put(COLUMNPOS, columnPos);
	}
	
	public abstract String toString();
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		
		for (ActionFieldName fieldName : this.getActionFieldNames())
		{
			Integer value = this.actionFields.get(fieldName);
			if (value == null)
			{
				value = fieldName.defaultValue;
			}
			bytes.add(FileOperations.intToByte(value));
		}
		
		return bytes;
	}
	
	public byte getHeaderByte()
	{
		return FileOperations.intToByte(CPlayer.getActionClassOrdinal(this.getClass()));
	}
	
	protected enum ActionFieldName
	{
		COLUMNPOS(-1), ACTIVATECONDITION(ColumnSearchCondition.values().length), UNITTYPE(UnitType.values().length);
		
		
		public int defaultValue;
		
		ActionFieldName(int defaultValue)
		{
			this.defaultValue = defaultValue;
		}
	}
	
	public abstract boolean attemptAction(GameGrid gameGrid, boolean isPlayer1);
}
