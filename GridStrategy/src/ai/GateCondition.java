package ai;

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
}
