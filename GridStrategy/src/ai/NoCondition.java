package ai;

import java.util.ArrayList;

import main.FileOperations;

public class NoCondition extends Condition
{

	public NoCondition(boolean isPlayer1)
	{
		super(isPlayer1);
	}

	@Override
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		return true;
	}

	@Override
	public String toString(int depth)
	{
		return "No Condition";
	}
}
