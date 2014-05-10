package animation;

import panes.Cell;
import panes.Effect;
import panes.GridPane;

public class EffectFrame extends Frame
{
	EffectFrame(int milliseconds)
	{
		super(milliseconds);
	}
	
	public void playFrame(Cell cell)
	{
		GridPane gridPane = Animator.getGridPane();
		gridPane.paintEffect(cell, Effect.BATTLE);
		this.pause();
		gridPane.repaintCell(cell);
	}
}
