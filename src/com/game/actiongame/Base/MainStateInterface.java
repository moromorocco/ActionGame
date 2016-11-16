package com.game.actiongame.Base;

public interface MainStateInterface {
	/**
	 * シーン切り替え
	 * @param scene 切り替えるシーン
	 */
	public void changeScene(MainStateImpl.SCENE scene);

	public void getSeald();
}
