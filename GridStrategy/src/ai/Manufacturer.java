package ai;

import java.io.IOException;
import java.util.ArrayList;

import main.FileOperations;

public class Manufacturer 
{
	public static Integer counter;
	
	public static void main(String args[]) throws IOException
	{
		CPlayer cPlayer = Spawner.createCPlayer(false);
		
		ArrayList<Byte> bytes = cPlayer.toBytes();
		FileOperations.saveFile("Test.ai", bytes);
		
		CPlayer readPlayer = new CPlayer(false, FileOperations.loadFile("Test.ai"));
		System.out.println(cPlayer.toString());
		System.out.println(readPlayer.toString());
		System.out.println("End");
	}
}
