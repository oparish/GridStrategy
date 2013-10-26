package ai;

import java.util.ArrayList;

public abstract class Condition
{
	protected static final int COLUMNCONDITIONTYPE = 0;
	protected static final int GATECONDITIONTYPE = 1;
	
	private Boolean result;
	
	public boolean checkCondition(ObservationBatch observationBatch)
	{
		if (this.result == null)
		{
			this.result = runCheck(observationBatch);
		}
		return this.result;
	}
	
	protected abstract boolean runCheck(ObservationBatch observationBatch);
	
	public abstract String toString(int depth);
	
	public abstract ArrayList<Byte> toBytes();
	
	public static Condition setupConditionFromIntegers(ArrayList<Integer> integers, 
			boolean player1)
	{
		Condition condition;
		if (integers.get(Manufacturer.counter++).intValue() == GATECONDITIONTYPE)
		{
			condition = new GateCondition(integers, player1);
		}
		else
		{
			condition = new ColumnCondition(integers.subList(
					Manufacturer.counter++, Manufacturer.counter+=5), player1);
		}
		return condition;
	}
}
