package ai;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ai.Condition.ConditionFieldName;
import data.Unit;
import data.UnitType;
import main.Main;

public class Spawner
{
	private static final ConditionClass[] randomConditionClasses = {ConditionClass.COLUMN, ConditionClass.GATE};
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
		rules.addAll(Spawner.createDefaultRuleBatch(isPlayer1));
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
		ArrayList<Rule> rules = new ArrayList<Rule>();
		HashMap<ColumnConditionParameter, Boolean> parameters = new HashMap<ColumnConditionParameter, Boolean>();
		parameters.put(ColumnConditionParameter.USECOLUMNCOUNT, true);
		ColumnCondition[] conditions = Spawner.makeColumnConditionBatch(parameters, isPlayer1);
		Action[] actions = createActionBatch();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			rules.add(new Rule(conditions[i], actions[i]));
		}
		
		if (actions[0] instanceof DeployAction && ((DeployAction) actions[0]).getUnitType().getAbilityType() != null)
		{
			ColumnCondition[] activateConditions = 
					Spawner.makeColumnConditionBatch(parameters, isPlayer1);
			ActivateAction[] activateActions = Spawner.createActivateActionBatch(((DeployAction) actions[0]).getUnitType());
			for (int i = 0; i < Main.GRIDWIDTH; i++)
			{
				rules.add(new Rule(activateConditions[i], activateActions[i]));
			}
		}
		return rules;
	}
	
	private static ActivateAction[] createActivateActionBatch(UnitType unitType)
	{
		ActivateAction[] actions = new ActivateAction[Main.GRIDWIDTH];
		ColumnSearchCondition columnSearchCondition = Spawner.randomColumnSearchCondition();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new ActivateAction(i, unitType, columnSearchCondition);
		}
		return actions;
	}
	
	private static DeployAction[] createDeployActionBatch()
	{
		DeployAction[] actions = new DeployAction[Main.GRIDWIDTH];
		UnitType unitType = Spawner.randomUnitType();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new DeployAction(i, unitType);
		}
		return actions;
	}
	
	
	private static Action[] createActionBatch()
	{
		ActionType actionType = Spawner.randomActionType();
		switch (actionType)
		{
		case DEPLOY_ACTION:
			return Spawner.createDeployActionBatch();
		case ACTIVATE_ACTION:
			UnitType unitType = Spawner.randomUnitType();
			return Spawner.createActivateActionBatch(unitType);
		default:
			return null;
		}

	}
	
	private static ArrayList<Rule> createDefaultRuleBatch(boolean isPlayer1)
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		NoCondition noCondition = new NoCondition(isPlayer1);
		UnitType unitType = Spawner.randomUnitType();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			DeployAction deployAction = new DeployAction(i, unitType);
			rules.add(new Rule(noCondition, deployAction));
		}
		
		return rules;
	}
	
	private static ColumnCondition[] makeColumnConditionBatch(HashMap<ColumnConditionParameter, Boolean> parameters, boolean isPlayer1)
	{
		ColumnCondition[] conditionBatch = new ColumnCondition[Main.GRIDWIDTH];
		conditionBatch[0] = Spawner.createColumnCondition(parameters, isPlayer1);
		for (int i = 1; i < Main.GRIDWIDTH; i++)
		{
			conditionBatch[i] = conditionBatch[0].copyConditionWithNewColumn(i);
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
			return new ActivateAction(randomColumn(), randomActivatableUnitType(), randomColumnSearchCondition());
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
		ArrayList<UnitType> unitTypes = UnitType.getDeployableUnitTypes();
		int randTypeNumber = random.nextInt(unitTypes.size());
		return unitTypes.get(randTypeNumber);
	}
	
	private static UnitType randomActivatableUnitType()
	{
		ArrayList<UnitType> unitTypes = UnitType.getActivatableUnitTypes();
		int randTypeNumber = random.nextInt(unitTypes.size());
		return unitTypes.get(randTypeNumber);
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
	
	private enum ConditionClass
	{
		COLUMN, GATE, NO;
	}
}