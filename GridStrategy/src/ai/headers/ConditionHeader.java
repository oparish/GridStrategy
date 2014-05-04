package ai.headers;

import ai.Condition;

public abstract class ConditionHeader
{
	protected Class<? extends Condition> conditionClass;
	
	public Class<? extends Condition> getConditionClass()
	{
		return this.conditionClass;
	}
	
	public abstract int getSize();
	
	public abstract int getHeaderSize();
	
	public ConditionHeader(Class<? extends Condition> conditionClass)
	{
		this.conditionClass = conditionClass;
	}
}
