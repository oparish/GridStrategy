package main;

import java.io.IOException;

import ai.CPlayer;

public class ReadPlayer
{
	public static void main(String args[]) throws IOException
	{
		CPlayer cPlayer = FileOperations.loadCPlayer(null, true);
		System.out.println(cPlayer.toString());
	}
}
