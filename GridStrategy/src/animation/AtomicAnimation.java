package animation;

import java.util.ArrayList;

public class AtomicAnimation extends Animation
{
	private ArrayList<FrameWithContext> frames;
	
	public AtomicAnimation(ArrayList<FrameWithContext> frames)
	{
		this.frames = frames;
	}
	
	public void playAnimation(int x, int y)
	{
		Animator.startAnimation(this);
		for (FrameWithContext frameWithContext : frames)
		{
			frameWithContext.getFrame().playFrame(x, y);
		}
		Animator.endAnimation();
	}
	
	public void playTwoCellAnimation(int x1, int y1, int x2, int y2)
	{
		Animator.startAnimation(this);
		for (FrameWithContext frameWithContext : frames)
		{
			if (frameWithContext.isFirstPositionFrame())
				frameWithContext.getFrame().playFrame(x1, y1);
			else
				frameWithContext.getFrame().playFrame(x2, y2);
		}
		Animator.endAnimation();
	}
}
