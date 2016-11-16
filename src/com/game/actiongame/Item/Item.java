package com.game.actiongame.Item;

import org.newdawn.slick.Graphics;

import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

/**
 * アイテムクラス
 * @author moromorocco
 *
 */
public class Item extends AbstractSprite {
	private final int LIMIT = 300;
	private ITEM_TYPE type;
	private int frameCount;

	public enum ITEM_TYPE {
		COIN,
		APPLE,
		NIKU,
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 */
	public Item(GameMap map) {
		super(map);
	}

	@Override
	public void loadImage() {
		switch(type) {
			case COIN:
//				img = ImageLoaderUtility.getImage(ImageType.ITEM_COIN);
				img = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_COIN);
				break;
			case APPLE:
//				img = ImageLoaderUtility.getImage(ImageType.ITEM_APPLE);
				img = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_APPLE);
				break;
			case NIKU:
//				img = ImageLoaderUtility.getImage(ImageType.ITEM_NIKU);
				img = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_NIKU);
				break;
			default:
				break;
		}
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		//消滅前で点滅
		if(frameCount > LIMIT - 100) {
			if(frameCount % 2 == 0) {
				return;
			}
		}

		super.draw(g, offSetX, offSetY);
	}

	@Override
	public void update(int delta) {
		frameCount++;

		if(frameCount > LIMIT) {
			super.delete();
		}
	}

	/**
	 * アイテム作成
	 * @param x x座標
	 * @param y y座標
	 * @param type 種類
	 */
	public void make(float x, float y, ITEM_TYPE type) {
		this.type = type;

		super.make(x, y);

		frameCount = 0;
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		if(sprite instanceof Player) {
			//TODO アイテム取得したら何か処理
			SoundManager.playSE(SoundEnum.GET_ITEM);

			Player player = (Player)sprite;
			if(type == ITEM_TYPE.COIN) {
				player.addCoins(1);
			}

			super.delete();
		}
	}
}
