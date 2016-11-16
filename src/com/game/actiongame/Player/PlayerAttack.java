package com.game.actiongame.Player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

import lombok.Getter;

/**
 * プレイヤーの物理攻撃クラス
 * @author moromorocco
 *
 */
public class PlayerAttack extends AbstractSprite {
	private static final int COOL_TIME = 30;
	private Image imgDown;
	private Player player;
	private DirType dirType;
	@Getter
	private int coolTime;

	private enum DirType {
		NOTHING,
		RIGHT,
		LEFT,
		DOWN,
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param player プレイヤー
	 */
	public PlayerAttack(GameMap map, Player player) {
		super(map);
		this.player = player;

		dirType = DirType.NOTHING;

		super.make(0, 0);
		delete();
	}

	@Override
	public void loadImage() {
		if(img == null) {
//			img = ImageLoaderUtility.getImage(ImageType.ITEM_SOWRD);
			img = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_SOWRD);
		}

		//下攻撃時の画像
//		imgDown = ImageLoaderUtility.getImage(ImageType.PLAYER_DOWN_ATTACK);
		imgDown = ImageLoaderUtility.loadImage(ImageTypeEnum.PLAYER_DOWN_ATTACK);
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		//プレイヤーの前に描画する処理。右、左の時だけ描画
		switch(dirType) {
			case RIGHT:
				super.draw(g, offSetX - 24, offSetY - 4);
				break;
			case LEFT:
				super.draw(g, offSetX, offSetY - 4);
				break;
			case DOWN:
				break;
			case NOTHING:
				break;
			default:
				break;
		}

//		g.drawRect(x, y, getWidth(), getHeight());

		Rectangle rect = getRectangle();
		if(rect != null) {
			g.drawRect(rect.getX(), rect.getY(), getWidth(), getHeight());
		}
	}

	/**
	 * プレイヤーより手前に描画する処理
	 * @param g graphics
	 * @param offSetX オフセットX
	 * @param offSetY オフセットY
	 */
	public void drawFront(Graphics g, float offSetX, float offSetY) {
		//プレイヤーの後で描画する処理。
		if(dirType == DirType.DOWN) {
			g.drawImage(imgDown, x + offSetX, y + offSetY + 6);
		}
	}

	@Override
	public void update(int delta) {
		y = player.getY();

		if(coolTime > 0) {
			coolTime--;
		}

		switch(dirType) {
			case RIGHT:
				x = player.getX() + player.getWidth() - getWidth()/2;
				super.rotate();
				break;
			case LEFT:
				x = player.getX() - getWidth() / 2;
				super.rotate();
				break;
			case DOWN:
				x = player.getX();
				break;
			default:
				break;
		}

		if(coolTime == 0) {
			super.delete();

			player.setDrawStyleNormal();
		}
	}

	/** 攻撃を行う */
	public void makeAttack(Player.DIR_TYPE type) {
		//描画位置のセット
		float newX = player.getX();
		float newY = player.getY();

		//プレイヤーの向いている方向で、描画位置の調節。
		switch(type) {
			case RIGHT:
				this.dirType = DirType.RIGHT;

				coolTime = COOL_TIME;
				super.img.setCenterOfRotation(img.getWidth(), img.getHeight());
				super.img.setRotation(90);
				super.setRotate(3);
				break;
			case LEFT:
				this.dirType = DirType.LEFT;
				coolTime = COOL_TIME;
				newX = player.getX() - getWidth() / 2;
				super.img.setCenterOfRotation(img.getWidth(), img.getHeight());
				super.img.setRotation(0);
				super.setRotate(-3);
				break;
			case DOWN:
				this.dirType = DirType.DOWN;
				newY = y + 10;
				break;
			default:
				return;
		}

		this.x = newX;
		this.y = newY;
		this.isUsing = true;

//		player.setDrawStyle(Player.DRAW_STYPE.ATTACK);
	}

	@Override
	public Rectangle getRectangle() {
		if(dirType == DirType.DOWN) {
			return new Rectangle(x + (player.getWidth() - getWidth())/2, y + getHeight() - getWidth()/2, getWidth(), getHeight());
		}

		return super.getRectangle();
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		//下向きの攻撃が当たったらジャンプする。
		if(dirType == DirType.DOWN) {
			player.setOnGround(true);
			player.doJump();
		}
	}

	/**
	 * 着地した時の処理
	 * 下攻撃時の場合、NOTHINGにする。
	 */
	public void playerOnGround() {
		if(dirType == DirType.DOWN) {
			dirType = DirType.NOTHING;
		}
	}
}
