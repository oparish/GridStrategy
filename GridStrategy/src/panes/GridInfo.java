package panes;

import main.Main;
import events.EventBase;
import events.EventCell;
import events.EventLocation;

public class GridInfo
{
	public int gridWidth;
	public int gridHeight;
	public int baseY;
	public Cell[][] cells;
	public PaintArea[] player1BaseCells;
	public PaintArea[] player2BaseCells;
	public Integer rowNumbers[];
	public Integer columnNumbers[];
	public Integer base1RowNumber;
	public Integer base2RowNumber;
	
	public PaintArea getPaintAreaFromEventLocation(EventLocation eventLocation)
	{
		if (eventLocation instanceof EventBase)
		{
			EventBase eventBase = (EventBase) eventLocation;
			if (eventBase.isPlayer1())
				return this.player1BaseCells[eventBase.getColumn()];
			else
				return this.player2BaseCells[eventBase.getColumn()];
		}
		else
		{
			EventCell eventCell = (EventCell) eventLocation;
			return this.cells[eventCell.getColumn()][eventCell.getRow()];
		}
	}
	
	public PaintArea getDeployPointPaintArea(int deployColumn, int deployRow)
	{
		if (deployRow == -1)
		{
			return this.player2BaseCells[deployColumn];
		}
		else if(deployRow == Main.GRIDHEIGHT)
		{
			return this.player1BaseCells[deployColumn];
		}
		else
		{
			return this.cells[deployColumn][deployRow];
		}
	}
}