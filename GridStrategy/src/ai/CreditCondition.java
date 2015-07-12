package ai;

import java.util.HashMap;
import java.util.List;

import main.Main;
import ai.Condition.ConditionFieldName;
import ai.headers.CreditConditionHeader;

public class CreditCondition extends Condition implements NumberCondition
{	
	public CreditCondition(boolean isPlayer1, int amount, ConditionType conditionType)
	{
		super(isPlayer1);
		this.setNumber(amount);
		this.setConditionType(conditionType);
	}
	
	public CreditCondition(List<Integer> integers, boolean isPlayer1, CreditConditionHeader conditionHeader)
	{
		super(integers, isPlayer1, conditionHeader);
	}
	
	public CreditCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}

	@Override
	protected int runCheck(ObservationBatch observationBatch, Action action)
	{
		int credits = observationBatch.getCredits();
		switch(this.getConditionType())
		{
		case GREATER_THAN:
			return credits > this.getNumber() ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		case EQUAL_TO:
			return credits == this.getNumber() ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		case SMALLER_THAN:
			return credits < this.getNumber() ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		default:
			return Main.GENERIC_CHECK_FAILURE;
		}
	}

	public Integer getNumber()
	{
		return this.conditionFields.get(ConditionFieldName.NUMBER);
	}
	
	public ConditionType getConditionType() {
		Integer value = this.conditionFields.get(ConditionFieldName.CONDITION_TYPE);
		if (value == null)
			return null;
		else
			return ConditionType.values()[value];
	}
	
	public void setNumber(Integer value)
	{
		this.conditionFields.put(ConditionFieldName.NUMBER, value);
	}
	
	public void setConditionType(ConditionType conditionType)
	{
		this.conditionFields.put(ConditionFieldName.CONDITION_TYPE, conditionType.ordinal());
	}
	
	@Override
	public String toString(int depth)
	{
		return "		" + "Credit Condition: " + this.getConditionType() + 
				", Number: " + this.getNumber();
	}

}
