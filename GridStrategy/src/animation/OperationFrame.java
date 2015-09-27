package animation;

import panes.Cell;
import panes.GridPane;
import panes.PaintArea;
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
	
	private void addOperation(PaintArea paintArea)
	{
		if (paintArea instanceof Cell)
			Animator.getGridPane().setCellContent((Cell) paintArea, this.unit);
		else
			Animator.getGridPane().getCellPanel().paintUnitImage(paintArea, this.unit);
	}
	
	private void removeOperation(PaintArea paintArea)
	{
		if (paintArea instanceof Cell)
			Animator.getGridPane().deleteCellContent((Cell) paintArea);
		else
			Animator.getGridPane().getCellPanel().clearArea(paintArea);
	}
	
	public void playFrame(PaintArea paintArea)
	{
		GridPane gridPane = Animator.getGridPane();
		
		switch(this.operationType)
		{
		default:
		case ADD:
			addOperation(paintArea);
			break;
		case REMOVE:
			removeOperation(paintArea);
			break;
		}
	}
}
