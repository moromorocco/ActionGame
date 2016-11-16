package com.game.actiongame.Utility;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * フォント取得ユーティリティクラス
 * @author moromorocco
 *
 */
public class FontUtility {
	/** 唯一のインスタンス */
	private static UnicodeFont ufont = null;

	private FontUtility() {
	}

	/**
	 * インスタンスの取得
	 * @return ufont 唯一のインスタンス
	 */
	public static UnicodeFont getFont() {
		if(ufont == null) {
			loadFont();
		}

		return ufont;
	}

	@SuppressWarnings("unchecked")
	private static void loadFont() {
		try {
			ufont = new UnicodeFont("data/rounded-l-mplus-1c-black.ttf", 16, false, false);
			ufont.addAsciiGlyphs();
			ufont.addGlyphs(0x2190,0x21ff); // yajirusi
			ufont.addGlyphs(0x3000, 0x30ff); // Hiragana + katakanab + fullwidth punctuations
			ufont.addGlyphs(0x4e00,0x9fc0); // Kanji

			ufont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			ufont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
