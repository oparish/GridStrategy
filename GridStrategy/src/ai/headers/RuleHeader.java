package ai.headers;

import ai.Action;

public class RuleHeader
{
	public ConditionHeader conditionHeader;
	public ActionHeader actionHeader;
	
	public RuleHeader(ConditionHeader conditionHeader, ActionHeader actionHeader)
	{
		this.conditionHeader = conditionHeader;
		this.actionHeader = actionHeader;
	}
	
	public int getSize()
	{
		return this.conditionHeader.getSize() + this.actionHeader.getSize();
	}
}
