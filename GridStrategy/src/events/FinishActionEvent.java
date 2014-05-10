package events;

public class FinishActionEvent extends MyEvent
{
	public FinishActionEvent(Object source)
	{
		super(source, EventType.FINISH_ACTION);
	}
}
