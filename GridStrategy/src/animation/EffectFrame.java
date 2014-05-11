package animation;

import panes.Cell;
import panes.CellPanel;
import panes.Effect;
import panes.GridPane;

public class EffectFrame extends Frame
{
	private Effect effect;
	private EffectPosition effectPosition;
	
	EffectFrame(int milliseconds, Effect effect)
	{
		super(milliseconds);
		this.effect = effect;
		this.effectPosition = EffectPosition.MIDDLE;
	}
	
	EffectFrame(int milliseconds, Effect effect, EffectPosition effectPosition)
	{
		super(milliseconds);
		this.effect = effect;
		this.effectPosition = effectPosition;
	}
	
	public void playFrame(Cell cell)
	{
		GridPane gridPane = Animator.getGridPane();
		CellPanel cellPanel = gridPane.getCellPanel();
		cellPanel.paintEffect(cell, effect, this.effectPosition);
		this.pause();
		cellPanel.repaintCell(cell);
	}
}
