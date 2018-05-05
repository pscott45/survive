import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public abstract class Audio {
	
	private static Clip background;
	
	public static void playSound(String file)
	{
		try{
			File sound = new File(file);
			AudioInputStream input = AudioSystem.getAudioInputStream(sound);
			AudioFormat format = input.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(input);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			if (file != "grenadeAudio.wav" && file != "backgroundAudio.wav")
				gainControl.setValue(-25);
			if (file == "flamethrowerAudio.wav")
				gainControl.setValue(-15);
			clip.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static void playBackground()
	{
		try{
			File sound = new File("backgroundAudio.wav");
			AudioInputStream input = AudioSystem.getAudioInputStream(sound);
			AudioFormat format = input.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			background = (Clip) AudioSystem.getLine(info);
			background.open(input);
			background.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static void stopBackground()
	{
		background.stop();
	}
	
}
