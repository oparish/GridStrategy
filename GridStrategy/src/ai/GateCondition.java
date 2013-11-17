package ai;

import java.util.ArrayList;

import main.FileOperations;

public class GateCondition extends Condition
{	
	private final GateType gateType;
	private final Condition condition1;
	private final Condition condition2;
	
	public GateCondition(Condition condition1, Condition condition2, 
			GateType gateType)
	{
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.gateType = gateType;
	}
	
	public GateCondition(ArrayList<Integer> integers, boolean player1)
	{
		this.gateType = GateType.values()[integers.get(Manufacturer.counter++)
		                                  .intValue()];
		this.condition1 = Condition.setupConditionFromIntegers(integers, 
				player1);
		this.condition2 = Condition.setupConditionFromIntegers(integers, 
				player1);
	}
	
	public String toString(int depth)
	{
		return "		" + depth + " - Gate Condition: " + this.gateType + "\n" + 
				this.condition1.toString(depth + 1) + "\n" + 
						this.condition2.toString(depth + 1);
	}
	
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		boolean result1 = this.condition1.checkCondition(observationBatch);
		boolean result2 = this.condition2.checkCondition(observationBatch);
		
		switch(this.gateType)
		{
		case AND:
			return (result1 && result2);
		case OR:
			return (result1 || result2);
		case NAND:
			return !(result1 && result2);
		case NOR:
			return !(result1 || result2);
		default:
			return false;
		}
	}

	@Override
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Byte conditionClass = FileOperations.intToByte(GATECONDITIONTYPE);
		Byte conditionType = FileOperations.intToByte(this.gateType.ordinal());
		bytes.add(conditionClass);
		bytes.add(conditionType);
		bytes.addAll(this.condition1.toBytes());
		bytes.addAll(this.condition2.toBytes());
		return bytes;
	}
}
