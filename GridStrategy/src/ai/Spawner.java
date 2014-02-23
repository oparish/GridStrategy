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
			rules.add(createCompletelyRandomRule());
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
			rules.addAll(createRuleBatch());
		}
		return new CPlayer(rules, isPlayer1);
	}
	
	private static Rule createCompletelyRandomRule()
	{
		return new Rule(createCondition(), createAction());
	}
	
	private static Condition createCondition()
	{
		if (randomBoolean())
			return createColumnCondition();
		else
			return createGateCondition();	
	}
	
	private static ColumnCondition createColumnCondition(HashMap<ColumnConditionParameter, Boolean> parameters)
	{		
		boolean useColumnCount = fillParameter(parameters, ColumnConditionParameter.USECOLUMNCOUNT);
		boolean useRowCount = fillParameter(parameters, ColumnConditionParameter.USEROWCOUNT);
		boolean useUnitType = fillParameter(parameters, ColumnConditionParameter.USEUNITTYPE);
		return Spawner.createColumnCondition(useColumnCount, useRowCount, useUnitType);
	}
	
	private static ColumnCondition createColumnCondition()
	{		
		boolean useColumnCount = randomBoolean();
		boolean useRowCount = randomBoolean();
		boolean useUnitType = randomBoolean();
		return Spawner.createColumnCondition(useColumnCount, useRowCount, useUnitType);
	}
	
	private static ColumnCondition createColumnCondition(boolean useColumnCount, boolean useRowCount, boolean useUnitType)
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
				randomConditionType(), unitCount);
		if (useUnitType)
			columnCondition.setUnit(randomUnit());
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
	
	private static ArrayList<Rule> createRuleBatch()
	{
		UnitType unitType = Spawner.randomUnitType();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		HashMap<ColumnConditionParameter, Boolean> parameters = new HashMap<ColumnConditionParameter, Boolean>();
		parameters.put(ColumnConditionParameter.USECOLUMNCOUNT, true);
		ColumnCondition[] conditions = Spawner.turnColumnConditionIntoBatch(Spawner.createColumnCondition(parameters));
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			Action action = new Action(i, unitType);
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
	
	private static GateCondition createGateCondition()
	{
		return new GateCondition(createCondition(), createCondition(),
				randomGateType());
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
		return new Action(randomColumn(), randomUnitType());
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
	
	private static Unit randomUnit()
	{
		return new Unit(randomBoolean(), randomUnitType());
	}
	
	private static UnitType randomUnitType()
	{
		UnitType[] unitTypes = UnitType.values();
		int randTypeNumber = random.nextInt(unitTypes.length);
		return unitTypes[randTypeNumber];
	}
	
	private enum ColumnConditionParameter
	{
		USECOLUMNCOUNT, USEROWCOUNT, USEUNITTYPE;
	}
}