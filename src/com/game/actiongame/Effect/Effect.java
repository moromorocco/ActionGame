package com.game.actiongame.Effect;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.OyaSheetEnum;
import com.game.actiongame.Utility.SheetTypeEnum32;

/**
 * エフェクトを描画するクラス
 * @author moromorocco
 *
 */
public class Effect extends AbstractSprite {
	private EFFECT_TYPE effectType;

	public enum EFFECT_TYPE {
		NOTHING,
		SHOT_HIT,
		ENEMY_DEAD,
		BOSS_DEAD,
	}

	/** コンストラクタ */
	public Effect() {
		super(null);
	}

	@Override
	public void loadImage() {
		SpriteSheet sheet;
		switch(effectType) {
			case ENEMY_DEAD:
				sheet  = ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.EFFECT_SMOKE);
				break;
			case SHOT_HIT:
//				sheet = ImageLoaderUtility.getSheet(SheetType.EFFECT_SHOTHIT);
				sheet  = ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.EFFECT_SHOT_HIT);
				break;
			case BOSS_DEAD:
//				sheet = ImageLoaderUtility.getSheet(SheetType.EFFECT_BOSS_DEAD);
				sheet  = ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.EFFECT_BOSS_DEAD);
				break;
			default:
				return;
		}

		animation = new Animation2(sheet, 60);
		animation.setLooping(false);
	}

	@Override
	public void update(int delta) {
		if(animation.isStopped()) {
			super.delete();
		}
	}

	/**
	 * エフェクト作成
	 * @param x x座標
	 * @param y y座標
	 * @param effectType 種類
	 */
	public void make(float x, float y, EFFECT_TYPE effectType) {
		this.effectType = effectType;

		super.make(x, y);
	}
}
