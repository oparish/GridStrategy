package main;

import java.io.IOException;
import java.util.ArrayList;

import data.Unit;
import data.UnitType;
import ai.Action;
import ai.CPlayer;
import ai.ColumnCondition;
import ai.Condition;
import ai.ConditionType;
import ai.DeployAction;
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
				testCondition2, GateType.NOR, false);
		Rule testRule = new Rule(gateCondition, standardTestSuccessAction());
		rules.add(testRule);
		rules.add(TestPlayers.standardTestFailureRule());
		return new CPlayer(rules, false);
	}
	
	private static Rule standardTestFailureRule()
	{
		Action testFailureAction = new DeployAction(0, UnitType.INTERCEPTOR);
		ColumnCondition testFailureCondition = 
				new ColumnCondition(ConditionType.SMALLER_THAN, 100, false);
		Rule testFailureRule = new Rule(testFailureCondition, testFailureAction);
		return testFailureRule;
	}
	
	private static Action standardTestSuccessAction()
	{
		return new DeployAction(9, UnitType.INTERCEPTOR);
	}
	
	private static ColumnCondition numberOfUnitsInColumnTestCondition
		(int columnPos, int rowPos, Unit unit, ConditionType conditionType, int number)
	{
		ColumnCondition testCondition = 
				new ColumnCondition(conditionType, number, false);
		testCondition.setColumn(columnPos);
		testCondition.setRow(rowPos);
		testCondition.setUnitType(unit.getUnitType());
		testCondition.setUnitPlayer(unit.isOwnedByPlayer1());
		return testCondition;
	}
	
	public static void main(String args[]) throws IOException
	{
		CPlayer cPlayer = new CPlayer(false, FileOperations.loadFile("test.ai"));
		Main.getMain().startGameGridWithScreen(null, cPlayer);
//		CPlayer cPlayer = TestPlayers.unitsOnBoardTestPlayer();
//		FileOperations.saveFile("test.ai", cPlayer.toBytes());
	}
}
