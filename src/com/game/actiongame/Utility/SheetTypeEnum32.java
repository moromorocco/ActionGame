package com.game.actiongame.Utility;

public enum SheetTypeEnum32 implements KoSheetInterface32 {
	//プレイヤーグラフィック
	PLAYER_LEFT(OyaSheetEnum.PLAYER, 0, 1),
	PLAYER_RIGHT(OyaSheetEnum.PLAYER, 0, 2),
	//敵グラ
	ENEMY_OBAKE_LEFT(OyaSheetEnum.ENEMY_OBAKE, 0, 1),
	ENEMY_OBAKE_RIGHT(OyaSheetEnum.ENEMY_OBAKE, 0, 2),
	ENEMY_KABOCHA_LEFT(OyaSheetEnum.ENEMY_KABOCHA, 0, 1),
	ENEMY_KABOCHA_RIGHT(OyaSheetEnum.ENEMY_KABOCHA, 0, 2),
	ENEMY_KABOCHA_STOP(OyaSheetEnum.ENEMY_KABOCHA, 0, 0),
	//エフェクト
	EFFECT_SHOT_HIT(OyaSheetEnum.EFFECT_SHOT_HIT, 0, 6);

	SheetTypeEnum32(OyaSheetEnum oyaSheet, int subX, int subY) {
		this.oyaSheet = oyaSheet;
		this.subX = subX;
		this.subY = subY;
	}

	private final OyaSheetEnum oyaSheet;
	private final int subX;
	private final int subY;

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
}
