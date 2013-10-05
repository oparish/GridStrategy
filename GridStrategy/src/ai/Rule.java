package ai;

public class Rule
{
	private final Condition condition;
	private final Action action;
	
	public Rule(Condition condition, Action action)
	{
		this.condition = condition;
		this.action = action;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public Action getAction() {
		return action;
	}
	
	public String toString()
	{
		return "Rule:\n" + this.action.toString() + 
				"\n" + this.condition.toString(0);
	}
}
