package com.game.actiongame.Player;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

/**
 * プレイヤーの魔法攻撃管理クラス
 * @author moromorocco
 *
 */
public class PlayerBulletManager extends AbstractManager {
	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public PlayerBulletManager(GameMap map, MediatorInterface mediator) {
		super(map, mediator);

		super.init();
	}

	/**
	 * ショットの作成
	 *
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 * @param type 描画種類
	 */
	public void makeShot(float x, float y, float vx, float vy,
			PlayerBullet.TYPE type) {

		PlayerBullet bullet = (PlayerBullet)super.getSprite();
		bullet.make(x, y, vx, vy, type);

		SoundManager.playSE(SoundEnum.PLAYER_SHOT);
	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new PlayerBullet(map, mediator);
	}
}
