package com.game.actiongame.Utility;

/**
 * 効果音を定義する列挙型
 * @author moromorocco
 *
 */
public enum SoundEnum {
	PLAYER_ATTACK("SE/playerAttack.wav"),
 PLAYER_SHOT("SE/playerShot.wav"),
 PLAYER_DAMAGE("SE/playerDanage.wav"),
 ENEMY_SHOT("SE/enemyShot.wav"),
 ENEMY_DEAD("SE/enemyDead.wav"),
BOSS_EXPLOTION("SE/explotion.wav"),
GET_ITEM("SE/getItem.wav");
	
	private final String filename;
	
	SoundEnum(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
	
//	PLAYER_ATTACK {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/playerAttack.wav");
//		}
//	}, PLAYER_SHOT {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/playerShot.wav");
//		}
//	}, PLAYER_DAMAGE {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/playerDanage.wav");
//		}
//	}, ENEMY_SHOT {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/enemyShot.wav");
//		}
//	}, ENEMY_DEAD {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/enemyDead.wav");
//		}
//	}, BOSS_EXPLOTION {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/explotion.wav");
//		}
//	}, GET_ITEM {
//		@Override
//		public Sound getSound() {
//			return getSound("SE/getItem.wav");
//		}
//	};
//
//	public abstract Sound getSound();
//
//	private static Sound getSound(String filename) {
//		try {
//			return new Sound(filename);
//		} catch (SlickException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
}