package animation;

import java.util.ArrayList;

import main.Main;
import events.EventBase;
import events.EventCell;
import events.EventLocation;
import panes.Cell;
import panes.CellPanel;
import panes.GridInfo;
import panes.GridPane;
import panes.PaintArea;

public class VerticalAnimationSeries extends AnimationSeries
{
	VerticalAnimationSeries(Animation animation)
	{
		super(animation);
	}

	@Override
	public void playAnimations(boolean player1Direction, EventLocation eventLocation1, EventLocation eventLocation2)
	{
		GridPane gridPane = Animator.getGridPane();
		GridInfo gridInfo = gridPane.getGridInfo();
		CellPanel cellPanel = gridPane.getCellPanel();
		ArrayList<PaintArea> paintAreas = new ArrayList<PaintArea>();
		
		int startRow;
		int endRow;
		int column = eventLocation1.getColumn();
		
		if (eventLocation1 instanceof EventBase)
		{
			paintAreas.add(gridInfo.getPaintAreaFromEventLocation(eventLocation1));
			if (((EventBase) eventLocation1).isPlayer1())
				startRow = Main.GRIDHEIGHT - 1;
			else
				startRow = 0;
		}
		else
		{
			startRow = ((EventCell) eventLocation1).getRow();
		}
		
		if (eventLocation2 instanceof EventBase)
		{
			if (((EventBase) eventLocation2).isPlayer1())
				endRow = Main.GRIDHEIGHT - 1;
			else
				endRow = 0;
		}
		else
		{
			endRow = ((EventCell) eventLocation2).getRow();
		}
		
		if (player1Direction)
			for (int i = startRow; i >= endRow; i--)
			{
				paintAreas.add(gridInfo.cells[column][i]);
			}
		else
			for (int i = startRow; i <= endRow; i++)
			{
				paintAreas.add(gridInfo.cells[column][i]);
			}
		
		if (eventLocation2 instanceof EventBase)
		{
			paintAreas.add(gridInfo.getPaintAreaFromEventLocation(eventLocation2));
		}
		
		for(int i = 1; i < paintAreas.size(); i++)
		{
			this.animation.playTwoCellAnimation(paintAreas.get(i - 1), paintAreas.get(i));
		}
	}
}
