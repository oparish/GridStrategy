package buttons;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import main.Main;

@SuppressWarnings("serial")
public class ColumnButton extends JButton
{
	private static final String ARROW = "Arrow";
	private static ImageIcon arrowIcon;
	private static Integer width;
	private static Integer height;
	private int xPos;
	
	public int getXPos() {
		return xPos;
	}
	
	public static void setWidth(Integer width) {
		ColumnButton.width = width;
	}
	
	public static void setHeight(Integer height) {
		ColumnButton.height = height;
	}

	static
	{
		try {
			arrowIcon = new ImageIcon(Main.loadImage(ARROW));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ColumnButton(int xpos, ActionListener actionListener)
	{
		super(ColumnButton.arrowIcon);
		this.xPos = xpos;
		this.setSize(ColumnButton.width, ColumnButton.height);
		this.addActionListener(actionListener);
	}
}
