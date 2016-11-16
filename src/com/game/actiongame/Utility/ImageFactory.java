package com.game.actiongame.Utility;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageFactory {
	public static final String IMAGE_DIR = "data/";
	
	public Image getImage(String filename) {
		try {
			return new Image(IMAGE_DIR + filename);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;
	}
}
