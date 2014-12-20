package main;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import ai.CPlayer;

public class FileOperations {

	private static String lastCPlayer;
	
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
	
	public static void saveCPlayer(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
			FileOperations.saveFile(FileOperations.lastCPlayer, bytes);
	}
	
	public static void saveCPlayerAs(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
		JFileChooser fc = new JFileChooser(".");
		fc.showSaveDialog(component);
		String filename = fc.getSelectedFile().getName();
		FileOperations.lastCPlayer = filename;
		FileOperations.saveFile(filename, bytes);
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
	
	public static CPlayer resetCPlayer(Component component, boolean isPlayer1) throws IOException
	{
		ArrayList<Integer> integers = FileOperations.loadFile(FileOperations.lastCPlayer);
		return new CPlayer(isPlayer1, integers);
	}
	
	public static CPlayer loadCPlayer(Component component, boolean isPlayer1) throws IOException
	{
		JFileChooser fc = new JFileChooser(".");
		fc.showOpenDialog(component);
		String filename = fc.getSelectedFile().getName();
		FileOperations.lastCPlayer = filename;
		ArrayList<Integer> integers = FileOperations.loadFile(filename);
		return new CPlayer(isPlayer1, integers);
	}
	
	public static Byte intToByte(int number)
	{
		return ((Byte) ((Integer) number).byteValue());
	}
}
