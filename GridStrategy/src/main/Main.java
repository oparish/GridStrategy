package main;

import static data.UnitType.TEST_UNIT;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import data.UnitType;

import screens.GameScreen;

public class Main
{
	private GameScreen gameScreen;

	Main()
	{
		this.setupGameScreen();
	}
	
	private void setupGameScreen()
	{
		this.gameScreen = new GameScreen();
		gameScreen.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		gameScreen.setSize(screenSize.width, screenSize.height);
		gameScreen.setVisible(true);
	}
	
	public static void main(String args[])
	{
		Main main = new Main();
//		Main.test1(main);
	}
	
	public static GridBagConstraints getFillConstraints(int xpos, int ypos)
	{
		GridBagConstraints fillConstraints = Main.getBaseConstraints(xpos, ypos);
		fillConstraints.fill = GridBagConstraints.BOTH;
		return fillConstraints;
	}
	
	public static GridBagConstraints getBaseConstraints(int xpos, int ypos)
	{
		GridBagConstraints baseConstraints = new GridBagConstraints();
		baseConstraints.anchor = GridBagConstraints.CENTER;
		baseConstraints.gridx = xpos;
		baseConstraints.gridy = ypos;
		baseConstraints.weightx = 1;
		baseConstraints.weighty = 1;
		return baseConstraints;
	}
	
	private static void test1(Main main)
	{
			main.gameScreen.getGridPane().setCellContent(4, 4, TEST_UNIT);
			main.gameScreen.getGridPane().setCellContent(1, 5, TEST_UNIT);
			main.gameScreen.getGridPane().repaint();
	}
}
