package com.game.actiongame.Player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Base.Start;
import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.SheetTypeEnum;

/**
 * プレイヤーの魔法攻撃クラス
 * @author moromorocco
 *
 */
public class PlayerBullet extends AbstractSprite {
	public static final int WIDTH = Start.WIDTH;
	public static final int HEIGHT = Start.HEIGHT;
	private MediatorInterface mediator;
	private TYPE bulletType;

	public enum TYPE {
		RED;
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public PlayerBullet(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		bulletType = TYPE.RED;
	}

	@Override
	public void loadImage() {
		SpriteSheet sheet = null;
		switch(bulletType) {
			case RED:
//				sheet = ImageLoaderUtility.getSheet(SheetType.PLAYER_BULLET_RED);
				sheet = ImageLoaderUtility.loadKoSheet(SheetTypeEnum.PLAYER_BULLET);
				
				break;
			default:
				return;
		}

		animation = new Animation2(sheet, 60);
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		super.draw(g, offSetX, offSetY);

		if(x + offSetX < 0 || x + offSetX > WIDTH) {
			super.delete();
		}

		if(y +  + offSetY < 0 || y + offSetY > getHeight() + HEIGHT) {
			super.delete();
		}
	}

	@Override
	public void update(int delta) {
		x += vx;
		y += vy;

		if(!super.moveVX()) {
			makeEffect();
			super.delete();
		}

		if(!super.moveVY()) {
			makeEffect();
			super.delete();
		}
	}

	/**
	 * スプライトの作成
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 * @param bulletType 弾の種類(色)
	 */
	public void make(float x, float y, float vx, float vy, TYPE bulletType) {
		this.bulletType = bulletType;

		super.make(x, y, vx, vy);
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		//エフェクトの作成
		makeEffect();

		delete();
	}

	/** 消滅エフェクト作成 */
	private void makeEffect() {
		mediator.makeEffect(x, y, EFFECT_TYPE.SHOT_HIT);
	}
}
