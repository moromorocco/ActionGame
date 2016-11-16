package com.game.actiongame.Base;

public interface GameSceneInterface extends GameObjectInterface {
	/**
	 * キー押下時の処理
	 * @param key キーコード
	 * @param c char
	 */
	public void keyPressed(int key, char c);
	/**
	 * キー離した時の処理
	 * @param key キーコード
	 * @param c char
	 */
	public void keyReleased(int key, char c);
}
