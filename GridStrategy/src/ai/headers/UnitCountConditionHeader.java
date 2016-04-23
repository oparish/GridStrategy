package ai.headers;

import ai.Condition;

public class UnitCountConditionHeader extends ConditionHeader
{
	public int getSize()
	{
		return Condition.getConditionFieldNamesLength(this.conditionClass);
	}
	
	public UnitCountConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}
	
	public int getHeaderSize()
	{
		return 1;
	}
}
