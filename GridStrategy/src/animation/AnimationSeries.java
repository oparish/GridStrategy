package animation;

import java.util.ArrayList;

public abstract class AnimationSeries
{
	protected Animation animation;
	
	public AnimationSeries(Animation animation)
	{
		this.animation = animation;
	}
	
	public abstract void playAnimations(boolean player1Direction, int xPos, int yPos, int xPos2, int yPos2);

}
