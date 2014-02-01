package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import data.GameResult;

import main.FileOperations;
import main.Main;

public class Manufacturer 
{
	private static final int NUMBER_OF_CONTESTANTS = 8;
	public static Integer counter;
	
	public static void main(String args[]) throws IOException
	{
		CPlayer[] initialCplayers = Manufacturer.getInitialCPlayers();
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
				result = Main.getMain().startGameGridWithoutScreen
						(player1, player2);
				if (result == GameResult.PLAYER1_WINS)
					cPlayerMap.put(player1, (cPlayerMap.get(player1) + 1));
				else if (result == GameResult.PLAYER2_WINS)
					cPlayerMap.put(player2, (cPlayerMap.get(player2) + 1));
			}
		}

		CPlayer winner = Manufacturer.findWinner(cPlayerMap);
		
		FileOperations.saveFile("Test.ai", winner.toBytes());
		System.out.println("Finish");
		System.exit(0);
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
				cPlayer = Spawner.createCPlayer(false);
				result = Main.getMain().startGameGridWithoutScreen(cPlayer, null);
			} while(result != GameResult.PLAYER1_WINS);
			players[i] = cPlayer;
			System.out.println("Player Found");
		}
		
		return players;
	}
}
