package animation;

import java.util.ArrayList;

public class MultiAnimation extends Animation
{
	private ArrayList<Animation> animations;
	
	public MultiAnimation(ArrayList<Animation> animations)
	{
		this.animations = animations;
	}
	
	@Override
	public void playAnimation(int x, int y) 
	{
		for (Animation animation : animations)
		{
			animation.playAnimation(x, y);
		}
	}

	@Override
	public void playTwoCellAnimation(int x1, int y1, int x2, int y2)
	{
		for (Animation animation : animations)
		{
			animation.playTwoCellAnimation(x1, y1, x2, y2);
		}	
	}

}
