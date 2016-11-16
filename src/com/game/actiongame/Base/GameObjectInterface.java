package com.game.actiongame.Base;

import org.newdawn.slick.Graphics;

/**
 * ゲームオブジェクトのベースとなるインターフェース
 * @author moromorocco
 *
 */
public interface GameObjectInterface {
	/**
	 * 描画処理
	 * @param g graphics
	 * @param offSetX offSetX
	 * @param offSetY offSetY
	 */
	public void draw(Graphics g, float offSetX, float offSetY);

	/**
	 * 更新処理
	 * @param delta フレーム
	 */
	public void update(int delta);
}
