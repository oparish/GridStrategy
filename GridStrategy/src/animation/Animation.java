package animation;

import panes.Cell;
import panes.PaintArea;

public abstract class Animation
{
	public abstract void playAnimation(PaintArea cell);
	
	public abstract void playTwoCellAnimation(PaintArea cell1, PaintArea cell2);
}
