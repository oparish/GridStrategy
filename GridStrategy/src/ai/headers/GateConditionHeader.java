package ai.headers;

import ai.Condition;
import ai.GateCondition;

public class GateConditionHeader extends ConditionHeader
{
	public ConditionHeader condition1;
	public ConditionHeader condition2;
	
	public GateConditionHeader(Class<? extends Condition> conditionClass, ConditionHeader condition1, ConditionHeader condition2)
	{
		super(conditionClass);
		this.condition1 = condition1;
		this.condition2 = condition2;
	}
	
	public int getSize()
	{
		return 1 + this.condition1.getSize() + this.condition2.getSize();
	}
	
	
	public int getHeaderSize()
	{
		return 1 + this.condition1.getHeaderSize() + this.condition2.getHeaderSize();
	}
}
