package ai.headers;

import ai.Condition;

public class CreditConditionHeader extends ConditionHeader
{
	public CreditConditionHeader(Class<? extends Condition> conditionClass)
	{
		super(conditionClass);
	}

	public int getSize()
	{
		return Condition.getConditionFieldNamesLength(this.conditionClass);
	}

	@Override
	public int getHeaderSize() {
		return 1;
	}

}
