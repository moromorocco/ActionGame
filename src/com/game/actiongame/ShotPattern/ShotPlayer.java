package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * プレイヤー狙いに攻撃するパターン
 * @author moromorocco
 *
 */
public class ShotPlayer implements MakeShotInterface {
	private static final int SPEED = 4;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		double rad = manager.gerPlayerRad(x, y);

		manager.makeBullet(
				x,
				y,
				(float)(SPEED * Math.sin(rad)),
				(float)(SPEED * Math.cos(rad)),
				SheetTypeEnum.ENEMY_BULLET_ORANGE);
	}
}
