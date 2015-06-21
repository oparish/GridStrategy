package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import data.GameResult;
import main.FileOperations;
import main.Main;

public class Manufacturer 
{
	private static final String INITDIALOG_MESSAGE = "Do you want to load another step player?";
	private static final String INITDIALOG_TITLE = "Load Step Player?";
	private static final int NUMBER_OF_CONTESTANTS = 8;
	private static ArrayList<CPlayer> stepPlayers = new ArrayList<CPlayer>();
	
	public static void main(String args[]) throws IOException
	{
		int result = JOptionPane.showOptionDialog(null, INITDIALOG_MESSAGE, INITDIALOG_TITLE, JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, null);
		while (result == 0)
		{
			CPlayer stepPlayer = FileOperations.loadCPlayer(null, false);
			if (stepPlayer != null)
				stepPlayers.add(stepPlayer);
			result = JOptionPane.showOptionDialog(null, INITDIALOG_MESSAGE, INITDIALOG_TITLE, JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, null);
		}
			
		CPlayer[] initialCplayers = Manufacturer.getInitialCPlayers();
//		CPlayer winner = Manufacturer.pitPlayers(initialCplayers);
		CPlayer winner = initialCplayers[0];
		
		FileOperations.saveFile("Test.ai", winner.toBytes());
		System.out.println(winner);
		System.out.println("Finish");
		System.exit(0);
	}
	
	private static CPlayer pitPlayers(CPlayer[] initialCplayers)
	{
		HashMap<CPlayer, Integer> cPlayerMap = new HashMap<CPlayer, Integer>();
		for (CPlayer cPlayer : initialCplayers)
		{
			cPlayerMap.put(cPlayer, 0);
		}
		GameResult result;
		
		for (int i = 0; i < (initialCplayers.length - 1); i++)
		{
			System.out.println("i = " + i);
			for (int j = (i + 1); j < initialCplayers.length; j++)
			{
				System.out.println("j = " + j);
				CPlayer player1 = initialCplayers[i];
				CPlayer player2 = initialCplayers[j];
				result = Main.getMain().startGameGridWithoutScreen(player1, player2);
				if (result == GameResult.PLAYER1_WINS)
					cPlayerMap.put(player1, (cPlayerMap.get(player1) + 1));
				else if (result == GameResult.PLAYER2_WINS)
					cPlayerMap.put(player2, (cPlayerMap.get(player2) + 1));
			}
		}

		return Manufacturer.findWinner(cPlayerMap);
	}
	
	private static CPlayer findWinner(HashMap<CPlayer, Integer> cPlayerMap)
	{
		Entry<CPlayer, Integer> winningEntry = null;
		for (Entry<CPlayer, Integer> entry : cPlayerMap.entrySet())
		{
			if (winningEntry == null)
				winningEntry = entry;
			else if (entry.getValue() > winningEntry.getValue())
				winningEntry = entry;		
		}
		return winningEntry.getKey();
	}
	
	private static CPlayer[] getInitialCPlayers()
	{		
		CPlayer cPlayer;
		CPlayer[] players = new CPlayer[NUMBER_OF_CONTESTANTS];
		GameResult result;
		
		System.out.println(players.length);
		
//		ArrayList<Byte> bytes = cPlayer.toBytes();
//		FileOperations.saveFile("Test.ai", bytes);
		
		for (int i = 0; i < players.length; i++)
		{
			do
			{
				System.out.println("Making player");
				cPlayer = Spawner.createBatchedCPlayer(true);
				System.out.println("Player Made");
				
				result = Main.getMain().startGameGridWithoutScreen(cPlayer, null);
				
				if (result == GameResult.PLAYER1_WINS)
				{
					for (CPlayer stepPlayer : stepPlayers)
					{
						result = Main.getMain().startGameGridWithoutScreen(cPlayer, stepPlayer);
						if (result != GameResult.PLAYER1_WINS)
							break;
					}
				}				
			} while(result != GameResult.PLAYER1_WINS);
			players[i] = cPlayer;
			System.out.println("Player Found");
		}
		
		return players;
	}
}
