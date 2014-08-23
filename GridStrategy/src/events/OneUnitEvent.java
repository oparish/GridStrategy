package events;

import static events.EventType.DEPLOYING_UNIT;
import panes.PaintArea;
import data.Unit;

public class OneUnitEvent extends MyEvent
{
	private final Unit unit;
	private final EventLocation eventLocation;

	public EventLocation getEventLocation() {
		return eventLocation;
	}

	public Unit getUnit() {
		return unit;
	}
	
	public OneUnitEvent(Object source, EventType type, EventLocation eventLocation, Unit unit)
	{
		super(source, type);
		this.unit = unit;
		this.eventLocation = eventLocation;
	}
	
	public OneUnitEvent(Object source, EventType type, int xPos, int yPos, Unit unit)
	{
		this(source, type, new EventCell(xPos, yPos), unit);
	}
}
