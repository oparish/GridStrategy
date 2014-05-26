package ai;

import java.util.List;

import ai.headers.CreditConditionHeader;

public class CreditCondition extends Condition
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

	@Override
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		int credits = observationBatch.getCredits();
		switch(this.getConditionType())
		{
		case GREATER_THAN:
			return (credits > this.getNumber());
		case EQUAL_TO:
			return (credits == this.getNumber());
		case SMALLER_THAN:
			return (credits < this.getNumber());
		default:
			return false;
		}
	}

	public int getNumber()
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
	
	public void setNumber(int value)
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
