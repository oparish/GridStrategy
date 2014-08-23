package animation;

import java.util.ArrayList;

import panes.Cell;
import panes.PaintArea;

public class MultiAnimation extends Animation
{
	private ArrayList<Animation> animations;
	
	public MultiAnimation(ArrayList<Animation> animations)
	{
		this.animations = animations;
	}
	
	@Override
	public void playAnimation(PaintArea cell) 
	{
		for (Animation animation : animations)
		{
			animation.playAnimation(cell);
		}
	}

	@Override
	public void playTwoCellAnimation(PaintArea cell1, PaintArea cell2)
	{
		for (Animation animation : animations)
		{
			animation.playTwoCellAnimation(cell1, cell2);
		}	
	}

}
