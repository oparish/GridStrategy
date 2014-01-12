package ai;

import java.io.IOException;
import java.util.ArrayList;

import main.FileOperations;
import main.Main;

public class Manufacturer 
{
	public static Integer counter;
	
	public static void main(String args[]) throws IOException
	{
		CPlayer cPlayer;
		CPlayer[] players = new CPlayer[8];
		
		System.out.println(players.length);
		
//		ArrayList<Byte> bytes = cPlayer.toBytes();
//		FileOperations.saveFile("Test.ai", bytes);
		
		boolean result;
		for (int i = 0; i < players.length; i++)
		{
			do
			{
				cPlayer = Spawner.createCPlayer(false);
				result = Main.getMain().startGameGridWithoutScreen(cPlayer, null);
			} while(!result);
			players[i] = cPlayer;
			System.out.println("Player Found");
		}
		
		while (players.length != 1)
		{
			CPlayer[] newPlayers = new CPlayer[(players.length / 2)];
			for (int i = 0; i < newPlayers.length; i++)
			{
				Integer a = 2 * i;
				Integer b = (2 * i) + 1;
				result = Main.getMain().startGameGridWithoutScreen(players[a], 
						players[b]);
				System.out.println(result);
				if (result)
					newPlayers[i] = players[a];
				else
					newPlayers[i] = players[b];
			}
			players = newPlayers;
			System.out.println(players.length);
		}

		FileOperations.saveFile("Test.ai", players[0].toBytes());
		System.out.println("Finish");
	}
}
