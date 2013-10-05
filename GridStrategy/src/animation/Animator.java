package animation;

import static animation.OperationType.ADD;
import static animation.OperationType.REMOVE;

import java.awt.Graphics2D;
import java.util.ArrayList;

import data.Unit;
import panes.GridPane;

public class Animator 
{
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
	
	public static AtomicAnimation getSimpleMoveAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(unit, REMOVE);
		OperationFrame addFrame = new OperationFrame(unit, ADD);
		frames.add(new FrameWithContext(removeFrame, true));
		frames.add(new FrameWithContext(addFrame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getSimpleCombatAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame combat1Frame = new OperationFrame(unit, REMOVE);
		OperationFrame combat2Frame = new OperationFrame(unit, REMOVE);
		frames.add(new FrameWithContext(combat1Frame, true));
		frames.add(new FrameWithContext(combat2Frame, false));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getBaseAttackAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(unit, REMOVE);
		frames.add(new FrameWithContext(removeFrame, true));
		return new AtomicAnimation(frames);
	}
	
	public static AtomicAnimation getDeployAnimation(Unit unit)
	{
		ArrayList<FrameWithContext> frames = new ArrayList<FrameWithContext>();
		OperationFrame removeFrame = new OperationFrame(unit, ADD);
		frames.add(new FrameWithContext(removeFrame, true));
		return new AtomicAnimation(frames);
	}
}
