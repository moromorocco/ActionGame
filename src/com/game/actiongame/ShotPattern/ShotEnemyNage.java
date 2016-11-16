package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.GetRandomUtility;
import com.game.actiongame.Utility.SheetTypeEnum;

public class ShotEnemyNage implements MakeShotInterface {
	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		int vx = GetRandomUtility.getRand(3) + 1;

		if(manager.getPlayerX() < x) {
			vx = -vx;
		}

		manager.makeBullet(x, y, vx, -8, SheetTypeEnum.BULLET_RAKKA);
	}
}
