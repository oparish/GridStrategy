package ai.headers;

import ai.Condition;

public class NoConditionHeader extends ConditionHeader
{

	public NoConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}

	@Override
	public int getSize()
	{
		return 0;
	}

	@Override
	public int getHeaderSize()
	{
		return 1;
	}

}
