package animation;

import java.util.ArrayList;

public class VerticalAnimationSeries extends AnimationSeries
{
	VerticalAnimationSeries(Animation animation)
	{
		super(animation);
	}

	@Override
	public void playAnimations(boolean player1Direction, int xPos, int yPos, int xPos2, int yPos2)
	{
		if (player1Direction)
			for(int i = yPos; i > yPos2; i--)
			{
				animation.playTwoCellAnimation(xPos, i, xPos2, i - 1);
			}
		else
			for(int i = yPos; i < yPos2; i++)
			{
				animation.playTwoCellAnimation(xPos, i, xPos2, i + 1);
			}	
	}
}
