package events;

public class EventBase extends EventLocation
{
	boolean player;
	
	public boolean isPlayer1() {
		return player;
	}

	public EventBase(int column, boolean player)
	{
		super(column);
		this.player = player;
	}
}
