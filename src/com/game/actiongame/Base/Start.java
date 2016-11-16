package com.game.actiongame.Base;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.MyKeyListener;

/**
 * メインクラス
 * @author moromorocco
 *
 */
public class Start extends StateBasedGame {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 480;

	public Start() {
		super("アクションゲームV2.1");
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		//ゲーム画面
		addState(new MainStateImpl());

		//キーリスナ登録
		Input input = gc.getInput();
		input.addKeyListener(new MyKeyListener());

		//フォントセット
		gc.setDefaultFont(FontUtility.getFont());

		//画面に移動
		enterState(MainStateImpl.MAIN_ID);
	}

	public static void main(String[] args) {
		Start game = new Start();
		AppGameContainer container;

		try {
			container = new AppGameContainer(game);
			container.setDisplayMode(WIDTH, HEIGHT, false);		//ウインドウサイズ
			container.setTargetFrameRate(60);							//FPS
			container.start();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	}
}
