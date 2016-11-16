package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * 右から順番に上からショットが降ってくる
 * @author moromorocco
 *
 */
public class ShotBoss2Rain implements MakeShotInterface {
	private static final int SPEED = 4;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		manager.makeBullet(
				x,
				y,
				0,
				SPEED,
				SheetTypeEnum.ENEMY_BULLET_BLUE);
	}
}
