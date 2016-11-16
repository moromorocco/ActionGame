package com.game.actiongame.Utility;

/**
 * スプライトシートを管理するユーティリティクラス
 * @author mozaedon
 *
 */
public enum SheetTypeEnum implements KoSheetInterface {
	PLAYER_BULLET(OyaSheetEnum.PLAYER_BULLET, 0, 0, 16, 16),
	//敵弾
	ENEMY_BULLET_BLUE(OyaSheetEnum.BULLET, 0, 0, 32, 32),
	ENEMY_BULLET_ORANGE(OyaSheetEnum.BULLET, 0, 1, 32, 32),
	ENEMY_BULLET_PERPLE(OyaSheetEnum.BULLET, 0, 2, 32, 32),
	ENEMY_BULLET_RED(OyaSheetEnum.BULLET, 0, 3, 32, 32),
	ENEMY_BULLET_YELLOW(OyaSheetEnum.BULLET, 0, 4, 32, 32),
	ENEMY_BULLET_LIGHTGREEN(OyaSheetEnum.BULLET, 0, 5, 32, 32),
	ENEMY_BULLET_GREEN(OyaSheetEnum.BULLET, 0, 6, 32, 32),
	ENEMY_BULLET_GREENSKYBLUE(OyaSheetEnum.BULLET, 0, 7, 32, 32),
	BULLET_RAKKA(OyaSheetEnum.ENEMY_BULLET_RAKKA, 0, 0, 24, 24),
	BULLET_TURN(OyaSheetEnum.ENEMY_BULLET_TURN, 0, 0, 20, 20),
	BULLET_BOSS2_WAVE(OyaSheetEnum.BULLET, 0, 0, 32, 32);

		private final OyaSheetEnum oyaSheet;
		private final int subX;
		private final int subY;
		private final int width;
		private final int height;

		SheetTypeEnum(OyaSheetEnum oyaSheet, int subX, int subY, int width, int height) {
			this.oyaSheet = oyaSheet;
			this.subX = subX;
			this.subY = subY;
			this.width = width;
			this.height = height;
		}
	
		@Override
		public OyaSheetEnum getOya() {
			return oyaSheet;
		}
	
		@Override
		public int getSubX() {
			return subX;
		}
	
		@Override
		public int getSubY() {
			return subY;
		}
	
		@Override
		public int getWidth() {
			return width;
		}
	
		@Override
		public int getHeight() {
			return height;
		}
		
	}

