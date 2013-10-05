package events;

import data.Unit;

public class TwoPositionEvent extends OneUnitEvent
{
	private final int xPos2;
	public int getxPos2() {
		return xPos2;
	}

	private final int yPos2;
	
	public int getyPos2() {
		return yPos2;
	}
	
	public TwoPositionEvent(Object source, EventType type, int xPos, int yPos, 
			Unit unit1, int xPos2, int yPos2)
	{
		super(source, type, xPos, yPos, unit1);
		this.xPos2 = xPos2;
		this.yPos2 = yPos2;
	}
}
