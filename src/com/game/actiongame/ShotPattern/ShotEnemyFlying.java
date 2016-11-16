package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.SheetTypeEnum;

public class ShotEnemyFlying implements MakeShotInterface {
	private static final int SPEED = 3;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		manager.makeBullet(x, y, 0, SPEED, SheetTypeEnum.ENEMY_BULLET_ORANGE);
	}
}
