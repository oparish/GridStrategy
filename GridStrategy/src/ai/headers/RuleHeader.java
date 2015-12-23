package ai.headers;

import java.util.ArrayList;

import ai.Action;

public class RuleHeader
{
	private ConditionHeader conditionHeader;
	public ConditionHeader getConditionHeader() {
		return conditionHeader;
	}

	private ArrayList<ActionHeader> actionHeaders;
	
	public ArrayList<ActionHeader> getActionHeaders() {
		return actionHeaders;
	}

	public RuleHeader(ConditionHeader conditionHeader, ArrayList<ActionHeader> actionHeaders)
	{
		this.conditionHeader = conditionHeader;
		this.actionHeaders = actionHeaders;
	}
	
	public int getSize()
	{
		int actionHeaderSize = 0;
		for (ActionHeader actionHeader : this.actionHeaders)
		{
			actionHeaderSize += actionHeader.getSize();
		}
		return this.conditionHeader.getSize() + actionHeaderSize;
	}
}
