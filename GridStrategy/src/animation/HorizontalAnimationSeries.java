package animation;

import java.util.ArrayList;

import main.Main;
import panes.CellPanel;
import panes.GridInfo;
import panes.GridPane;
import panes.PaintArea;
import events.EventBase;
import events.EventCell;
import events.EventLocation;

public class HorizontalAnimationSeries extends AnimationSeries
{
	HorizontalAnimationSeries(Animation animation)
	{
		super(animation);
	}

	@Override
	public void playAnimations(boolean player1Direction, EventLocation eventLocation1, EventLocation eventLocation2)
	{
		GridPane gridPane = Animator.getGridPane();
		GridInfo gridInfo = gridPane.getGridInfo();
		ArrayList<PaintArea> paintAreas = new ArrayList<PaintArea>();
		int column1 = eventLocation1.getColumn();
		int column2 = eventLocation2.getColumn();
		int row = ((EventCell)eventLocation1).getRow();
		boolean movingRight = column2 > column1;
		int i = column1;
		
		if (movingRight)
		{
			while (i <= column2)
			{
				paintAreas.add(gridInfo.cells[i][row]);
				i++;
			}
		}
		else
		{
			while (i >= column2)
			{
				paintAreas.add(gridInfo.cells[i][row]);
				i--;
			}
		}
		
		for(int j = 1; j < paintAreas.size(); j++)
		{
			this.animation.playTwoCellAnimation(paintAreas.get(j - 1), paintAreas.get(j));
		}
	}
}
