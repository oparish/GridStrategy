package ai;

import java.util.ArrayList;

public class Rule
{
	private final Condition condition;
	private final Action action;
	
	public Rule(Condition condition, Action action)
	{
		this.condition = condition;
		this.action = action;
	}
	
	public Rule(ArrayList<Integer> integers, boolean player1)
	{
		this.action = new Action(integers);
		Manufacturer.counter+=2;
		this.condition = Condition.setupConditionFromIntegers(integers, 
				player1);
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
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		bytes.addAll(this.action.toBytes());
		bytes.addAll(this.condition.toBytes());
		return bytes;
	}
}
