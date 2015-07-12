package ai.headers;

import ai.Condition;

public class SpecificColumnConditionHeader extends ColumnConditionHeader
{
	public SpecificColumnConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}
}
