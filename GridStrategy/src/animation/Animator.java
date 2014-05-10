package animation;

import static animation.OperationType.ADD;
import static animation.OperationType.REMOVE;

import java.awt.Graphics2D;
import java.util.ArrayList;

import data.Unit;
import panes.GridPane;

public class Animator 
{
	private static final int NO_PAUSE = 0;
	private static final int SHORT_PAUSE = 333;
	private static final int MEDIUM_PAUSE = 1000;
	
	private static boolean animationRunning = false;
	private static Animation currentAnimation;
	private static Graphics2D currentGraphics;
	private static GridPane gridPane;
	
	public static GridPane getGridPane() {
		return gridPane;
	}

	public static void setGridPane(GridPane gridPane) {
		Animator.gridPane = gridPane;
	}

	public static boolean isAnimationRunning()
	{
		return Animator.animationRunning;
	}
	
	public static Animation getCurrentAnimation()
	{
		return Animator.currentAnimation;
	}
	
	public static void startAnimation(Animation animation)
	{
		while(Animator.animationRunning);
		Animator.currentAnimation = animation;
		Animator.animationRunning = true;
	}
	
	public static void endAnimation()
	{
		Animator.currentAnimation = null;
		Animator.animationRunning = false;
	}

	public static Graphics2D getCurrentGraphics() {
		return currentGraphics;
	}

	public static void setCurrentGraphics(Graphics2D currentGraphics) {
		Animator.currentGraphics = currentGraphics;
	}
	
	private static AtomicAnimation getSimpleMoveAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(NO_PAUSE, unit, REMOVE);
		OperationFrame addFrame = new OperationFrame(SHORT_PAUSE,unit, ADD);
		frames.add(new FrameWithContext(removeFrame, true));
		frames.add(new FrameWithContext(addFrame, false));
		return new AtomicAnimation(frames);
	}
	
	public static VerticalAnimationSeries getMoveAnimationSeries(Unit unit)
	{
		AtomicAnimation moveAnimation = Animator.getSimpleMoveAnimation(unit);
		return new VerticalAnimationSeries(moveAnimation);
	}
	
	public static AtomicAnimation getSimpleCombatDrawAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame1 = new EffectFrame(SHORT_PAUSE);
		EffectFrame effectFrame2 = new EffectFrame(SHORT_PAUSE);
		OperationFrame combat1Frame = new OperationFrame(NO_PAUSE, unit, REMOVE);
		OperationFrame combat2Frame = new OperationFrame(NO_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame1, true));
		frames.add(new FrameWithContext(effectFrame2, false));
		frames.add(new FrameWithContext(combat1Frame, false));
		frames.add(new FrameWithContext(combat2Frame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getSimpleCombatUnit1DestroyedAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame = new EffectFrame(SHORT_PAUSE);
		OperationFrame combat1Frame = new OperationFrame(1000, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame, true));
		frames.add(new FrameWithContext(combat1Frame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getSimpleCombatUnit2DestroyedAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame = new EffectFrame(SHORT_PAUSE);
		OperationFrame combat1Frame = new OperationFrame(1000, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame, false));
		frames.add(new FrameWithContext(combat1Frame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getBaseAttackAnimation(Unit unit)
	{	
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(NO_PAUSE, unit, REMOVE);
		OperationFrame addFrame = new OperationFrame(SHORT_PAUSE,unit, ADD);
		OperationFrame attackFrame = new OperationFrame(SHORT_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(removeFrame, true));
		frames.add(new FrameWithContext(addFrame, false));
		frames.add(new FrameWithContext(attackFrame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getDeployAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(SHORT_PAUSE, unit, ADD);
		frames.add(new FrameWithContext(removeFrame, true));
		return new AtomicAnimation(frames);
	}
}
