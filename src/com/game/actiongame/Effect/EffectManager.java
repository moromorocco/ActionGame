package com.game.actiongame.Effect;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * エフェクト管理クラス
 * @author moromorocco
 *
 */
public class EffectManager extends AbstractManager {

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public EffectManager(GameMap map, MediatorInterface mediator) {
		super(map, mediator);
	}

	/**
	 * エフェクト作成
	 * @param x x座標
	 * @param y y座標
	 * @param effectType 種類
	 */
	public void makeEffect(float x, float y, EFFECT_TYPE effectType) {
		Effect effect = (Effect)super.getSprite();

		effect.make(x, y, effectType);
	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new Effect();
	}
}
