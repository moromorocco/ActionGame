package com.game.actiongame.Enemy;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.SheetTypeEnum32;

public class EnemyTypeSinmove extends AbstractSprite {
	private static final int SPEED = 8;
	private MediatorInterface mediator;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private int frameCount;
	private float moveHeight;
	private float dec;

	public EnemyTypeSinmove(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		dec = 0;
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
		turn();
	}

	@Override
	public void update(int delta) {
		if(!super.getIsUsing()) {
			return;
		}

		frameCount++;
		dec += 5;
		if(dec >=360) {
			dec = 0;
		}

		if(!super.moveVX()) {
			vx = -vx;

			turn();
		}

		vy = moveHeight * (float)Math.sin(Math.toRadians(dec));
		if(!super.moveVY()) {
			dec += 90;
		}

//		super.moveVY();
	}

	@Override
	public void make(float x, float y, float vx, float vy) {
		super.make(x, y, vx, vy);

		moveHeight = vy;
	}

	/**
	 * 向きを変える処理
	 */
	private void turn() {
		if(vx > 0) {
			animation = animeRight;
		} else {
			animation = animeLeft;
		}
	}

}
