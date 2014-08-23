package animation;

import java.util.ArrayList;

import events.EventLocation;

public abstract class AnimationSeries
{
	protected Animation animation;
	
	public AnimationSeries(Animation animation)
	{
		this.animation = animation;
	}
	
	public abstract void playAnimations(boolean player1Direction, EventLocation eventLocation1, EventLocation eventLocation2);

}
