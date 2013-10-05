package events;

import java.util.EventObject;

public class MyEvent extends EventObject
{
	private final EventType type;
	
	public EventType getType() {
		return type;
	}

	public MyEvent(Object source, EventType type)
	{
		super(source);
		this.type = type;
	}
}
