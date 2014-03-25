package animation;

import panes.Effect;

public class EffectFrame extends Frame
{
	EffectFrame(int milliseconds)
	{
		super(milliseconds);
	}
	
	public void playFrame(int x, int y)
	{
		Animator.getGridPane().paintEffect(x, y, Effect.BATTLE);
		this.pause();
		Animator.getGridPane().repaintCell(x, y);
	}
}
