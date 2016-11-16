package com.game.actiongame.Utility;

/**
 * スプライトシートの大本
 * @author mozaedon
 *
 */
public enum OyaSheetEnum implements OyaSheetInterface {
	PLAYER("dog.png", 96, 32),
	PLAYER_DOWN("dogFront.png", 32, 32),
	PLAYER_BULLET("bulletPlayer.png", 64, 16),
	ENEMY_OBAKE("enemyBlack.png", 96, 32),
	ENEMY_KABOCHA("enemyKabocha.png", 96, 32),
	ENEMY_TRAP("enemyTrap.png", 24, 32),
	BULLET("bulletEnemy.png", 128, 32),
	//自由落下する用の敵弾
	ENEMY_BULLET_RAKKA("bulletEnemyRakka.png", 24, 24),
	//ブーメラン
	ENEMY_BULLET_TURN("bulletTurn.png", 20, 20),
	EFFECT_SMOKE("effectSmoke.png", 64, 64),
	EFFECT_SHOT_HIT("effectShotHit.png", 192, 32),
	EFFECT_BOSS_DEAD("effectExplosion.png", 120, 120),
	EFFECT_BOSS2_ATTACK("boss2Attack.png", 192, 192),
	SHOP_CURSOL("shop_cursol.png", 30, 16);
	
	private final String filename;
	private final int width;
	private final int height;
	
	OyaSheetEnum(String filename, int width, int height) {
		this.filename = filename;
		this.width = width;
		this.height = height;
	}

	@Override
	public String getFilename() {
		return filename;
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
