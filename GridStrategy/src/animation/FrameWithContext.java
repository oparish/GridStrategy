package animation;

public class FrameWithContext
{
	private final Frame frame;
	public Frame getFrame() {
		return frame;
	}

	public boolean isFirstPositionFrame() {
		return firstPositionFrame;
	}

	private final boolean firstPositionFrame;
	
	public FrameWithContext(Frame frame, boolean firstPositionFrame)
	{
		this.frame = frame;
		this.firstPositionFrame = firstPositionFrame;
	}
}
