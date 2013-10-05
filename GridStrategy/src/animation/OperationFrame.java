package animation;

import data.Unit;

public class OperationFrame extends Frame
{
	private final Unit unit;
	private final OperationType operationType;
	
	public OperationFrame(Unit unit, OperationType operationType)
	{
		this.unit = unit;
		this.operationType = operationType;
	}
	
	private void addOperation(int x, int y)
	{
		Animator.getGridPane().setCellContent(x, y, this.unit);
	}
	
	private void removeOperation(int x, int y)
	{
		Animator.getGridPane().deleteCellContent(x, y);
	}
	
	public void playFrame(int x, int y)
	{
		switch(this.operationType)
		{
		default:
		case ADD:
			addOperation(x, y);
			break;
		case REMOVE:
			removeOperation(x, y);
			break;
		}
		Animator.getGridPane().repaintCell(x, y);
	}
}
