package com.dwang.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

public class SoundPlayer {

	public SoundPlayer() {
	}

	public void play() {
		try {
			AudioClip clip = Applet.newAudioClip(new URL(
					"file:///Users/derekwang/Music/phone/bingyu.wav"));
			clip.play();
		} catch (MalformedURLException murle) {
			System.out.println(murle);
		}
	}

	public static void main(String[] args) throws Exception {
		SoundPlayer player = new SoundPlayer();
		player.play();
	}

}
