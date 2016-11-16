package com.game.actiongame.Enemy;

import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.OyaSheetEnum;

/**
 * 罠オブジェクト
 * 倒せない
 * @author moromorocco
 *
 */
public class EnemyTypeTrap extends AbstractSprite implements CannotBreakMark {
	private final int MOVE_LENGTH = 100;
	private MediatorInterface mediator;
	private float dec;
	private float firstX;
	private float firstY;

	public EnemyTypeTrap(GameMap map, MediatorInterface mediator) {
		super(map);

		this.mediator = mediator;

		dec = 0;
	}

	@Override
	public void loadImage() {
		SpriteSheet sheet =ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.ENEMY_TRAP);
		
		animation = new Animation2(sheet, 60);
	}

	@Override
	public void update(int delta) {
		dec ++;

		if(dec >= 360) {
			dec = 0;
		}

		double rad = Math.toRadians(dec);
		x = (float)(firstX + MOVE_LENGTH * Math.cos(rad));
		y = (float)(firstY + MOVE_LENGTH * Math.sin(rad));
	}

	@Override
	public void make(float x, float y, float vx, float vy) {
		super.make(x, y, vx, vy);

		this.firstX = x;
		this.firstY = y;
	}
}
