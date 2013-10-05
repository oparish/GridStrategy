package ai;

import java.util.ArrayList;

public class CPlayer
{
	private final ArrayList<Rule> rules;
	private final boolean isPlayer1;
	
	public CPlayer(ArrayList<Rule> rules, boolean isPlayer1)
	{
		this.rules = rules;
		this.isPlayer1 = isPlayer1;
	}
	
	public Action getAction(ObservationBatch observationBatch)
	{
		for(Rule rule : this.rules)
		{
			if (rule.getCondition().checkCondition(observationBatch))
				return rule.getAction();
		}
		return null;
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
}
