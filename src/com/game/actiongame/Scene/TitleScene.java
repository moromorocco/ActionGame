package com.game.actiongame.Scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

import com.game.actiongame.Base.GameSceneInterface;
import com.game.actiongame.Base.MainStateImpl.SCENE;
import com.game.actiongame.Base.MainStateInterface;
import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.SelectBox;

public class TitleScene implements GameSceneInterface {
	private MainStateInterface mainState;
	private UnicodeFont font;
	private SelectBox box;

	private enum BOX_MENU {
		START,
		OPTION,
		EXIT;
	}

	public TitleScene(MainStateInterface mainState) {
		this.mainState = mainState;
		font = FontUtility.getFont();

		box = new SelectBox();
		box.add(BOX_MENU.START.toString(), "スタート");
		box.add(BOX_MENU.OPTION.toString(), "オプション");
		box.add(BOX_MENU.EXIT.toString(), "終了");
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		g.setFont(font);

		box.draw(g);
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public void keyPressed(int key, char c) {
		box.keyPressed(key, c);

		if(key == Input.KEY_Z) {
			BOX_MENU select = BOX_MENU.valueOf(box.pressZ());

			switch(select) {
				case START:
					mainState.changeScene(SCENE.NEW_GAME);
					break;
				case OPTION:
					mainState.changeScene(SCENE.OPTION);
					break;
				case EXIT:
					System.exit(0);
				default:
					break;
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
	}
}
