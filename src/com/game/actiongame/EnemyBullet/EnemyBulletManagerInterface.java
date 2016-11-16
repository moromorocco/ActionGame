package com.game.actiongame.EnemyBullet;

import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * 敵弾管理クラスに通知するインターフェース
 * @author moromorocco
 *
 */
public interface EnemyBulletManagerInterface {
	/**
	 * プレイヤーとの角度取得
	 *
	 * @param x x座標
	 * @param y y座標
	 * @return rad ラジアン
	 */
	public double gerPlayerRad(float x, float y);

	/**
	 * 敵ショット作成(まっすぐ飛ぶタイプ)
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 * @param type 弾の描画種類
	 */
	public void makeBullet(float x, float y, float vx, float vy,
			SheetTypeEnum type);

//	/**
//	 * 敵ショット作成（ユニークタイプ)
//	 * @param x x座標
//	 * @param y y座標
//	 * @param vx 横加速度
//	 * @param vy 縦加速度
//	 * @param type 弾の描画種類
//	 */
//	public void makeBullet(float x, float y, float vx, float vy,
//			EnemyBulletUniqueEnum type);

	
	/**
	 * プレイヤーのX座標を取得
	 * @return x x座標
	 */
	public float getPlayerX();

	/**
	 * プレイヤーのY座標を取得
	 * @return y y座標
	 */
	public float getPlayerY();
}

