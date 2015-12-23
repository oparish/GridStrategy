package ai;

import java.util.ArrayList;

import main.FileOperations;
import main.Main;

public class NoCondition extends Condition
{

	public NoCondition(boolean isPlayer1)
	{
		super(isPlayer1);
	}

	@Override
	protected int runCheck(ObservationBatch observationBatch)
	{
		return Main.GENERIC_CHECK_SUCCESS;
	}

	@Override
	public String toString(int depth)
	{
		return "No Condition";
	}
}
