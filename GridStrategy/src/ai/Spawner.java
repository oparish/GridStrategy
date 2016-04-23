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
	private static final int MIN_ACTIONS = 1;
	private static final int MAX_ACTIONS = 3;
	
	public static CPlayer createCPlayer(boolean isPlayer1)
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		int range = MAX_RULES - MIN_RULES + 1;
		int ruleNum = RandomGen.randomIntPlus(range, MIN_RULES);
		for (int i = 0; i < ruleNum; i++)
		{
			rules.add(createCompletelyRandomRule(isPlayer1));
		}
		return new CPlayer(rules, isPlayer1);
	}
	
	private static Rule createCompletelyRandomRule(boolean isPlayer1)
	{
		ArrayList<Action> actions = new ArrayList<Action>();
		int range = MAX_ACTIONS - MIN_ACTIONS + 1;
		int actionNum = RandomGen.randomIntPlus(range, MIN_ACTIONS);
		for (int i = 0; i < actionNum; i++)
		{
			actions.add(createAction());
		}
		return new Rule(createCondition(isPlayer1), actions);
	}
	
	private static Condition createCondition(boolean isPlayer1)
	{
		if (RandomGen.randomBoolean())
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
		boolean useColumnCount = RandomGen.randomBoolean();
		boolean useRowCount = RandomGen.randomBoolean();
		boolean useUnitType = RandomGen.randomBoolean();
		return Spawner.createSpecificColumnCondition(useColumnCount, useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean isPlayer1)
	{
		boolean useRowCount = RandomGen.randomBoolean();
		boolean useUnitType = RandomGen.randomBoolean();
		return Spawner.createColumnCondition(useRowCount, useUnitType, isPlayer1);
	}
	
	private static ColumnCondition createColumnCondition(boolean useRowCount, boolean useUnitType, boolean isPlayer1)
	{
		int unitCount;
		
		if (useRowCount)
			unitCount = RandomGen.randomInt(2);
		else
			unitCount = RandomGen.randomInt(Main.GRIDWIDTH + 1);
		
		ColumnCondition columnCondition = new ColumnCondition(RandomGen.randomConditionType(), unitCount, isPlayer1);
		if (useUnitType)
		{
			columnCondition.setUnitType(RandomGen.randomUnitType());
			columnCondition.setUnitPlayer(RandomGen.randomBoolean());
		}

		if (useRowCount)
			columnCondition.setRow(RandomGen.randomRow());
		return columnCondition;
	}
	
	private static SpecificColumnCondition createSpecificColumnCondition(boolean useColumnCount, boolean useRowCount, boolean useUnitType, 
			boolean isPlayer1)
	{
		int unitCount;
		
		if (useColumnCount && useRowCount)
			unitCount = RandomGen.randomInt(2);
		else if (useColumnCount)
			unitCount = RandomGen.randomInt(Main.GRIDWIDTH + 1);
		else if (useRowCount)
			unitCount = RandomGen.randomInt(Main.GRIDHEIGHT + 1);
		else
			unitCount = RandomGen.randomInt(Main.GRIDWIDTH * Main.GRIDHEIGHT + 1);
		
		SpecificColumnCondition columnCondition = new SpecificColumnCondition(
				RandomGen.randomConditionType(), unitCount, RandomGen.randomColumn(), isPlayer1);
		if (useUnitType)
		{
			columnCondition.setUnitType(RandomGen.randomUnitType());
			columnCondition.setUnitPlayer(RandomGen.randomBoolean());
		}

		if (useColumnCount)
			columnCondition.setColumn(RandomGen.randomColumn());
		if (useRowCount)
			columnCondition.setRow(RandomGen.randomRow());
		return columnCondition;
	}
	
	private static boolean fillParameter(HashMap<ColumnConditionParameter, Boolean> parameters, ColumnConditionParameter parameter)
	{
		if (parameters.containsKey(parameter))
			return parameters.get(parameter);
		else
			return RandomGen.randomBoolean();
	}
	
	private static ActivateAction[] createActivateActionBatch(UnitType unitType)
	{
		ActivateAction[] actions = new ActivateAction[Main.GRIDWIDTH];
		ColumnSearchCondition columnSearchCondition = RandomGen.randomColumnSearchCondition();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new ActivateAction(i, unitType, columnSearchCondition);
		}
		return actions;
	}
	
	private static FurtherInputActivateAction[] createFurtherInputActivateActionBatch(UnitType unitType)
	{
		FurtherInputActivateAction[] actions = new FurtherInputActivateAction[Main.GRIDWIDTH];
		ColumnSearchCondition columnSearchCondition = RandomGen.randomColumnSearchCondition();
		int furtherInput = RandomGen.randomInt(2);
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new FurtherInputActivateAction(i, unitType, columnSearchCondition, furtherInput);
		}
		return actions;
	}
	
	private static DeployAction[] createDeployActionBatch()
	{
		DeployAction[] actions = new DeployAction[Main.GRIDWIDTH];
		UnitType unitType = RandomGen.randomUnitType();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			actions[i] = new DeployAction(i, unitType);
		}
		return actions;
	}
	
	
	private static Action[] createActionBatch()
	{
		ActionType actionType = RandomGen.randomActionType();
		switch (actionType)
		{
		case DEPLOY_ACTION:
			return Spawner.createDeployActionBatch();
		case FURTHERINPUTACTIVATE_ACTION:
			UnitType unitType1 = RandomGen.randomUnitType();
			return Spawner.createFurtherInputActivateActionBatch(unitType1);
		case ACTIVATE_ACTION:
			UnitType unitType2 = RandomGen.randomUnitType();
			return Spawner.createActivateActionBatch(unitType2);
		default:
			return null;
		}

	}
	
	private static GateCondition createGateCondition(boolean isPlayer1)
	{
		return new GateCondition(createCondition(isPlayer1), createCondition(isPlayer1),
				RandomGen.randomGateType(), isPlayer1);
	}
	
	private static Action createAction()
	{
		switch (RandomGen.randomActionType())
		{
		case DEPLOY_ACTION:
			return new DeployAction(Main.NO_SPECIFIC_COLUMN, RandomGen.randomUnitType());
		case CLEAR_ACTION:
			return new ClearAction(Main.NO_SPECIFIC_COLUMN);
		case FURTHERINPUTACTIVATE_ACTION:
			return new FurtherInputActivateAction(Main.NO_SPECIFIC_COLUMN, RandomGen.randomActivatableUnitType(), RandomGen.randomColumnSearchCondition(), 
					RandomGen.randomInt(2));
		case ACTIVATE_ACTION:
			return new ActivateAction(Main.NO_SPECIFIC_COLUMN, RandomGen.randomActivatableUnitType(), RandomGen.randomColumnSearchCondition());
		default:
			return null;
		}
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