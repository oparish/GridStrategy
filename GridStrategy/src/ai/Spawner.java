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
	
	private static SpecificColumnCondition createSpecificColumnCondition(HashMap<ColumnConditionParameter, Boolean> parameters, boolean isPlayer1)
	{		
		boolean useColumnCount = fillParameter(parameters, ColumnConditionParameter.USECOLUMNCOUNT);
		boolean useRowCount = fillParameter(parameters, ColumnConditionParameter.USEROWCOUNT);
		boolean useUnitType = fillParameter(parameters, ColumnConditionParameter.USEUNITTYPE);
		return Spawner.createSpecificColumnCondition(useColumnCount, useRowCount, useUnitType, isPlayer1);
	}
	
	private static SpecificColumnCondition createSpecificColumnCondition(boolean isPlayer1)
	{		
		boolean useColumnCount = randomBoolean();
		boolean useRowCount = randomBoolean();
		boolean useUnitType = randomBoolean();
		return Spawner.createSpecificColumnCondition(useColumnCount, useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean isPlayer1)
	{
		boolean useRowCount = randomBoolean();
		boolean useUnitType = randomBoolean();
		return Spawner.createColumnCondition(useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean useRowCount, boolean useUnitType, boolean isPlayer1)
	{
		int unitCount;
		
		if (useRowCount)
			unitCount = random.nextInt(2);
		else
			unitCount = random.nextInt(Main.GRIDWIDTH + 1);		
		
		ColumnCondition columnCondition = new ColumnCondition(randomConditionType(), unitCount, isPlayer1);
		if (useUnitType)
		{
			columnCondition.setUnitType(randomUnitType());
			columnCondition.setUnitPlayer(randomBoolean());
		}

		if (useRowCount)
			columnCondition.setRow(randomRow());
		return columnCondition;
	}
	
	private static SpecificColumnCondition createSpecificColumnCondition(boolean useColumnCount, boolean useRowCount, boolean useUnitType, 
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
		
		SpecificColumnCondition columnCondition = new SpecificColumnCondition(
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
	
	private static FurtherInputActivateAction[] createFurtherInputActivateActionBatch(UnitType unitType)
	{
		FurtherInputActivateAction[] actions = new FurtherInputActivateAction[Main.GRIDWIDTH];
		ColumnSearchCondition columnSearchCondition = Spawner.randomColumnSearchCondition();
		int furtherInput = Spawner.randomFurtherInput(2);
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new FurtherInputActivateAction(i, unitType, columnSearchCondition, furtherInput);
		}
		return actions;
	}
	
	private static int randomFurtherInput(int range)
	{
		return random.nextInt(range);
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
		case FURTHERINPUTACTIVATE_ACTION:
			UnitType unitType1 = Spawner.randomUnitType();
			return Spawner.createFurtherInputActivateActionBatch(unitType1);
		case ACTIVATE_ACTION:
			UnitType unitType2 = Spawner.randomUnitType();
			return Spawner.createActivateActionBatch(unitType2);
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
			return new DeployAction(Main.NO_SPECIFIC_COLUMN, randomUnitType());
		case FURTHERINPUTACTIVATE_ACTION:
			return new FurtherInputActivateAction(Main.NO_SPECIFIC_COLUMN, randomActivatableUnitType(), randomColumnSearchCondition(), 
					randomFurtherInput(2));
		case ACTIVATE_ACTION:
			return new ActivateAction(Main.NO_SPECIFIC_COLUMN, randomActivatableUnitType(), randomColumnSearchCondition());
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