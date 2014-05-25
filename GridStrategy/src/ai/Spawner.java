package ai;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import data.Unit;
import data.UnitType;
import main.Main;

public class Spawner
{
	private static final int MIN_RULES = 3;
	private static final int MAX_RULES = 10;
	private static Random random;
	
	static
	{
		Spawner.random = new Random();
	}
	
	public static CPlayer createCPlayer(boolean isPlayer1)
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		int range = MAX_RULES - MIN_RULES + 1;
		int ruleNum = random.nextInt(range) + MIN_RULES;
		for (int i = 0; i < ruleNum; i++)
		{
			rules.add(createCompletelyRandomRule(isPlayer1));
		}
		return new CPlayer(rules, isPlayer1);
	}
	
	public static CPlayer createBatchedCPlayer(boolean isPlayer1)
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		int range = MAX_RULES - MIN_RULES + 1;
		int ruleNum = random.nextInt(range) + MIN_RULES;
		for (int i = 0; i < ruleNum; i++)
		{
			rules.addAll(createRuleBatch(isPlayer1));
		}
		return new CPlayer(rules, isPlayer1);
	}
	
	private static Rule createCompletelyRandomRule(boolean isPlayer1)
	{
		return new Rule(createCondition(isPlayer1), createAction());
	}
	
	private static Condition createCondition(boolean isPlayer1)
	{
		if (randomBoolean())
			return createColumnCondition(isPlayer1);
		else
			return createGateCondition(isPlayer1);	
	}
	
	private static ColumnCondition createColumnCondition(HashMap<ColumnConditionParameter, Boolean> parameters, boolean isPlayer1)
	{		
		boolean useColumnCount = fillParameter(parameters, ColumnConditionParameter.USECOLUMNCOUNT);
		boolean useRowCount = fillParameter(parameters, ColumnConditionParameter.USEROWCOUNT);
		boolean useUnitType = fillParameter(parameters, ColumnConditionParameter.USEUNITTYPE);
		return Spawner.createColumnCondition(useColumnCount, useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean isPlayer1)
	{		
		boolean useColumnCount = randomBoolean();
		boolean useRowCount = randomBoolean();
		boolean useUnitType = randomBoolean();
		return Spawner.createColumnCondition(useColumnCount, useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean useColumnCount, boolean useRowCount, boolean useUnitType, 
			boolean isPlayer1)
	{
		int unitCount;
		
		if (useColumnCount && useRowCount)
			unitCount = random.nextInt(2);
		else if (useColumnCount)
			unitCount = random.nextInt(Main.GRIDWIDTH + 1);
		else if (useRowCount)
			unitCount = random.nextInt(Main.GRIDHEIGHT + 1);
		else
			unitCount = random.nextInt(Main.GRIDWIDTH * Main.GRIDHEIGHT + 1);		
		
		ColumnCondition columnCondition = new ColumnCondition(
				randomConditionType(), unitCount, isPlayer1);
		if (useUnitType)
		{
			columnCondition.setUnitType(randomUnitType());
			columnCondition.setUnitPlayer(randomBoolean());
		}

		if (useColumnCount)
			columnCondition.setColumn(randomColumn());
		if (useRowCount)
			columnCondition.setRow(randomRow());
		return columnCondition;
	}
	
	private static boolean fillParameter(HashMap<ColumnConditionParameter, Boolean> parameters, ColumnConditionParameter parameter)
	{
		if (parameters.containsKey(parameter))
			return parameters.get(parameter);
		else
			return randomBoolean();
	}
	
	private static ArrayList<Rule> createRuleBatch(boolean isPlayer1)
	{
		UnitType unitType = Spawner.randomUnitType();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		HashMap<ColumnConditionParameter, Boolean> parameters = new HashMap<ColumnConditionParameter, Boolean>();
		parameters.put(ColumnConditionParameter.USECOLUMNCOUNT, true);
		ColumnCondition[] conditions = Spawner.turnColumnConditionIntoBatch(Spawner.createColumnCondition(parameters, isPlayer1));
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			Action action;
			switch(randomActionType())
			{
			case DEPLOY_ACTION:
				action = new DeployAction(i, unitType);
				break;
			case ACTIVATE_ACTION:
				action = new ActivateAction(i, unitType, randomColumnSearchCondition());
				break;
			default:
				action = null;
			}
			rules.add(new Rule(conditions[i], action));
		}
		return rules;
	}
	
	private static ColumnCondition[] turnColumnConditionIntoBatch(ColumnCondition originalCondition)
	{
		ColumnCondition[] conditionBatch = new ColumnCondition[Main.GRIDWIDTH];
		int originalIndex = originalCondition.getColumn();
		for (int i = 0; i < originalIndex; i++)
		{
			conditionBatch[i] = originalCondition.copyConditionWithNewColumn(i);
		}
		conditionBatch[originalIndex] = originalCondition;
		for (int i = originalIndex + 1; i < Main.GRIDWIDTH; i++)
		{
			conditionBatch[i] = originalCondition.copyConditionWithNewColumn(i);
		}
		return conditionBatch;
	}
	
	private static GateCondition createGateCondition(boolean isPlayer1)
	{
		return new GateCondition(createCondition(isPlayer1), createCondition(isPlayer1),
				randomGateType(), isPlayer1);
	}
	
	private static ConditionType randomConditionType()
	{
		ConditionType[] conditionTypes = ConditionType.values();
		int randTypeNumber = random.nextInt(conditionTypes.length);
		return conditionTypes[randTypeNumber];
	}
	
	private static GateType randomGateType()
	{
		GateType[] gateTypes = GateType.values();
		int randTypeNumber = random.nextInt(gateTypes.length);
		return gateTypes[randTypeNumber];
	}
	
	private static Action createAction()
	{
		switch (randomActionType())
		{
		case DEPLOY_ACTION:
			return new DeployAction(randomColumn(), randomUnitType());
		case ACTIVATE_ACTION:
			return new ActivateAction(randomColumn(), randomUnitType(), randomColumnSearchCondition());
		default:
			return null;
		}
	}
	
	private static ActionType randomActionType()
	{
		ActionType[] actionTypes = ActionType.values();
		int randomNumber = random.nextInt(actionTypes.length);
		return actionTypes[randomNumber];
	}
	
	private static int randomColumn()
	{
		return random.nextInt(Main.GRIDWIDTH);
	}
	
	private static int randomRow()
	{
		return random.nextInt(Main.GRIDHEIGHT);
	}
	private static boolean randomBoolean()
	{
		if (random.nextInt(2) == 0)
			return true;
		else
			return false;
	}
	
	private static UnitType randomUnitType()
	{
		UnitType[] unitTypes = UnitType.getDeployableUnitTypes();
		int randTypeNumber = random.nextInt(unitTypes.length);
		return unitTypes[randTypeNumber];
	}
	
	private static ColumnSearchCondition randomColumnSearchCondition()
	{
		ColumnSearchCondition[] columnSearchConditions = ColumnSearchCondition.values();
		int randTypeNumber = random.nextInt(columnSearchConditions.length);
		return columnSearchConditions[randTypeNumber];
	}
	
	private enum ColumnConditionParameter
	{
		USECOLUMNCOUNT, USEROWCOUNT, USEUNITTYPE;
	}
}