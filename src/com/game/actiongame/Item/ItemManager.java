package com.game.actiongame.Item;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * アイテム管理クラス
 * @author moromorocco
 *
 */
public class ItemManager extends AbstractManager{

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public ItemManager(GameMap map, MediatorInterface mediator) {
		super(map, mediator);
	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new Item(map);
	}

	/**
	 * アイテム作成
	 * @param x x座標
	 * @param y y座標
	 * @param itemType アイテム種類
	 */
	public void makeItem(float x, float y, Item.ITEM_TYPE itemType) {
		Item item = (Item)super.getSprite();

		item.make(x, y, itemType);
	}
}
