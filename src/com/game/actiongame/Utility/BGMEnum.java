package com.game.actiongame.Utility;

public enum BGMEnum {
	STAGE1("BGM/stage1.wav"),
	BOSS("BGM/boss1.wav"),
	STAGE2("BGM/stage1.wav"),
	SHOP("BGM/shop.wav");
	
	private final String filename;
	
	BGMEnum(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
}
