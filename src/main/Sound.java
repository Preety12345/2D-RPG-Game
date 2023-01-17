package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	Clip clip; //WE USE THIS TO ACCESS AUDIO FILES
	URL soundURL[]= new URL[30]; //WE USE THIS URL TO STORE THE AUDIO PATH
	
	public Sound() {
		soundURL[0]=getClass().getResource("/sound/BlueBoyAdventure.wav");
		soundURL[1]=getClass().getResource("/sound/coin.wav");
		soundURL[2]=getClass().getResource("/sound/powerup.wav");
		soundURL[3]=getClass().getResource("/sound/unlock.wav");
		soundURL[4]=getClass().getResource("/sound/fanfare.wav");
		soundURL[5]=getClass().getResource("/sound/hitmonster.wav");
		soundURL[6]=getClass().getResource("/sound/receivedamage.wav");
		soundURL[7]=getClass().getResource("/sound/swingweapon.wav");
		soundURL[8]=getClass().getResource("/sound/cursor.wav");
	}
	public void setFile(int i) {
		try {
			AudioInputStream ais=AudioSystem.getAudioInputStream(soundURL[i]); //WE GET THE INDEX OF URL FROM INT I
			clip=AudioSystem.getClip();
			clip.open(ais);
			
		}catch(Exception e) {
			
		}
	}
	public void play() { //WHENEVER WE WANT TO PLAY A SOUND, WE PLAY THIS METHOD
		
		clip.start();
	}
	public void loop() {
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}
	public void stop() { //WE CALL THIS METHOD TO STOP THE SOUND
		clip.stop();
	}
}
