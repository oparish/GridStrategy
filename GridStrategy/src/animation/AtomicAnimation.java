package animation;

import java.util.ArrayList;

import panes.Cell;
import panes.PaintArea;

public class AtomicAnimation extends Animation
{
	private ArrayList<FrameWithContext> frames;
	
	public AtomicAnimation(ArrayList<FrameWithContext> frames)
	{
		this.frames = frames;
	}
	
	public void playAnimation(PaintArea cell)
	{
		Animator.startAnimation(this, cell);
	}
	
	public void playTwoCellAnimation(PaintArea cell1, PaintArea cell2)
	{
		Animator.startAnimation(this, cell1, cell2);
	}
	
	public void nextFrame(PaintArea cell, int counter)
	{
		this.frames.get(counter).getFrame().playFrame(cell);
		if (counter == this.frames.size() - 1)
			Animator.endAnimation();
	}
	
	public int getFramePause(int counter)
	{
		return this.frames.get(counter).getFrame().getTicks();
	}
	
	public void nextFrame(PaintArea cell1, PaintArea cell2, int counter)
	{

		FrameWithContext frameWithContext = this.frames.get(counter);
		if (frameWithContext.isFirstPositionFrame())
			frameWithContext.getFrame().playFrame(cell1);
		else
			frameWithContext.getFrame().playFrame(cell2);
		
		if (counter == this.frames.size() - 1)
			Animator.endAnimation();			
			
	}
}
