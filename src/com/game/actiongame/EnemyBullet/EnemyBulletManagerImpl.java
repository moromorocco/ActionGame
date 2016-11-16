package com.game.actiongame.EnemyBullet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * 敵弾管理クラス
 * @author moromorocco
 *
 */
public class EnemyBulletManagerImpl extends AbstractManager
		implements EnemyBulletManagerInterface {

	public EnemyBulletManagerImpl(GameMap map, MediatorInterface mediator) {
		super(map, mediator);
	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new EnemyBullet(map, mediator);
	}

	@Override
	public double gerPlayerRad(float x, float y) {
		return mediator.getRad(x, y);
	}

	@Override
	public void makeBullet(float x, float y, float vx, float vy,
			SheetTypeEnum type) {

		EnemyBullet bullet = (EnemyBullet)super.getSprite();
		bullet.make(x, y, vx, vy, type);
	}
	
//	@Override
//	public void makeBullet(float x, float y, float vx, float vy,
//			EnemyBulletUniqueEnum type) {
//
//		EnemyBulletUnique bulletUnique = (EnemyBulletUnique)super.getSprite();
//		bulletUnique.make(x, y, vx, vy, type);
//	}


	@Override
	public float getPlayerX() {
		return mediator.getPlayerX();
	}

	@Override
	public float getPlayerY() {
		return mediator.getPlayerY();
	}
}
