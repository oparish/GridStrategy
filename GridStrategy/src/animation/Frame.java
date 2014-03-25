package animation;

public abstract class Frame
{	
protected int milliseconds;
	
	public int getMilliseconds() {
		return milliseconds;
	}

	public Frame(int milliseconds)
	{
		super();
		this.milliseconds = milliseconds;
	}
	
	protected void pause()
	{
		try 
		{
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public abstract void playFrame(int x, int y);
}
