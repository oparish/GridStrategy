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

	private Action action;
	
	public void setAction(Action action) {
		this.action = action;
	}

	public Rule(Condition condition, Action action)
	{
		this.condition = condition;
		this.action = action;
	}
	
	public Rule(List<Integer> integers, boolean player1, RuleHeader ruleHeader)
	{
		ConditionHeader conditionHeader = ruleHeader.getConditionHeader();
		ActionHeader actionHeader = ruleHeader.getActionHeader();
		this.condition = Condition.makeCondition(integers.subList(0, conditionHeader.getSize()), player1, conditionHeader);
		this.action = Action.makeAction(integers.subList(conditionHeader.getSize(), actionHeader.getSize() + conditionHeader.getSize()), 
				player1, ruleHeader.getActionHeader());
	}
	
	public Rule clone()
	{
		return new Rule (this.condition.clone(), this.action.clone());
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
		bytes.addAll(this.condition.toBytes());
		bytes.addAll(this.action.toBytes());
		return bytes;
	}
	
	public ArrayList<Byte> getHeaderBytes()
	{
		ArrayList<Byte> headerByteList = new ArrayList<Byte>();
		headerByteList.addAll(this.condition.getHeaderBytes());
		headerByteList.add(this.action.getHeaderByte());
		return headerByteList;
	}
}
