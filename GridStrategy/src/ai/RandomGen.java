package ai;

import java.util.ArrayList;
import java.util.Random;

import main.Main;
import data.UnitType;

public class RandomGen
{
	private static Random random;
	
	static
	{
		RandomGen.random = new Random();
	}
	
	public static int randomInt(int range)
	{
		return random.nextInt(range);
	}
	
	public static int randomIntPlus(int range, int min)
	{
		return RandomGen.randomInt(range) + min;
	}
	
	public static UnitType randomUnitType()
	{
		ArrayList<UnitType> unitTypes = UnitType.getDeployableUnitTypes();
		int randTypeNumber = RandomGen.randomInt(unitTypes.size());
		return unitTypes.get(randTypeNumber);
	}
	
	public static UnitType randomActivatableUnitType()
	{
		ArrayList<UnitType> unitTypes = UnitType.getActivatableUnitTypes();
		int randTypeNumber = RandomGen.randomInt(unitTypes.size());
		return unitTypes.get(randTypeNumber);
	}
	
	public static ColumnSearchCondition randomColumnSearchCondition()
	{
		ColumnSearchCondition[] columnSearchConditions = ColumnSearchCondition.values();
		int randTypeNumber = RandomGen.randomInt(columnSearchConditions.length);
		return columnSearchConditions[randTypeNumber];
	}
	
	public static ConditionType randomConditionType()
	{
		ConditionType[] conditionTypes = ConditionType.values();
		int randTypeNumber = RandomGen.randomInt(conditionTypes.length);
		return conditionTypes[randTypeNumber];
	}
	
	public static GateType randomGateType()
	{
		GateType[] gateTypes = GateType.values();
		int randTypeNumber = RandomGen.randomInt(gateTypes.length);
		return gateTypes[randTypeNumber];
	}
	
	public static ActionType randomActionType()
	{
		ActionType[] actionTypes = ActionType.values();
		int randomNumber = RandomGen.randomInt(actionTypes.length);
		return actionTypes[randomNumber];
	}
	
	public static int randomColumn()
	{
		return RandomGen.randomInt(Main.GRIDWIDTH);
	}
	
	public static int randomRow()
	{
		return RandomGen.randomInt(Main.GRIDHEIGHT);
	}
	
	public static boolean randomBoolean()
	{
		if (RandomGen.randomInt(2) == 0)
			return true;
		else
			return false;
	}
}
