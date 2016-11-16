package com.game.actiongame.Mapobject;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * 移動床クラス
 * @author moromorocco
 *
 */
public class MoveFloor extends AbstractSprite implements PlayerMoveable {
	/** 角度 */
	private int dec;
	/** 初期位置 */
	private float firstX;
	private float firstY;
	private int floorWidth;
	private int floorHeight;
	private int areaWidth;
	private int areaHeight;
	/** 移動量 */
	private float moveX;
	private float moveY;

	/**
	 * コンストラクタ
	 * @param map ゲームマップ
	 * @param mediator メディエータ
	 */
	public MoveFloor(GameMap map, MediatorInterface mediator) {
		super(map);
		mediator.set(this);
		dec = 0;
	}

	@Override
	public void loadImage() {
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(getIsUsing()) {
			g.setColor(Color.gray);
			g.fillRect(x + offSetX, y + offSetY, floorWidth, floorHeight);
		}
	}

	@Override
	public void update(int delta) {
		dec++;

		if(dec >= 360) dec = 0;

		double rad = Math.toRadians(dec);
		float newX = (float)(firstX + areaWidth * Math.cos(rad));
		float newY = (float)(firstY + areaHeight * Math.sin(rad));

		moveX = newX - getX();
		moveY = newY - getY();

		x = newX;
		y = newY;
	}

	//移動作成
	public void make(float x, float y, int floorWidth, int floorHeight, int moveWidth, int moveHeight, int dec) {
		this.firstX = x;
		this.firstY = y;
		this.floorWidth = floorWidth;
		this.floorHeight = floorHeight;
		this.areaWidth = moveWidth;
		this.areaHeight = moveHeight;
		this.dec = dec;

		super.make(x, y);
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		Rectangle rectangle = getRectangle();
		Player player = (Player)sprite;

		//上から当たった
		if(rectangle.contains(player.getCenterX(), player.getY() + player.getHeight())) {
			player.setOnGround(true);
			player.setY(getY() - player.getHeight());
			player.setX(player.getX() + moveX * 2);
			return;
		}

		//下から当たった
		if(rectangle.contains(player.getCenterX(), player.getY())) {
			player.setVy(Math.abs(moveY));
			player.setY(getY() + getHeight() + Math.abs(moveY) * 2);
			return;
		}

		//左から当たった
		if(rectangle.contains(player.getX() + player.getWidth(), player.getCenterY())) {
			player.setX(getX() - player.getWidth());
			return;
		}

		//右から当たった
		if(rectangle.contains(player.getX(), player.getCenterY())) {
			player.setX(getX() + getWidth());
			return;
		}
	}

	@Override
	public int getWidth() {
		return floorWidth;
	}

	@Override
	public int getHeight() {
		return floorHeight;
	}
}
