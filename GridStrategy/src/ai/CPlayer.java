package ai;

import java.util.ArrayList;
import java.util.HashMap;

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
import ai.headers.SpecificColumnConditionHeader;

public class CPlayer
{	
	public static final int TRY_NEW_COLUMN = -2; 
	
	private static ArrayList<Class<? extends Condition>> conditionClasses;
	
	static
	{
		conditionClasses = new ArrayList<Class<? extends Condition>>();
		conditionClasses.add(SpecificColumnCondition.class);
		conditionClasses.add(ColumnCondition.class);
		conditionClasses.add(GateCondition.class);
		conditionClasses.add(CreditCondition.class);
		conditionClasses.add(NoCondition.class);
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
		return (Class<? extends Action>) ActionType.values()[value].getActionClass();
	}
	
	public static Integer getActionClassOrdinal(Class<? extends Action> actionClass)
	{
		int i = 0;
		for (ActionType actionType : ActionType.values())
		{
			if (actionClass == actionType.getActionClass())
				return i;
			i++;
		}
		return null;
	}
	
	private ArrayList<Rule> rules;
	public ArrayList<Rule> getRules() {
		return rules;
	}

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
			RuleHeader ruleHeader = new RuleHeader(conditionHeader, actionHeader);
			ruleHeaders.add(ruleHeader);
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
			counter+=condition1.getHeaderSize();
			ConditionHeader condition2 = this.makeConditionHeader(integers, counter);
			conditionHeader = new GateConditionHeader(conditionClass, condition1, condition2);
		}
		else if (conditionClass == SpecificColumnCondition.class)
		{
			conditionHeader = new SpecificColumnConditionHeader(conditionClass);
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
			Condition condition = rule.getCondition();
			int columnResult = condition.checkCondition(observationBatch, rule.getAction());
			while (columnResult != Main.GENERIC_CHECK_FAILURE)
			{
				int result = rule.getAction().attemptAction(gameGrid, this.isPlayer1, columnResult);
				Main.debugOut(this.isPlayer1);
				Main.debugOut("Result: " + result);
				if (result >= 0)
						return true;
				else if (rule.getAction().getColumnPos() == Main.NO_SPECIFIC_COLUMN && condition instanceof ColumnCondition)
					columnResult = ((ColumnCondition) condition).runCheck(observationBatch, rule.getAction(), columnResult + 1);
				else if (rule.getAction().getColumnPos() == Main.NO_SPECIFIC_COLUMN && columnResult != Main.GRIDWIDTH - 1)
					columnResult++;
				else
					break;
					
			}
		}
		return false;
	}
	
	public int getNumberOfRules()
	{
		return this.rules.size();
	}
	
	public void setRules(ArrayList<Rule> rules)
	{
		this.rules = rules;
	}
	
	public void setRules(Rule[] rules)
	{
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		for (Rule rule : rules)
		{
			ruleList.add(rule);
		}
		this.rules = ruleList;
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
