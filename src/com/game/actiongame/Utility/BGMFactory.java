package com.game.actiongame.Utility;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class BGMFactory {
	public Music getMusic(String filename) {
		try {
			return new Music(filename);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;
	}
}
