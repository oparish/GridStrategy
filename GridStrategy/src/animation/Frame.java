package animation;

import panes.Cell;
import panes.GridPane;
import panes.PaintArea;

public abstract class Frame
{	
protected int ticks;
	
	public int getTicks() {
		return ticks;
	}

	public Frame(int ticks)
	{
		super();
		this.ticks = ticks;
	}
	
	public abstract void playFrame(PaintArea cell);

}
