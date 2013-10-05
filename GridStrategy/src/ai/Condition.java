package ai;

public abstract class Condition
{
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
}
