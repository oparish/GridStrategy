package ai;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import ai.headers.ActionHeader;
import ai.headers.ConditionHeader;
import ai.headers.RuleHeader;

public class Rule
{
	private Condition condition;
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	private ArrayList<Action> actions;
	
	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public Rule(Condition condition, ArrayList<Action> actions)
	{
		this.condition = condition;
		this.actions = actions;
	}
	
	public Rule(List<Integer> integers, boolean player1, RuleHeader ruleHeader)
	{
		ConditionHeader conditionHeader = ruleHeader.getConditionHeader();
		ArrayList<ActionHeader> actionHeaders = ruleHeader.getActionHeaders();
		this.actions = new ArrayList<Action>();
		this.condition = Condition.makeCondition(integers.subList(0, conditionHeader.getSize()), player1, conditionHeader);
		for (ActionHeader actionHeader : actionHeaders)
		{
			Action action = Action.makeAction(integers.subList(conditionHeader.getSize(), actionHeader.getSize() + conditionHeader.getSize()), 
					player1, actionHeader);
			this.actions.add(action);
		}
	}
	
	public Rule clone()
	{
		ArrayList<Action> actions = new ArrayList<Action>();
		for (Action action : actions)
		{
			actions.add(action.clone());
		}
		return new Rule (this.condition.clone(), actions);
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public ArrayList<Action> getActions() {
		return this.actions;
	}
	
	public String toString()
	{
		String actions = "";
		for (Action action : this.actions)
		{
			actions += (action.toString() + "\n");
		}
		return "Rule:\n" + actions + this.condition.toString(0);
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		bytes.addAll(this.condition.toBytes());
		for (Action action : this.actions)
		{
			bytes.addAll(action.toBytes());
		}
		return bytes;
	}
	
	public ArrayList<Byte> getHeaderBytes()
	{
		ArrayList<Byte> headerByteList = new ArrayList<Byte>();
		headerByteList.addAll(this.condition.getHeaderBytes());
		headerByteList.add(Integer.valueOf(headerByteList.size()).byteValue());
		for (Action action : this.actions)
		{
			headerByteList.add(action.getHeaderByte());
		}
		return headerByteList;
	}
}
