package animation;

import java.util.TimerTask;

import panes.CellPanel;

public class EffectTask extends TimerTask
{
	private CellPanel cellPanel;
	private int x;
	private int y;
	
	public EffectTask(CellPanel cellPanel, int x, int y)
	{
		super();
		this.cellPanel = cellPanel;
		this.x = x;
		this.y = y;
	}
	
	public void run()
	{
		this.cellPanel.repaintCell(this.cellPanel.getCell(this.x, this.y));
	}
}
