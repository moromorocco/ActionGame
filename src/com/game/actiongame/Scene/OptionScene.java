package com.game.actiongame.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

import com.game.actiongame.Base.GameSceneInterface;
import com.game.actiongame.Base.MainStateImpl;
import com.game.actiongame.Base.MainStateImpl.SCENE;
import com.game.actiongame.Base.MainStateInterface;
import com.game.actiongame.Utility.BGMEnum;
import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.OptionValue;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

import lombok.Getter;

public class OptionScene implements GameSceneInterface {
	private MainStateInterface mainState;
	private UnicodeFont font;

	@Getter
	private String[] options;
	private OptionValue optionValue;
	@Getter
	private int selected;
	private int seCount;

	private MainStateImpl.SCENE oldScene;

	public OptionScene(MainStateInterface mainState) {
		this.mainState = mainState;
		font = FontUtility.getFont();

		SoundManager.playBGM(BGMEnum.BOSS);

		options = new String[] {"VOLUME","SE","Default", "スコア初期化", "Exit"};

		optionValue = new OptionValue();

		init();
	}

	public void init() {
		selected = 0;
		seCount = 0;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		g.setFont(font);
		g.setColor(Color.white);

		for(int i = 0; i < options.length; i++) {
			g.drawString(options[i], 100 - (font.getWidth(options[i])/2), 50+(i*50));

			if (selected == i) {
				g.drawRect(50, 40+(i*50), 200, 50);
			}

			g.drawString(String.format("%3d " + " ％", (int)optionValue.getBGM()), 200, 50);
			g.drawString(String.format("%3d " + " ％", (int)optionValue.getSE()), 200, 100);
			g.drawString("←→キーで調節/Xキー・ESCキーで", 30, 400);
			g.drawString("メニューに戻る", 30, 430);
		}
	}

	@Override
	public void update(int delta) {
		//SE選択時、効果音再生
		if(selected == 1) {
			seCount++;

			if(seCount > 30) {
				SoundManager.playSE(SoundEnum.PLAYER_SHOT);
				seCount = 0;
			}
		} else {
			seCount = 0;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE) {
			mainState.changeScene(SCENE.GAME);
		}

		int length = options.length;

		if (key == Input.KEY_DOWN) {
			selected++;
			if (selected >= length) {
				selected = 0;
			}
		}

		if (key == Input.KEY_UP) {
			selected--;
			if (selected < 0) {
				selected = length - 1;
			}
		}

		if(key == Input.KEY_RIGHT) {
			pressRight();
		}

		if(key == Input.KEY_LEFT) {
			pressLeft();
		}

		if(key == Input.KEY_X || key == Input.KEY_ESCAPE) {
			doExit();
		}

		if(key == Input.KEY_Z) {
			if(selected == 2) {
				optionValue.setDefaultBGM();
				optionValue.setDefaultSE();
			}

//			if(selected == 3) {
//				ScoreReader.Singleton.INSTANCE.getInstance().clearScore();
//			}

			if(selected == 4) {
				doExit();
			}
		}

	}

	@Override
	public void keyReleased(int key, char c) {
	}

	/**
	 * 前のシーンを保存
	 * @param oldScene 移動前のシーン
	 */
	public void setScene(MainStateImpl.SCENE oldScene) {
		this.oldScene = oldScene;
	}

	/** 右キー押下時の処理 */
	private void pressRight() {
		switch(selected) {
			case 0:
				optionValue.addBGM();
				break;
			case 1:
				optionValue.addSE();
				break;
		}
	}

	/** 左キー押下時の処理 */
	private void pressLeft() {
		switch (selected) {
			case 0:
				optionValue.lessBGM();
				break;
			case 1:
				optionValue.lessSE();
				break;
		}
	}

	/** メニュー画面に戻る */
	private void doExit() {
		optionValue.saveFile();

		mainState.changeScene(oldScene);
	}
}
