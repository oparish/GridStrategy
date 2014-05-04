package ai.headers;

import ai.ColumnCondition;
import ai.Condition;

public class ColumnConditionHeader extends ConditionHeader
{
	public int getSize()
	{
		return Condition.getConditionFieldNamesLength(this.conditionClass);
	}
	
	public ColumnConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}
	
	public int getHeaderSize()
	{
		return 1;
	}
}
