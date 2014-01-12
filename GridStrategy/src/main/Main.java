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

import data.GameGrid;
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
	private GameGrid gameGrid;

	private static GameScreen gameScreen;

	private static Main main;

	public static Main getMain() {
		if (Main.main != null)
			return main;
		else
		{
			Main.main = new Main();
			return Main.main;
		}
	}
	
	Main()
	{

	}
	
	public void startGameGridWithScreen(CPlayer cPlayer1, CPlayer cPlayer2)
	{
		this.gameGrid = new GameGrid(cPlayer1, cPlayer2);			
		this.setupGameScreen();
		this.gameGrid.startGame(Main.gameScreen);
	}
	
	public boolean startGameGridWithoutScreen(CPlayer cPlayer1, CPlayer cPlayer2)
	{
		this.gameGrid = new GameGrid(cPlayer1, cPlayer2);
		return this.gameGrid.startGameWithoutScreen();
	}
	
	private void setupGameScreen()
	{
		Main.gameScreen = new GameScreen(this.gameGrid);
		gameScreen.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = screenSize.width;
		this.screenHeight = screenSize.height;
		gameScreen.setSize(this.screenWidth, this.screenHeight);
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
	
	public static void main(String args[]) throws IOException
	{
		//Main.testPlayer = Spawner.createCPlayer(false);
		//CPlayer.showCPlayer(Main.testPlayer);
		//CPlayer readPlayer = 
		//new CPlayer(false, FileOperations.loadFile("Test.ai"));
		Main main = Main.getMain();
//		while (Main.currentOpponent == null)
//		{
			CPlayer testPlayer = TestPlayers.unitsOnBoardTestPlayer();
			main.startGameGridWithScreen(null, testPlayer);

//		}
//		Main.main.startGameGrid(null, Main.getCurrentOpponent());
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
