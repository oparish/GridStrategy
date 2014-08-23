package events;

public class EventCell extends EventLocation
{
	int row;
	
	public int getRow() {
		return row;
	}

	public EventCell(int column, int row)
	{
		super(column);
		this.row = row;
	}

}
