package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.SheetTypeEnum;

public class ShotEnemyTurn implements MakeShotInterface {
	private static final int SPEED = 3;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		int vx = SPEED;

		if(manager.getPlayerX() < x) {
			vx = -vx;
		}

		manager.makeBullet(x, y, vx, 0, SheetTypeEnum.BULLET_TURN);
	}
}
