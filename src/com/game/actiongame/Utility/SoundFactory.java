package com.game.actiongame.Utility;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundFactory {
	public Sound getSound(String filename) {
		try {
			return new Sound(filename);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;

	}
}
