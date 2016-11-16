package com.game.actiongame.Utility;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class SheetFactory {
	public static final String IMAGE_DIR = "data/";
	
	public Image getImage(String filename) {
		try {
			return new Image(IMAGE_DIR + filename);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public SpriteSheet getSheet(OyaSheetInterface type) {
		try {
			return new SpriteSheet(IMAGE_DIR + type.getFilename(), type.getWidth(), type.getHeight());
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
