package animation;

import static animation.OperationType.ADD;
import static animation.OperationType.REMOVE;
import static panes.Effect.BATTLE;
import static panes.Effect.PROJECTILE;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import data.Unit;
import panes.Effect;
import panes.GridPane;
import panes.PaintArea;

public class Animator 
{
	private static final int TINY_PAUSE = 1;
	private static final int SHORT_PAUSE = 2;
	private static final int MEDIUM_PAUSE = 3;
	public static final int CYCLE_TIME = 6;
	public static final int UNIT_SECONDS = 100;
	public static final int TICKER_PAUSE = 2;
	
	private static boolean animationRunning = false;
	private static Animation currentAnimation;
	private static Graphics2D currentGraphics;
	private static GridPane gridPane;
	private static ArrayList<AnimationCells> animationQueue = new ArrayList<AnimationCells>();
	private static int counter = -1;
	private static Integer framePauseNumber;
	
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
	
	public static void startAnimation(AtomicAnimation animation, PaintArea cell)
	{
		AnimationCells animationCells = new AnimationCells();
		animationCells.atomicAnimation = animation;
		animationCells.cell1 = cell;
		Animator.animationQueue.add(animationCells);
	}
	
	public static void startAnimation(AtomicAnimation animation, PaintArea cell1, PaintArea cell2)
	{
		AnimationCells animationCells = new AnimationCells();
		animationCells.atomicAnimation = animation;
		animationCells.cell1 = cell1;
		animationCells.cell2 = cell2;
		Animator.animationQueue.add(animationCells);
	}
	
	public static void playAnimation(int animationCounter)
	{
		if (Animator.animationQueue.size() == 0 || (Animator.framePauseNumber != null && animationCounter != Animator.framePauseNumber))
			return;
		
		AnimationCells animationCells = Animator.animationQueue.get(0);
		Animator.currentAnimation = animationCells.atomicAnimation;
		
		Animator.counter++;
		Animator.framePauseNumber = 
				(animationCells.atomicAnimation.getFramePause(Animator.counter) + animationCounter) % Animator.CYCLE_TIME;
		
		if (animationCells.cell2 == null)
			animationCells.atomicAnimation.nextFrame(animationCells.cell1, Animator.counter);
		else
			animationCells.atomicAnimation.nextFrame(animationCells.cell1, animationCells.cell2, Animator.counter);
	}
	
	public static void endAnimation()
	{
		Animator.counter = -1;
		Animator.animationQueue.remove(0);
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
		OperationFrame removeFrame = new OperationFrame(TINY_PAUSE, unit, REMOVE);
		OperationFrame addFrame = new OperationFrame(SHORT_PAUSE,unit, ADD);
		frames.add(new FrameWithContext(removeFrame, true));
		frames.add(new FrameWithContext(addFrame, false));
		return new AtomicAnimation(frames);
	}
	
	public static VerticalAnimationSeries getVertMoveAnimationSeries(Unit unit)
	{
		AtomicAnimation moveAnimation = Animator.getSimpleMoveAnimation(unit);
		return new VerticalAnimationSeries(moveAnimation);
	}
	
	public static HorizontalAnimationSeries getHorzMoveAnimationSeries(Unit unit)
	{
		AtomicAnimation moveAnimation = Animator.getSimpleMoveAnimation(unit);
		return new HorizontalAnimationSeries(moveAnimation);
	}
	
	public static AtomicAnimation getSimpleCombatDrawAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame1 = new EffectFrame(SHORT_PAUSE, Effect.BATTLE);
		EffectFrame effectFrame2 = new EffectFrame(SHORT_PAUSE, Effect.BATTLE);
		OperationFrame combat1Frame = new OperationFrame(TINY_PAUSE, unit, REMOVE);
		OperationFrame combat2Frame = new OperationFrame(TINY_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame1, true));
		frames.add(new FrameWithContext(effectFrame2, false));
		frames.add(new FrameWithContext(combat1Frame, false));
		frames.add(new FrameWithContext(combat2Frame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getSimpleCombatUnit1DestroyedAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame = new EffectFrame(SHORT_PAUSE, Effect.BATTLE);
		OperationFrame combat1Frame = new OperationFrame(MEDIUM_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame, true));
		frames.add(new FrameWithContext(combat1Frame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getSimpleCombatUnit2DestroyedAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame = new EffectFrame(SHORT_PAUSE, Effect.BATTLE);
		OperationFrame combat1Frame = new OperationFrame(MEDIUM_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame, false));
		frames.add(new FrameWithContext(combat1Frame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getBaseAttackAnimation(Unit unit)
	{	
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame effectFrame = new EffectFrame(SHORT_PAUSE, BATTLE);
		OperationFrame attackFrame = new OperationFrame(SHORT_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(effectFrame, false));
		frames.add(new FrameWithContext(attackFrame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getDeployAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame addFrame = new OperationFrame(SHORT_PAUSE, unit, ADD);
		frames.add(new FrameWithContext(addFrame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getClearAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(SHORT_PAUSE, unit, REMOVE);
		frames.add(new FrameWithContext(removeFrame, true));
		return new AtomicAnimation(frames);
	}
	
	public static VerticalAnimationSeries getFiringAnimationSeries(Unit unit)
	{
		AtomicAnimation fireAnimation = Animator.getSimpleFireAnimation(unit);
		return new VerticalAnimationSeries(fireAnimation);
	}
	
	private static EffectPosition getInternalCellAnimationPosition(boolean isPlayer1, boolean isFirst)
	{
		if ((isPlayer1 && isFirst) || (!isPlayer1 && !isFirst))
			return EffectPosition.BOTTOM;
		else
			return EffectPosition.TOP;
	}
	
	private static AtomicAnimation getSimpleFireAnimation(Unit unit)
	{
		boolean isPlayer1 = unit.isOwnedByPlayer1();
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		EffectFrame part1Frame = new EffectFrame(TINY_PAUSE, PROJECTILE, Animator.getInternalCellAnimationPosition(isPlayer1, false));
		EffectFrame part2Frame = new EffectFrame(TINY_PAUSE, PROJECTILE, Animator.getInternalCellAnimationPosition(isPlayer1, true));
		frames.add(new FrameWithContext(part1Frame, true));
		frames.add(new FrameWithContext(part2Frame, true));
		return new AtomicAnimation(frames);
	}
}

