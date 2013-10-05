package animation;

public abstract class PaintFrame extends Frame
{
	protected int milliseconds;
	
	public int getMilliseconds() {
		return milliseconds;
	}

	public PaintFrame(int milliseconds)
	{
		super();
		this.milliseconds = milliseconds;
	}
	
	public abstract void playFrame(int x, int y);
}
