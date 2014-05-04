package ai.headers;

import ai.Action;

public class RuleHeader
{
	private ConditionHeader conditionHeader;
	public ConditionHeader getConditionHeader() {
		return conditionHeader;
	}

	private ActionHeader actionHeader;
	
	public ActionHeader getActionHeader() {
		return actionHeader;
	}

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
