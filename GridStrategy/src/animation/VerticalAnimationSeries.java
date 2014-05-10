package animation;

import java.util.ArrayList;

import panes.Cell;
import panes.GridPane;

public class VerticalAnimationSeries extends AnimationSeries
{
	VerticalAnimationSeries(Animation animation)
	{
		super(animation);
	}

	@Override
	public void playAnimations(boolean player1Direction, int xPos, int yPos, int xPos2, int yPos2)
	{
		GridPane gridPane = Animator.getGridPane();
		if (player1Direction)
			for(int i = yPos; i > yPos2; i--)
			{
				Cell cell1 = gridPane.getCell(xPos, i);
				Cell cell2 = gridPane.getCell(xPos2, i - 1);
				animation.playTwoCellAnimation(cell1, cell2);
			}
		else
			for(int i = yPos; i < yPos2; i++)
			{
				Cell cell1 = gridPane.getCell(xPos, i);
				Cell cell2 = gridPane.getCell(xPos2, i + 1);
				animation.playTwoCellAnimation(cell1, cell2);
			}	
	}
}
