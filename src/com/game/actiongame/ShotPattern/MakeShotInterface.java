package com.game.actiongame.ShotPattern;

import com.game.actiongame.EnemyBullet.EnemyBulletManagerInterface;

/**
 * 敵の攻撃を作成するインターフェース
 * @author moromorocco
 *
 */
public interface MakeShotInterface {
	/**
	 * 敵ショット作成
	 * @param manager 敵ショットマネージャ
	 * @param x x座標
	 * @param y y座標
	 */
	public void make(EnemyBulletManagerInterface manager, float x, float y);
}
