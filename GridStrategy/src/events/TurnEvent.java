package events;

public class TurnEvent extends MyEvent
{
	private final boolean isPlayer1;
	
	public boolean isPlayer1() {
		return isPlayer1;
	}

	public TurnEvent(Object source, EventType type, boolean isPlayer1)
	{
		super(source, type);
		this.isPlayer1 = isPlayer1;
	}
}
