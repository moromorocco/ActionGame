package com.game.actiongame.Enemy;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.SheetTypeEnum32;

/**
 * 左右に移動するだけの敵クラス
 * @author moromorocco
 *
 */
public class EnemyTypeNormal extends AbstractSprite {
	private MediatorInterface mediator;
	private Animation2 animeRight;
	private Animation2 animeLeft;

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public EnemyTypeNormal(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;
	}

	@Override
	public void loadImage() {
//		SpriteSheet right =ImageLoaderUtility.getSheet(SheetType.ENEMY_BLACK_RIGHT);
//		SpriteSheet left =ImageLoaderUtility.getSheet(SheetType.ENEMY_BLACK_LEFT);

		SpriteSheet right =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_OBAKE_RIGHT);
		SpriteSheet left =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_OBAKE_LEFT);

		animeRight = new Animation2(right, 60);
		animeRight.setAutoUpdate(false);
		animeLeft = new Animation2(left, 60);
		animeLeft.setAutoUpdate(false);

		animation = animeRight;
	}

	@Override
	public void update(int delta) {
		if (getIsUsing()) {
			vy = (float)(vy + GameMap.GRAVITY);

			if (getVx() > 0) {
				animation.update(delta);
				animation = animeRight;
			} else if (getVx() < 0) {
				animation.update(delta);
				animation = animeLeft;
			}

			if(super.moveVX() == false) {
				setVx(-1 * getVx());
			}

			super.moveVY();
		}
	}
}
