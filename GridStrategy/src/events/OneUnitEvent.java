package events;

import static events.EventType.DEPLOYING_UNIT;
import data.Unit;

public class OneUnitEvent extends MyEvent
{
	private final Unit unit;
	private final int xPos1;
	private final int yPos1;
	
	public int getyPos1() {
		return yPos1;
	}

	public Unit getUnit() {
		return unit;
	}
	
	public int getXpos1() {
		return xPos1;
	}

	public OneUnitEvent(Object source, EventType type, int xPos1, 
			int yPos1, Unit unit)
	{
		super(source, type);
		this.unit = unit;
		this.xPos1 = xPos1;
		this.yPos1 = yPos1;
	}
}
