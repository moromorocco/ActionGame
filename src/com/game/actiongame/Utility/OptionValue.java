package com.game.actiongame.Utility;

import org.newdawn.slick.SavedState;
import org.newdawn.slick.SlickException;

/**
 * 調節した音量の値を保持するクラス
 * @author moromorocco
 *
 */
public class OptionValue {
	public static final String OPTION_FILE = "optionFile";
	public static final String OPTION_BGM = "bgm";
	public static final String OPTION_SE = "se";

	private SavedState savedState;
	private int bgm = 100;
	private int se = 100;

	/** コンストラクタ */
	public OptionValue() {
		try {
			savedState = new SavedState(OPTION_FILE);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		bgm = (int) savedState.getNumber(OPTION_BGM, 100);
		se = (int) savedState.getNumber(OPTION_SE, 100);

		System.out.println("bgm = " + bgm);
		System.out.println("se = " + se);

		SoundManager.setVolumeBGM((float)bgm / 100);
		SoundManager.setVolumeSE((float)se / 100);
	}

	/** BGM大きくする */
	public void addBGM() {
		bgm = addVal(bgm);

		SoundManager.setVolumeBGM((float)bgm / 100);
	}

	/** SE大きくする */
	public void addSE() {
		se = addVal(se);

		SoundManager.setVolumeBGM((float)se / 100);
	}

	/** BGM小さくする */
	public void lessBGM() {
		bgm = lessVal(bgm);

		SoundManager.setVolumeBGM((float)bgm / 100);
	}

	/** SE小さくする */
	public void lessSE() {
		se = lessVal(se);

		SoundManager.setVolumeSE((float)se / 100);
	}

	/** BGMを初期値にする */
	public void setDefaultBGM() {
		bgm = 100;
		SoundManager.setVolumeBGM((float)bgm / 100);
	}

	/** SEを初期値にする */
	public void setDefaultSE() {
		se = 100;
		SoundManager.setVolumeSE((float)bgm / 100);
	}

	/**
	 * BGMの音量取得
	 * @return bgm
	 */
	public int getBGM() {
		return bgm;
	}

	/**
	 * 効果音の音量取得
	 * @return se
	 */
	public float getSE() {
		return se;
	}

	/** オプションの値を保存 */
	public void saveFile() {
		try {
			savedState.setNumber(OPTION_BGM, bgm);
			savedState.setNumber(OPTION_SE, se);
			savedState.save();

			System.out.println("option saved");
		} catch (Exception e) {
			System.out.println(System.currentTimeMillis() + " : Failed to save state");
		}
	}

	private int addVal(int val) {
		val += 5;

		if(val >= 100) val = 100;

		return val;
	}

	private int lessVal(int val) {
		val -= 5;

		if(val <= 0) val = 0;

		return val;
	}
}
