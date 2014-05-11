package animation;

import panes.Cell;
import panes.GridPane;
import data.Unit;

public class OperationFrame extends Frame
{
	private final Unit unit;
	private final OperationType operationType;
	
	public OperationFrame(int millis, Unit unit, OperationType operationType)
	{
		super(millis);
		this.unit = unit;
		this.operationType = operationType;
	}
	
	private void addOperation(Cell cell)
	{
		Animator.getGridPane().setCellContent(cell, this.unit);
	}
	
	private void removeOperation(Cell cell)
	{
		Animator.getGridPane().deleteCellContent(cell);
	}
	
	public void playFrame(Cell cell)
	{
		GridPane gridPane = Animator.getGridPane();
		switch(this.operationType)
		{
		default:
		case ADD:
			addOperation(cell);
			break;
		case REMOVE:
			removeOperation(cell);
			break;
		}
		gridPane.getCellPanel().repaintCell(cell);
		this.pause();
	}
}
