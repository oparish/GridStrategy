package animation;

import panes.Cell;
import panes.CellPanel;
import panes.Effect;
import panes.GridPane;
import panes.PaintArea;

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
	
	public void playFrame(PaintArea paintArea)
	{
		GridPane gridPane = Animator.getGridPane();
		CellPanel cellPanel = gridPane.getCellPanel();
		cellPanel.paintEffect(paintArea, effect, this.effectPosition);
	}
}
