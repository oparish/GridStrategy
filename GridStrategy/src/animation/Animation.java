package animation;

import panes.Cell;

public abstract class Animation
{
	public abstract void playAnimation(Cell cell);
	
	public abstract void playTwoCellAnimation(Cell cell1, Cell cell2);
}
