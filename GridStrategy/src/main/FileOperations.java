package main;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ai.CPlayer;

public class FileOperations {

	private static String lastCPlayer;
	private static String lastMap;
	private static final String AI_FILE_EXTENSION = "ai";
	private static final String MAP_FILE_EXTENSION = "map"; 
	
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
	
	public static boolean hasLastCPlayer()
	{
		return FileOperations.lastCPlayer != null;
	}
	
	public static boolean hasLastMap()
	{
		return FileOperations.lastMap != null;
	}
	
	public static void clearLastCPlayer()
	{
		FileOperations.lastCPlayer = null;
	}
	
	public static void saveCPlayer(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
			FileOperations.saveFile(FileOperations.lastCPlayer, bytes);
	}
	
	public static void saveMap(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
			FileOperations.saveFile(FileOperations.lastMap, bytes);
	}
	
	public static void saveMapAs(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
		JFileChooser fc = FileOperations.setupMapJFileChooser();
		fc.showSaveDialog(component);
		File selectedFile = fc.getSelectedFile();
		if (selectedFile == null)
			return;
		String filename = selectedFile.getName();
		if (!filename.endsWith("." + MAP_FILE_EXTENSION))
				filename = filename + "." + MAP_FILE_EXTENSION;
		FileOperations.lastMap = filename;
		FileOperations.saveFile(filename, bytes);
	}
	
	public static void saveCPlayerAs(ArrayList<Byte> bytes, Component component) 
			throws IOException
	{
		JFileChooser fc = FileOperations.setupAIJFileChooser();
		fc.showSaveDialog(component);
		File selectedFile = fc.getSelectedFile();
		if (selectedFile == null)
			return;
		String filename = selectedFile.getName();
		if (!filename.endsWith("." + AI_FILE_EXTENSION))
				filename = filename + "." + AI_FILE_EXTENSION;
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
	
	public static Clip loadSound(String fileName)
	{
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(fileName)));
			return clip;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	public static CPlayer resetCPlayer(Component component, boolean isPlayer1) throws IOException
	{
		ArrayList<Integer> integers = FileOperations.loadFile(FileOperations.lastCPlayer);
		return new CPlayer(isPlayer1, integers);
	}
	
	public static CPlayer loadCPlayer(Component component, boolean isPlayer1) throws IOException
	{
		JFileChooser fc = FileOperations.setupAIJFileChooser();
		fc.showOpenDialog(component);
		String filename = fc.getSelectedFile().getName();
		FileOperations.lastCPlayer = filename;
		ArrayList<Integer> integers = FileOperations.loadFile(filename);
		return new CPlayer(isPlayer1, integers);
	}
	
	public static ArrayList<Integer> loadMap(Component component) throws IOException
	{
		JFileChooser fc = FileOperations.setupMapJFileChooser();
		fc.showOpenDialog(component);
		String filename = fc.getSelectedFile().getName();
		FileOperations.lastMap = filename;
		return FileOperations.loadFile(filename);
	}
	
	private static JFileChooser setupAIJFileChooser()
	{
		JFileChooser fc = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("AI only", AI_FILE_EXTENSION);
		fc.setFileFilter(filter);
		return fc;
	}
	
	private static JFileChooser setupMapJFileChooser()
	{
		JFileChooser fc = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map only", MAP_FILE_EXTENSION);
		fc.setFileFilter(filter);
		return fc;
	}
	
	public static Byte intToByte(int number)
	{
		return ((Byte) ((Integer) number).byteValue());
	}
}
