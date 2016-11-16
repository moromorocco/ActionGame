package com.game.actiongame.Map;

import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

public class MapHashigo extends AbstractSprite {
	private static final int HIT_DIFF = 3;

	public MapHashigo(GameMap map) {
		super(map);
	}

	@Override
	public void loadImage() {
//		img = ImageLoaderUtility.getImage(ImageType.MOVE_HASHIGO);
		img = ImageLoaderUtility.loadImage(ImageTypeEnum.MOVE_HASHIGO);
	}

	@Override
	public void update(int delta) {
	}
	
	
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(
				x + HIT_DIFF,
				y,
				this.getWidth() - HIT_DIFF,
				this.getHeight());
	}
	
//	@Override
//	public void hitSprite(AbstractSprite sprite) {
//		Player player = (Player) sprite;
//	}
}
