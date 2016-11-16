package com.game.actiongame.Map;

import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

/**
 * お店に入るためのクラス
 * @author moromorocco
 *
 */
class MapShop extends AbstractSprite {
	public MapShop(GameMap map) {
		super(map);
	}

	@Override
	public void loadImage() {
//		img = ImageLoaderUtility.getImage(ImageType.MAP_GATE);
		img = ImageLoaderUtility.loadImage(ImageTypeEnum.MAP_GATE);
	}

	@Override
	public void update(int delta) {
	}
}
