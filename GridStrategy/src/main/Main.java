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
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 10;
	private final static int MOVESPERTURN = 1;

	private static final String IMAGES = "Images\\";
	private static final String FILEEXTENSION = ".png";
	
	private int screenWidth;
	private int screenHeight;

	private GameScreen gameScreen;
	private static Main main;

	public static Main getMain() {
		return main;
	}

	Main()
	{
		this.setupGameScreen();
	}
	
	public static int getMovesperturn() {
		return MOVESPERTURN;
	}
	
	public static int getGridwidth() {
		return GRIDWIDTH;
	}
	
	public static int getGridheight() {
		return GRIDHEIGHT;
	}
	
	private void setupGameScreen()
	{
		this.gameScreen = new GameScreen();
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
//		Main.test1(main);
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
			main.gameScreen.getGridPane().setCellContent(4, 4, TEST_UNIT);
			main.gameScreen.getGridPane().setCellContent(1, 5, TEST_UNIT);
			main.gameScreen.getGridPane().repaint();
	}
}
