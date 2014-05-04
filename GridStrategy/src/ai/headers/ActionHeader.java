package ai.headers;

import ai.Action;
import ai.ColumnCondition;
import ai.Condition;

public class ActionHeader 
{
	public int getSize()
	{
		return Action.getActionFieldNamesLength(this.actionClass);
	}

	public Class<? extends Action> actionClass;
	
	public ActionHeader(Class<? extends Action> actionClass)
	{
		this.actionClass = actionClass;
	}
	
	public Class<? extends Action> getActionClass()
	{
		return this.actionClass;
	}
}
