package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import panes.CellPanel;
import panes.GridPane;
import ai.CPlayer;
import ai.Spawner;
import data.GameGrid;
import data.GameResult;
import data.Unit;
import data.UnitType;
import events.EventType;
import screens.GameScreen;

public class Main
{
	public final static boolean DEBUG = false;
	private final static int[] DEBUG_COLUMNS = {0};
	private final static EventType[] DEBUG_EVENTTYPES = {EventType.COMBAT};
	
	public final static int PLAYER1_DEFAULTHP = 10;
	public final static int PLAYER2_DEFAULTHP = 10;
	public final static int PLAYER1_DEFAULTCREDITS = 10;
	public final static int PLAYER2_DEFAULTCREDITS = 10;
	public final static int CREDITSPERTURN = 1;
	public final static int GRIDWIDTH = 5;
	public final static int GRIDHEIGHT = 15;
	public final static int MOVESPERTURN = 1;
	public final static int FIRSTATTACKSTALEMATE = 60;
	public final static int SUBATTACKSTALEMATE = 40;	
	public final static boolean PLAYER1STARTS = true;
	
	public final static int GENERIC_CHECK_SUCCESS = 0;
	public final static int GENERIC_CHECK_FAILURE = -1;
	public final static int NO_SPECIFIC_COLUMN = 255;

	public static final String SOUNDS_PATH = "Sounds\\";
	public static final String SOUND_FILEEXTENSION = ".wav";
	public static final String IMAGES_PATH = "Images\\";
	public static final String IMAGE_FILEEXTENSION = ".png";
	
	public final static int CELLWIDTH = 40;
	public final static int CELLHEIGHT = 40;
	
	private int screenWidth;
	private int screenHeight;
	private GameGrid gameGrid;

	private static GameScreen gameScreen;

	private static Main main;
	private static Random random;

	public static Main getMain()
	{
		Main.random = new Random();
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
	
	public static boolean checkDebugColumns(int column)
	{
		for (int i : Main.DEBUG_COLUMNS)
		{
			if (i == column)
				return true;
		}
		return false;
	}
	
	public static boolean checkDebugEventType(EventType eventType)
	{
		for (EventType type : Main.DEBUG_EVENTTYPES)
		{
			if (type == eventType)
				return true;
		}
		return false;
	}
	
	public GameResult startGameGridWithScreen(CPlayer cPlayer1, CPlayer cPlayer2, ArrayList<Integer> mapData, boolean automated)
	{
		this.gameGrid = new GameGrid(cPlayer1, cPlayer2, mapData);			
		this.setupGameScreen();
		return this.gameGrid.startGame(Main.gameScreen, automated);
	}
	
	public GameResult startGameGridWithoutScreen(CPlayer cPlayer1, CPlayer cPlayer2, ArrayList<Integer> mapData)
	{
		this.gameGrid = new GameGrid(cPlayer1, cPlayer2, mapData);
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
		CPlayer readPlayer = FileOperations.loadCPlayer(null, false);
		ArrayList<Integer> mapData = FileOperations.loadMap(null);
		Main.debugOut(readPlayer.toString());
		Main main = Main.getMain();
//		while (Main.currentOpponent == null)
//		{
			//CPlayer testPlayer = TestPlayers.unitsOnBoardTestPlayer();
			main.startGameGridWithScreen(null, readPlayer, mapData, false);

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
	
	public static BufferedImage loadImage(String filename)
	{
		File testG = new File(IMAGES_PATH + filename + IMAGE_FILEEXTENSION);
		try
		{
			return ImageIO.read(testG);
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] getRandomColumnList()
	{
		ArrayList<Integer> oldList = new ArrayList<Integer>();
		int[] newList = new int[GRIDWIDTH];
		for (int i = 0; i < GRIDWIDTH; i++)
		{
			oldList.add(i);
		}
		for (int j = 0; j < GRIDWIDTH; j++)
		{
			int pos = random.nextInt(GRIDWIDTH - j);
			newList[j] = oldList.get(pos);
			oldList.remove(pos);
		}
		return newList;
	}
	
	private static void test1(Main main)
	{
		Unit testUnit = new Unit(true, UnitType.INTERCEPTOR);
		GridPane gridPane = main.gameScreen.getGridPane();
		CellPanel cellPanel = gridPane.getCellPanel();
		gridPane.setCellContent(cellPanel.getCell(4, 4), testUnit);
		gridPane.setCellContent(cellPanel.getCell(1, 5), testUnit);
		gridPane.repaint();
	}
	
	public static void debugOut(String message)
	{
		if (DEBUG)
			System.out.println(message);
	}
	
	public static void debugOut(boolean message)
	{
		if (DEBUG)
			System.out.println(((Boolean) message).toString());
	}
	
	public static void debugOut(int message)
	{
		if (DEBUG)
			System.out.println(((Integer) message).toString());
	}
}
