package animation;

public enum EffectPosition
{
	BOTTOM(-0.25), MIDDLE(0), TOP(0.25);
	
	private final double positionNumber; 
	
	public double getPositionNumber() {
		return positionNumber;
	}

	private EffectPosition(double positionNumber)
	{
		this.positionNumber = positionNumber;
	}
}
