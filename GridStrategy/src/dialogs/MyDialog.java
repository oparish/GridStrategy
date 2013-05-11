package dialogs;

import java.awt.Frame;

import javax.swing.JDialog;

import main.Main;

@SuppressWarnings("serial")
public class MyDialog extends JDialog
{
	private static final int WIDTH = 500;
	private static final int HEIGHT = 300;
	
	public MyDialog(Frame frame)
	{
		super(frame, true);
		this.setSize(WIDTH, HEIGHT);
		this.centreDialog();
	}
	
	private void centreDialog()
	{
		Main main = Main.getMain();
		int x = (main.getScreenWidth() - WIDTH)/2;
		int y = (main.getScreenHeight() - HEIGHT)/2;
		this.setLocation(x, y);
	}
}
