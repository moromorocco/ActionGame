package com.game.actiongame.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

/**
 * メッセージを表示するクラス
 * @author moromorocco
 *
 */
public class MapMessage extends AbstractSprite {
	private static final int DIFF = 5;
	private UnicodeFont font;
	private String message;
	private int frameCount;
	private int charCount;
	private boolean isHit;

	/**
	 * コンストラクタ
	 * @param map ゲームマップ
	 */
	public MapMessage(GameMap map) {
		super(map);

		frameCount = 0;

		font = FontUtility.getFont();
	}

	@Override
	public void loadImage() {
//		super.img = ImageLoaderUtility.getImage(ImageType.MAP_MESSAGE);
		img = ImageLoaderUtility.loadImage(ImageTypeEnum.MAP_MESSAGE);
	}

	public void make(float x, float y, String message) {
		this.message = message;

		super.make(x, y);
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		super.draw(g, offSetX, offSetY);

		g.setFont(font);

		if(isHit) {
			g.setColor(Color.white);
	        g.fillRect(0, 20, WIDTH, 100);

			g.setColor(Color.black);
	        g.fillRect(DIFF, 20 + DIFF, WIDTH - DIFF * 2, 100 - DIFF * 2);

        	String showMessage = message.substring(0, charCount);

        	g.setColor(Color.white);
        	g.drawString(showMessage, 20 + DIFF + 5 , 20 + DIFF + 5);
		}
	}

	@Override
	public void update(int delta) {
		if(isHit) {
			frameCount++;

			if(frameCount >= 10) {
				if(charCount < message.length()) {
					charCount++;
				}

				frameCount = 0;
			}
		}
	}

	/** プレイヤーに当たったこ事をセット */
	public void setHitPlayer() {
		isHit = true;
	}

	public void setNotHitPlayer() {
		charCount = 0;

		isHit = false;
	}
}
