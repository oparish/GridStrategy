package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileOperations {

	public static void saveFile(String fileName, ArrayList<Byte> bytes) 
			throws IOException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		byte[] rawBytes = new byte[bytes.size()];
		int i = 0;
		for (byte rawByte : bytes)
		{
			rawBytes[i] = rawByte;
			i++;
		}
		fileOutputStream.write(rawBytes);
		fileOutputStream.close();
	}
	
	public static ArrayList<Integer> loadFile(String fileName) 
			throws IOException
	{
		ArrayList<Integer> integers = new ArrayList<Integer>();
		FileInputStream fileInputStream = new FileInputStream(fileName);
		Integer readByte = fileInputStream.read();
		while(readByte != -1)
		{
			integers.add(readByte);
			readByte = fileInputStream.read();
		}
		fileInputStream.close();
		return integers;
	}
	
	public static Byte intToByte(int number)
	{
		return ((Byte) ((Integer) number).byteValue());
	}
}
