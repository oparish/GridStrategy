package data;

import java.io.IOException;

import javax.sound.sampled.Clip;

import main.FileOperations;
import main.Main;

public enum Sound {
	ARTILLERY_FIRE("thud");
	
	Clip clip;
	
	Sound(String fileName)
	{
		this.clip = FileOperations.loadSound(Main.SOUNDS_PATH + fileName + Main.SOUND_FILEEXTENSION);
	}
	
	public void play()
	{
		this.clip.start();
	}
}
