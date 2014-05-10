package animation;

import java.util.ArrayList;

import panes.Cell;

public class AtomicAnimation extends Animation
{
	private ArrayList<FrameWithContext> frames;
	
	public AtomicAnimation(ArrayList<FrameWithContext> frames)
	{
		this.frames = frames;
	}
	
	public void playAnimation(Cell cell)
	{
		Animator.startAnimation(this);
		for (FrameWithContext frameWithContext : frames)
		{
			frameWithContext.getFrame().playFrame(cell);
		}
		Animator.endAnimation();
	}
	
	public void playTwoCellAnimation(Cell cell1, Cell cell2)
	{
		Animator.startAnimation(this);
		for (FrameWithContext frameWithContext : frames)
		{
			if (frameWithContext.isFirstPositionFrame())
				frameWithContext.getFrame().playFrame(cell1);
			else
				frameWithContext.getFrame().playFrame(cell2);
		}
		Animator.endAnimation();
	}
}
