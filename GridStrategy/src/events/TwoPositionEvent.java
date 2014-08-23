package events;

import data.Unit;

public class TwoPositionEvent extends OneUnitEvent
{
	private final EventLocation eventLocation2;
	
	public EventLocation getEventLocation2() {
		return eventLocation2;
	}

	public TwoPositionEvent(Object source, EventType type, EventLocation eventLocation1, 
			Unit unit1, EventLocation eventLocation2)
	{
		super(source, type, eventLocation1, unit1);
		this.eventLocation2 = eventLocation2;
	}
	
	public TwoPositionEvent(Object source, EventType type, int xPos, int yPos, 
			Unit unit1, int xPos2, int yPos2)
	{
		this(source, type, new EventCell(xPos, yPos), unit1, new EventCell(xPos2, yPos2));
	}
}
