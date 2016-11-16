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
 * 動く→停止→攻撃→動くパターンの敵
 * @author moromorocco
 *
 */
public class EnemyTypeAttack extends AbstractSprite {
	private MediatorInterface mediator;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private Animation2 animeStop;
	private MakeShotInterface shotType;
	private int frameCount;
	private EnemyManager.TYPE type;
	
	private ShotTypeFactory factory;

	//行動パターン
	private STATE state;

	private enum STATE {
		MOVE(60),
		STOP(80);

		private final int changeTime;

		STATE(int changeTime) {
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
	 * @param type 種類
	 */
	public EnemyTypeAttack(GameMap map, MediatorInterface mediator, EnemyManager.TYPE type) {
		super(map);

		this.mediator = mediator;
		this.type = type;
		
		factory = new ShotTypeFactory();

		switch(type) {
			case ATTACK:
				shotType = factory.makeInstance(ShotTypeEnum.PLAYER_NERAI);
				break;
			case NAGE:
				shotType = factory.makeInstance(ShotTypeEnum.ENEMY_NAGE);
				break;
			default:
				break;
		}

		frameCount = 0;
		state = STATE.MOVE;
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
				state = STATE.STOP;
				break;
			case STOP:
				state = STATE.MOVE;
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
		animation = animeStop;

		if(frameCount == 30) {
			mediator.makeEnemyShot(shotType, super.getX(), super.getY());
		}
	}
}
