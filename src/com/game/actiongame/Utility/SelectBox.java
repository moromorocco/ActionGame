package com.game.actiongame.Utility;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

/**
 * 選択ボックス
 * @author moromorocco
 *
 */
public class SelectBox {
	private final int DRAW_X = 250;

	private LinkedHashMap<String, String> messageList;
	private UnicodeFont font;
	private int selected;

	/**
	 * コンストラクタ
	 */
	public SelectBox() {
		messageList = new LinkedHashMap<String, String>();
		messageList.clear();

		font = FontUtility.getFont();
		selected = 0;
	}

	/**
	 * リストにメッセージ追加
	 * @param key キー
	 * @param message メッセージ内容
	 */
	public void add(String key, String message) {
		messageList.put(key, message);
	}

	/**
	 * 描画
	 * @param g graphics
	 */
	public void draw(Graphics g) {
		g.setFont(font);
		g.setColor(Color.white);

		int i = 0;
		Iterator<String> itr = messageList.values().iterator();
		while(itr.hasNext()) {
			String title = (String)itr.next();
			g.drawString(title, DRAW_X - (font.getWidth(title)/2), 200+(i * 50));

			if (selected == i) {
				g.drawRect(DRAW_X - 100, 190 + (i*50), 200, 50);
			}

			i++;
		}
	}

	/**
	 * キー入力
	 * @param key key
	 * @param c char
	 */
	public void keyPressed(int key, char c) {
		int length = messageList.size();

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

		if (key == Input.KEY_X) {
		}
	}

	/**
	 * 決定ボタン押下時の選択項目取得
	 * @return keyValue キー文字列
	 */
	public String pressZ() {
		int i = 0;
		for(String keyStr : messageList.keySet()) {
			if(i == selected) {
				return keyStr;
			}
			i++;
		}

		return "";
	}
}
