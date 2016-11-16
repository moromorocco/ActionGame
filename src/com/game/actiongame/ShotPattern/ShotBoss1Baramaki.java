package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.GetRandomUtility;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * バラマキ弾の作成
 * @author moromorocco
 *
 */
public class ShotBoss1Baramaki implements MakeShotInterface {
	private static final int SPEED = 4;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		int dec = -20 - GetRandomUtility.getRand(30);

		double rad = Math.toRadians(dec);

		manager.makeBullet(
				x,
				y,
				(float)(SPEED * Math.sin(rad)),
				(float)(SPEED * Math.cos(rad)),
				SheetTypeEnum.ENEMY_BULLET_ORANGE);
	}

}
