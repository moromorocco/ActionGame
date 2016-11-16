package com.game.actiongame.Utility;

/**
 * 画像を管理する列挙型
 * @author mozaedon
 *
 */
public enum ImageTypeEnum {
	// プレイヤーの下攻撃時の画像
	PLAYER_DOWN_ATTACK("itemSordDown.png"),
	// プレイヤーHPの枠
	PLAYER_FRAME("dogHPFrame.png"),
	// 武器
	ITEM_SOWRD("itemSord2.png"),
	// 盾
	ITEM_SEALD_RIGHT("itemSealdRight.png"), 
	ITEM_SEALD_LEFT("itemSealdLeft.png"),
	// アイテム
	ITEM_COIN("itemCoin.png"), ITEM_APPLE("itemApple.png"), 
	ITEM_NIKU("itemNiku.png"),
	// マップ
	MOVE_FLOOR("mapMoveFloor.png"),
	// マップ
	MOVE_HASHIGO("mapHashigo.png"), 
	MAP_GATE("mapGate.png"), 
	MAP_MESSAGE("mapMessage.png"), 
	MAP_ROCK("mapRock.png"),
	// ボスHPゲージ
	BOSS_HP_FRAME("bossHPFrame.png"),
	//ボス1
	BOSS1_NORMAL("boss1_normal.png"),
	BOSS1_SHOT("boss1_fire.png"),
	BOSS1_ATTACK("boss_attack.png"),
	BOSS1_WEPON("boss1_wepon.png"),
	//ボス2
	BOSS2_RIGHT("boss2_Right.png"),
	BOSS2_LEFT("boss2_Left.png"),
	// ショップ
	SHOP_BACK("shop_back.jpg"), SHOP_CHAR("shop_char.png"),
	// 盾
	ITEM_SEALD("itemSeald.png"), ITEM_MITEI("itemMitei.png");
	
	private final String filename;

	ImageTypeEnum(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
}
