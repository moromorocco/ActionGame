package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * 右から左への、波型のショット玉攻撃
 * @author moromorocco
 *
 */
public class ShotBoss2Wave implements MakeShotInterface {
	private static final int SPEED = 5;

	@Override
	public void make(EnemyBulletManagerInterface manager, float x, float y) {
		manager.makeBullet(
				x,
				y,
				-SPEED,
				SPEED,
				SheetTypeEnum.BULLET_BOSS2_WAVE);
	}

}
