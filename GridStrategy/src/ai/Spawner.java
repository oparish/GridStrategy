package ai;

import java.util.ArrayList;
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
			rules.add(createRule());
		}
		return new CPlayer(rules, isPlayer1);
	}
	
	private static Rule createRule()
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
	
	private static ColumnCondition createColumnCondition()
	{
		boolean useColumnCount = randomBoolean();
		boolean useRowCount = randomBoolean();
		boolean useUnitType = randomBoolean();
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
}