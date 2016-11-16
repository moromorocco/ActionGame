package com.game.actiongame.Scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

import com.game.actiongame.Base.GameSceneInterface;
import com.game.actiongame.Base.MainStateImpl.SCENE;
import com.game.actiongame.Base.MainStateInterface;
import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.SelectBox;

public class PauseScene implements GameSceneInterface {
	private MainStateInterface mainState;
	private UnicodeFont font;
	private SelectBox box;

	private enum BOX_MENU {
		CANCEL,
		OPTION,
		TO_TITLE,
	}

	public PauseScene(MainStateInterface mainState) {
		this.mainState = mainState;

		font = FontUtility.getFont();

		box = new SelectBox();
		box.add(BOX_MENU.CANCEL.toString(), "ポーズ解除");
		box.add(BOX_MENU.OPTION.toString(), "オプション");
		box.add(BOX_MENU.TO_TITLE.toString(), "タイトルへ");
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		g.setFont(font);

		g.drawString("PAUSE", 50, 100);

		box.draw(g);
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE) {
			mainState.changeScene(SCENE.GAME);
		}

		box.keyPressed(key, c);

		if(key == Input.KEY_Z) {
			BOX_MENU select = BOX_MENU.valueOf(box.pressZ());

			switch(select) {
				case CANCEL:
					mainState.changeScene(SCENE.GAME);
					break;
				case OPTION:
					mainState.changeScene(SCENE.OPTION);
					break;
				case TO_TITLE:
					mainState.changeScene(SCENE.TITLE);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
	}

}
