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
 * ブーメランで攻撃してくる敵クラス
 * @author moromorocco
 *
 */
public class EnemyTypeTurn extends AbstractSprite {
	private MediatorInterface mediator;
	private MakeShotInterface shotType;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private int frameCount;

	//行動パターン
	private StateEnum state;

	private enum StateEnum {
		MOVE(60),
		STOP(90);

		private final int changeTime;

		StateEnum(int changeTime) {
			this.changeTime = changeTime;
		}

		public int getChangeTime() {
			return changeTime;
		}
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public EnemyTypeTurn(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		ShotTypeFactory factory = new ShotTypeFactory();
		shotType = factory.makeInstance(ShotTypeEnum.ENEMY_TURN);

		frameCount = 0;
		state = StateEnum.MOVE;
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
		if(!super.getIsUsing()) {
			return;
		}

		frameCount++;

		if(frameCount > state.getChangeTime()) {
			changeState();
			frameCount = 0;
		}

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
	}

	/** 行動パターンの変更 */
	private void changeState() {
		switch(state) {
			case MOVE:
				state = StateEnum.STOP;
				break;
			case STOP:
				state = StateEnum.MOVE;
				break;
			default:
				break;
		}
	}

	/**
	 * 左右に移動
	 * @param delta
	 */
	private void move(int delta) {
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

	/**
	 * 停止中
	 * @param delta
	 */
	private void stop(int delta) {
		if(getX() < mediator.getPlayerX()) {
			//プレイヤーより左側
			animation = animeRight;
		} else {
			animation = animeLeft;
		}

		if(frameCount == 30) {
			mediator.makeEnemyShot(shotType, super.getX(), super.getY());
		}
	}
}
