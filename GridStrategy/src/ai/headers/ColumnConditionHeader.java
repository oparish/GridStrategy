package ai.headers;

import ai.ColumnCondition;
import ai.Condition;

public class ColumnConditionHeader extends UnitCountConditionHeader
{
	public ColumnConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}
}
