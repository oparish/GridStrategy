package ai;

import java.util.ArrayList;

import data.GameGrid;
import main.FileOperations;
import main.Main;
import ai.headers.ActionHeader;
import ai.headers.ColumnConditionHeader;
import ai.headers.ConditionHeader;
import ai.headers.CreditConditionHeader;
import ai.headers.GateConditionHeader;
import ai.headers.NoConditionHeader;
import ai.headers.RuleHeader;

public class CPlayer
{	
	private static ArrayList<Class<? extends Condition>> conditionClasses;
	private static ArrayList<Class<? extends Action>> actionClasses;
	
	static
	{
		conditionClasses = new ArrayList<Class<? extends Condition>>();
		conditionClasses.add(ColumnCondition.class);
		conditionClasses.add(GateCondition.class);
		conditionClasses.add(CreditCondition.class);
		conditionClasses.add(NoCondition.class);
		actionClasses = new ArrayList<Class<? extends Action>>();
		actionClasses.add(DeployAction.class);
		actionClasses.add(ActivateAction.class);
	}
	
	private static Class<? extends Condition> getConditionClass(int value)
	{
		return (Class<? extends Condition>) CPlayer.conditionClasses.get(value);
	}
	
	public static Integer getConditionClassOrdinal(Class<? extends Condition> conditionClass)
	{
		for (int i = 0; i < CPlayer.conditionClasses.size(); i++)
		{
			if (conditionClass == conditionClasses.get(i))
				return i;
		}
		return null;
	}
	
	private static Class<? extends Action> getActionClass(int value)
	{
		return (Class<? extends Action>) CPlayer.actionClasses.get(value);
	}
	
	public static Integer getActionClassOrdinal(Class<? extends Action> actionClass)
	{
		for (int i = 0; i < actionClasses.size(); i++)
		{
			if (actionClass == actionClasses.get(i))
				return i;
		}
		return null;
	}
	
	private final ArrayList<Rule> rules;
	private final boolean isPlayer1;
	
	public CPlayer(ArrayList<Rule> rules, boolean isPlayer1)
	{
		this.rules = rules;
		this.isPlayer1 = isPlayer1;
	}
	
	public CPlayer(boolean isPlayer1, ArrayList<Integer> integers)
	{
		int rulesCount = integers.get(0);
		
		this.isPlayer1 = isPlayer1;
		
		Integer counter = 1;
		ArrayList<RuleHeader> ruleHeaders = new ArrayList<RuleHeader>();
		
		while(ruleHeaders.size() < rulesCount)
		{
			ConditionHeader conditionHeader = this.makeConditionHeader(integers, counter);
			counter += conditionHeader.getHeaderSize();
			ActionHeader actionHeader = this.makeActionHeader(integers, counter);
			counter++;
			ruleHeaders.add(new RuleHeader(conditionHeader, actionHeader));
		}	
		
		
		int ruleStart = counter;
		int ruleEnd;
		this.rules = new ArrayList<Rule>();
		for (RuleHeader ruleHeader : ruleHeaders)
		{
			ruleEnd = ruleStart + ruleHeader.getSize();
			this.rules.add(new Rule(integers.subList(ruleStart, ruleEnd), isPlayer1, ruleHeader));
			ruleStart = ruleEnd;
		}
	}
	
	private ConditionHeader makeConditionHeader(ArrayList<Integer> integers, Integer counter)
	{
		Class<? extends Condition> conditionClass = CPlayer.getConditionClass(integers.get(counter));
		ConditionHeader conditionHeader;
		if (conditionClass == GateCondition.class)
		{
			counter++;
			ConditionHeader condition1 = this.makeConditionHeader(integers, counter);
			counter++;
			ConditionHeader condition2 = this.makeConditionHeader(integers, counter);
			conditionHeader = new GateConditionHeader(conditionClass, condition1, condition2);
		}
		else if (conditionClass == ColumnCondition.class)
		{
			conditionHeader = new ColumnConditionHeader(conditionClass);
		}
		else if (conditionClass == CreditCondition.class)
		{
			conditionHeader = new CreditConditionHeader(conditionClass);
		}
		else
		{
			conditionHeader = new NoConditionHeader(conditionClass);
		}
		return conditionHeader;
	}
	
	private ActionHeader makeActionHeader(ArrayList<Integer> integers, Integer counter)
	{
		Class<? extends Action> actionClass = CPlayer.getActionClass(integers.get(counter));
		ActionHeader actionHeader = new ActionHeader(actionClass);
		counter++;
		return actionHeader;
	}
	
	public boolean makeMove(ObservationBatch observationBatch, GameGrid gameGrid)
	{
		Main.debugOut("Making move: " + this.isPlayer1 );
		for(Rule rule : this.rules)
		{
			if (rule.getCondition().checkCondition(observationBatch))
			{
				boolean result = rule.getAction().attemptAction(gameGrid, this.isPlayer1);
				Main.debugOut(this.isPlayer1);
				Main.debugOut("Result: " + result);
				if (result)
						return true;
			}
		}
		return false;
	}
	
	public int getNumberOfRules()
	{
		return this.rules.size();
	}
	
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CPlayer:");
		for(Rule rule : this.rules)
		{
			stringBuilder.append("\n	");
			stringBuilder.append(rule.toString());
		}
		return stringBuilder.toString();
	}
	
	public static void showCPlayer(CPlayer cPlayer)
	{
		System.out.println(cPlayer.toString());
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		bytes.add(FileOperations.intToByte(this.rules.size()));
		for (Rule rule : this.rules)
		{
			bytes.addAll(rule.getHeaderBytes());
		}
		for (Rule rule : this.rules)
		{
			bytes.addAll(rule.toBytes());
		}
		return bytes;
	}
	
	protected enum cPlayerValues
	{
		
	}
}
