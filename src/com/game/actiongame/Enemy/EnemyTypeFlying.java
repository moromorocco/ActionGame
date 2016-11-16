package com.game.actiongame.Enemy;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.ShotPattern.MakeShotInterface;
import com.game.actiongame.ShotPattern.ShotTypeEnum;
import com.game.actiongame.ShotPattern.ShotTypeFactory;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.SheetTypeEnum32;

/**
 * プレーヤーのX座標が一致したら攻撃する敵クラス
 * @author moromorocco
 *
 */
public class EnemyTypeFlying extends AbstractSprite {
	private MediatorInterface mediator;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private Animation2 animeStop;
	private MakeShotInterface shotType;
	private int frameCount;
	private float oldX;

	//行動パターン
	private STATE state;
	private enum STATE {
		MOVE,
		STOP;
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public EnemyTypeFlying(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		frameCount = 0;
		state = STATE.MOVE;

		ShotTypeFactory factory = new ShotTypeFactory();
		shotType = factory.makeInstance(ShotTypeEnum.ENEMY_FLYING);
	}

	@Override
	public void loadImage() {
//		SpriteSheet right =ImageLoaderUtility.getSheet(SheetType.ENEMY_KABOCHA_RIGHT);
//		SpriteSheet left =ImageLoaderUtility.getSheet(SheetType.ENEMY_KABOCHA_LEFT);
//		SpriteSheet stop =ImageLoaderUtility.getSheet(SheetType.ENEMY_KABOCHA_STOP);
		SpriteSheet right =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_KABOCHA_RIGHT);
		SpriteSheet left =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_KABOCHA_LEFT);
		SpriteSheet stop =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_KABOCHA_STOP);
		
		animeRight = new Animation2(right, 60);
		animeRight.setAutoUpdate(false);
		animeLeft = new Animation2(left, 60);
		animeLeft.setAutoUpdate(false);
		animeStop = new Animation2(stop, 60);
		animeStop.setAutoUpdate(false);

		animation = animeRight;
	}

	@Override
	public void update(int delta) {
		if(!super.getIsUsing()) {
			return;
		}

		oldX = x;

		switch(state) {
			case MOVE:
				move(delta);
				break;
			case STOP:
				stop(delta);
				break;
			default:
				break;
		}

		//X座標が一致、もしくはすれ違ったら攻撃パターンに変更
		if((int)(x - mediator.getPlayerX()) * (int)(oldX - mediator.getPlayerX()) <= 0) {
			state = STATE.STOP;
		}
	}

	/**
	 * 左右に移動
	 * @param delta
	 */
	private void move(int delta) {
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
	}

	/**
	 * 攻撃態勢
	 * @param delta
	 */
	private void stop(int delta) {
		frameCount++;
		animation = animeStop;

		if(frameCount  == 30) {
			mediator.makeEnemyShot(shotType, getCenterX(), y);
		}

		if(frameCount > 50) {
			frameCount = 0;
			state = STATE.MOVE;
		}
	}
}
