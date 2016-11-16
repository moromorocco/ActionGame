package com.game.actiongame.Enemy;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.SheetTypeEnum32;

/**
 * 左右に移動するだけ。
 * ただし、加速→停止→加速→停止
 * @author moromorocco
 *
 */
public class EnemyTypeMoveLR extends AbstractSprite {
	private static final int SPEED = 8;
	private MediatorInterface mediator;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private int frameCount;
	private boolean canMoveX;

	//行動パターン
	private STATE state;
	private STATE oldState;
	private enum STATE {
		MOVE_RIGHT(20),
		MOVE_LEFT(20),
		STOP(40);

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
	 */
	public EnemyTypeMoveLR(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		frameCount = 0;

		state = STATE.STOP;
		oldState = STATE.MOVE_LEFT;

		canMoveX = true;
	}

	@Override
	public void loadImage() {
//		SpriteSheet right =ImageLoaderUtility.getSheet(SheetType.ENEMY_KABOCHA_RIGHT);
//		SpriteSheet left =ImageLoaderUtility.getSheet(SheetType.ENEMY_KABOCHA_LEFT);

		SpriteSheet right =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_KABOCHA_RIGHT);
		SpriteSheet left =ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.ENEMY_KABOCHA_LEFT);
		
		animeRight = new Animation2(right, 60);
		animeRight.setAutoUpdate(false);
		animeLeft = new Animation2(left, 60);
		animeLeft.setAutoUpdate(false);

		animation = animeLeft;
	}

	@Override
	public void update(int delta) {
		if(!super.getIsUsing()) {
			return;
		}

		frameCount++;

		switch(state) {
			case MOVE_LEFT:
			case MOVE_RIGHT:
				move(delta);
				break;
			case STOP:
				//何もしない
				break;
			default:
				break;
		}

		if(frameCount > state.getChangeTime()) {
			changeState();
			frameCount = 0;
		}
	}

	/** 行動パターンの変更 */
	private void changeState() {
		switch(state) {
			case MOVE_LEFT:
			case MOVE_RIGHT:
				state = STATE.STOP;
				break;
			case STOP:
				//横に移動できなかった場合は、方向転換の処理
				if(!canMoveX) {
					if(oldState == STATE.MOVE_RIGHT) {
						state = STATE.MOVE_LEFT;
					} else {
						state = STATE.MOVE_RIGHT;
					}
				} else {
					state = oldState;
				}

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
		oldState = state;

		if(state == STATE.MOVE_RIGHT) {
			animation = animeRight;
			vx = SPEED;
		} else {
			animation = animeLeft;
			vx = -SPEED;
		}

		canMoveX = super.moveVX();
	}
}
