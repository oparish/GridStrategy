package events;

import data.Unit;

public class TwoUnitEvent extends TwoPositionEvent
{	
	private final Unit unit2;

	public Unit getUnit2() {
		return unit2;
	}

	public TwoUnitEvent(Object source, EventType type, int xPos, int yPos, 
			Unit unit1,int xPos2, int yPos2, Unit unit2)
	{
		super(source, type, xPos, yPos, unit1, xPos2, yPos2);
		this.unit2 = unit2;
	}
}
