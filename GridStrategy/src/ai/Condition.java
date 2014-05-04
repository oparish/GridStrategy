package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.headers.ColumnConditionHeader;
import ai.headers.ConditionHeader;
import ai.headers.GateConditionHeader;
import main.FileOperations;
import main.Main;
import data.UnitType;

public abstract class Condition
{
	private static HashMap<Class<? extends Condition>, ConditionFieldName[]> conditionFieldNames;
	
	protected HashMap<ConditionFieldName, Integer> conditionFields = new HashMap<ConditionFieldName, Integer>();
	protected boolean isPlayer1;
	
	static
	{
		Condition.conditionFieldNames = new HashMap<Class<? extends Condition>, ConditionFieldName[]>();
		Condition.conditionFieldNames.put(GateCondition.class, new ConditionFieldName[]{ConditionFieldName.GATETYPE});
		Condition.conditionFieldNames.put(ColumnCondition.class, new ConditionFieldName[]{ConditionFieldName.ROW, ConditionFieldName.COLUMN, ConditionFieldName.UNIT_TYPE, 
			ConditionFieldName.NUMBER, ConditionFieldName.CONDITION_TYPE});
	}
	
	Condition(boolean isPlayer1)
	{
		this.isPlayer1 = isPlayer1;
	}
	
	Condition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		this(isPlayer1);
		this.conditionFields = fieldMap;
	}
	
	Condition(List<Integer> integers, boolean isPlayer1, ConditionHeader conditionHeader)
	{
		this(isPlayer1);
		int i = 0;
		for (ConditionFieldName fieldName : this.getConditionFieldNames())
		{
			Integer value = integers.get(i);
			if (value == fieldName.defaultValue)
				value = null;
			this.conditionFields.put(fieldName, value);
			i++;
		}
	}
	
	public static Condition makeCondition(List<Integer> integers, boolean isPlayer1, ConditionHeader conditionHeader)
	{
		if (conditionHeader instanceof GateConditionHeader)
		{
			return new GateCondition(integers, isPlayer1, (GateConditionHeader) conditionHeader);
		}
		else
		{
			return new ColumnCondition(integers, isPlayer1, (ColumnConditionHeader) conditionHeader);
		}
	}
	
	public static ConditionFieldName[] getConditionFieldNames(Class<? extends Condition> conditionClass)
	{
		return Condition.conditionFieldNames.get(conditionClass);
	}
	
	public static int getConditionFieldNamesLength(Class<? extends Condition> conditionClass)
	{
		return Condition.getConditionFieldNames(conditionClass).length;
	}
	
	public ConditionFieldName[] getConditionFieldNames()
	{
		return Condition.getConditionFieldNames(this.getClass());
	}
	
	public boolean checkCondition(ObservationBatch observationBatch)
	{
		boolean check = runCheck(observationBatch);
		return check;
	}
	
	public boolean getUnitPlayer()
	{
		return this.isPlayer1;
	}
	
	public void setUnitPlayer(boolean isPlayer1)
	{
		this.isPlayer1 = isPlayer1;
	}
	
	protected abstract boolean runCheck(ObservationBatch observationBatch);
	
	public abstract String toString(int depth);
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		
		for (ConditionFieldName fieldName : this.getConditionFieldNames())
		{
			Integer value = this.conditionFields.get(fieldName);
			System.out.println(fieldName.name());
			System.out.println(value);
			if (value == null)
			{
				value = fieldName.defaultValue;
			}
			bytes.add(FileOperations.intToByte(value));
		}
		
		return bytes;
	}
	
	public abstract ArrayList<Byte> getHeaderBytes();

	
	protected enum ConditionFieldName
	{
		CONDITION_TYPE(-1), NUMBER(-1), COLUMN(Main.GRIDWIDTH), ROW(Main.GRIDHEIGHT), UNIT_TYPE(UnitType.values().length), UNIT_PLAYER(2), 
		GATETYPE(GateType.values().length);
		
		public int defaultValue;
		
		ConditionFieldName(int defaultValue)
		{
			this.defaultValue = defaultValue;
		}
	}
}
