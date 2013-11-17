package main;

import java.util.ArrayList;

import data.Unit;
import data.UnitType;
import ai.Action;
import ai.CPlayer;
import ai.ColumnCondition;
import ai.Condition;
import ai.ConditionType;
import ai.GateCondition;
import ai.GateType;
import ai.Rule;

public class TestPlayers
{
	public static CPlayer unitsOnBoardTestPlayer()
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		Unit testUnit = new Unit(true, UnitType.INTERCEPTOR);
		ColumnCondition testCondition1 = 
				TestPlayers.numberOfUnitsInColumnTestCondition(9, 2, testUnit,
						ConditionType.EQUAL_TO, 2);
		ColumnCondition testCondition2 = 
				TestPlayers.numberOfUnitsInColumnTestCondition(8, 2, testUnit,
						ConditionType.EQUAL_TO, 2);
		GateCondition gateCondition = new GateCondition(testCondition1, 
				testCondition2, GateType.NOR);
		Rule testRule = new Rule(gateCondition, standardTestSuccessAction());
		rules.add(testRule);
		rules.add(TestPlayers.standardTestFailureRule());
		return new CPlayer(rules, false);
	}
	
	private static Rule standardTestFailureRule()
	{
		Action testFailureAction = new Action(0, UnitType.TEST_UNIT);
		ColumnCondition testFailureCondition = 
				new ColumnCondition(ConditionType.SMALLER_THAN, 100);
		Rule testFailureRule = new Rule(testFailureCondition, testFailureAction);
		return testFailureRule;
	}
	
	private static Action standardTestSuccessAction()
	{
		return new Action(9, UnitType.TEST_UNIT);
	}
	
	private static ColumnCondition numberOfUnitsInColumnTestCondition
		(int columnPos, int rowPos, Unit unit, ConditionType conditionType, int number)
	{
		ColumnCondition testCondition = 
				new ColumnCondition(conditionType, number);
		testCondition.setColumn(columnPos);
		testCondition.setRow(rowPos);
		testCondition.setUnit(unit);
		return testCondition;
	}
	
	
}
