package main;

import static data.UnitType.TEST_UNIT;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ai.CPlayer;
import ai.Spawner;

import data.Unit;

import screens.GameScreen;

public class Main
{
	public final static int PLAYER1_MAXHP = 10;
	public final static int PLAYER2_MAXHP = 10;
	public final static int GRIDWIDTH = 10;
	public final static int GRIDHEIGHT = 10;
	public final static int MOVESPERTURN = 1;
	public final static boolean PLAYER1STARTS = true;

	public static final String IMAGES = "Images\\";
	public static final String FILEEXTENSION = ".png";
	
	public final static int CELLWIDTH = 40;
	public final static int CELLHEIGHT = 40;
	
	private int screenWidth;
	private int screenHeight;
	private static CPlayer testPlayer;

	public static CPlayer getTestPlayer() {
		return Main.testPlayer;
	}

	private static GameScreen gameScreen;

	private static Main main;

	public static Main getMain() {
		return main;
	}

	Main()
	{
		Main.testPlayer = Spawner.createCPlayer(false);
		CPlayer.showCPlayer(Main.testPlayer);
		this.setupGameScreen();
	}
	
	private void setupGameScreen()
	{
		Main.gameScreen = new GameScreen();
		gameScreen.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		gameScreen.setSize(this.screenWidth, this.screenHeight);
		gameScreen.setVisible(true);
	}
	
	public void switchToMainMenu()
	{
		
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public static void main(String args[])
	{
		Main.main = new Main();
	}
	
	public static GridBagConstraints getAnchoredConstraints(int xpos, int ypos)
	{
		GridBagConstraints anchoredConstraints = Main.getBaseConstraints(xpos, ypos);
		anchoredConstraints.anchor = GridBagConstraints.NORTH;
		return anchoredConstraints;
	}
	
	public static GridBagConstraints getFillConstraints(int xpos, int ypos, 
			int gridWidth, int gridHeight)
	{
		GridBagConstraints fillConstraints = Main.getBaseConstraints(xpos, ypos);
		fillConstraints.gridwidth = gridWidth;
		fillConstraints.gridheight = gridHeight;
		fillConstraints.fill = GridBagConstraints.BOTH;
		fillConstraints.anchor = GridBagConstraints.CENTER;
		return fillConstraints;
	}
	
	public static GridBagConstraints getBaseConstraints(int xpos, int ypos)
	{
		GridBagConstraints baseConstraints = new GridBagConstraints();
		baseConstraints.gridx = xpos;
		baseConstraints.gridy = ypos;
		baseConstraints.weightx = 1;
		baseConstraints.weighty = 1;
		return baseConstraints;
	}
	
	public static BufferedImage loadImage(String filename) throws IOException
	{
		File testG = new File(IMAGES + filename + FILEEXTENSION);
		return ImageIO.read(testG);
	}
	
	private static void test1(Main main)
	{
		Unit testUnit = new Unit(true, TEST_UNIT);
		main.gameScreen.getGridPane().setCellContent(4, 4, testUnit);
		main.gameScreen.getGridPane().setCellContent(1, 5, testUnit);
		main.gameScreen.getGridPane().repaint();
	}
}
