package ai;

public enum ActionType
{
	DEPLOY_ACTION(DeployAction.class), CLEAR_ACTION(ClearAction.class), ACTIVATE_ACTION(ActivateAction.class), 
		FURTHERINPUTACTIVATE_ACTION(FurtherInputActivateAction.class);
	
	private Class<? extends Action> actionClass;
	
	public Class<? extends Action> getActionClass() {
		return actionClass;
	}

	ActionType(Class<? extends Action> actionClass)
	{
		this.actionClass = actionClass;
	}
	
	public static ActionType getActionType(Class<? extends Action> actionClass)
	{
		for (ActionType actionType : ActionType.values())
		{
			if (actionClass == actionType.getActionClass())
				return actionType;
		}
		return null;
	}
}
