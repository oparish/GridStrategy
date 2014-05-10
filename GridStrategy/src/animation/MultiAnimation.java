package animation;

import java.util.ArrayList;

import panes.Cell;

public class MultiAnimation extends Animation
{
	private ArrayList<Animation> animations;
	
	public MultiAnimation(ArrayList<Animation> animations)
	{
		this.animations = animations;
	}
	
	@Override
	public void playAnimation(Cell cell) 
	{
		for (Animation animation : animations)
		{
			animation.playAnimation(cell);
		}
	}

	@Override
	public void playTwoCellAnimation(Cell cell1, Cell cell2)
	{
		for (Animation animation : animations)
		{
			animation.playTwoCellAnimation(cell1, cell2);
		}	
	}

}
