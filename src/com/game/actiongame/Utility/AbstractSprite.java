package com.game.actiongame.Utility;

import java.awt.Point;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Base.GameObjectInterface;
import com.game.actiongame.Base.Start;
import com.game.actiongame.Map.GameMap;

import lombok.Getter;
import lombok.Setter;

/**
 * スプライトの基底クラス
 * @author moromorocco
 *
 */
public abstract class AbstractSprite implements GameObjectInterface {
	protected final int WIDTH = Start.WIDTH;	//画面の横幅
	protected final int HEIGHT = Start.HEIGHT;	//画面の縦幅
	protected GameMap map;
	protected Animation2 animation;				//描画用アニメ(回転)
	protected Image img;						//描画用イメージ
	@Getter
	@Setter
	protected float x;					//x座標
	@Getter
	@Setter
	protected float y;					//y座標
	@Getter
	@Setter
	protected float vx;					//横移動
	@Getter
	@Setter
	protected float vy;					//縦移動
	@Getter
	protected int life;					//ライフ

	protected boolean isOnGround;			//着地しているか
	protected boolean isRotate = false;	//回転するか
	protected boolean isUsing = false;	//使用中か判定
	protected int rotateSpeed = 0;		//回転角度
	private DRAW_TYPE drawType;			//描画種類

	/** 列挙型 */
	private enum DRAW_TYPE {
		IMAGE,
		ANIME,
		NOTHING,
	}

	/**  */

	/**
	 * コンストラクタ
	 * @param map ゲームマップ
	 */
	public AbstractSprite(GameMap map) {
		this.map = map;

		init();
	}

	/** 初期化 */
	public void init() {
		isUsing = false;
		isRotate = false;
		drawType = DRAW_TYPE.NOTHING;
		vx = 0;
		vy = 0;
		isOnGround = false;
	}

	/**
	 * スプライトの作成
	 * @param x x座標
	 * @param y y座標
	 */
	public void make(float x, float y) {
		make(x, y, 0, 0);
	}

	/**
	 * スプライトの作成
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 */
	public void make(float x, float y, float vx, float vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;

		isUsing = true;

		loadImage();
		setDrawType();
	}

	/** 画像の読込 */
	public abstract void loadImage();

	/** 更新 */
	public abstract void update(int delta);

	/**
	 * 描画処理
	 * @param g graphics
	 * @param offSetX オフセットX
	 * @param offSetY オフセットY
	 */
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(isUsing) {
			switch(drawType) {
				case ANIME:
					animation.draw(x + offSetX, y + offSetY);
					break;
				case IMAGE:
					img.draw(x + offSetX, y + offSetY);
					break;
				case NOTHING:
					break;
				default:
					break;
			}
		}

//		g.setColor(Color.red);
//		g.drawRect(x + offSetX, y + offSetY, getWidth(), getHeight());
	}

	/** 読み込んだ画像から、描画タイプを自動設定 */
	public void setDrawType() {
		if(animation != null) {
			drawType = DRAW_TYPE.ANIME;
		} else if(img != null) {
			drawType = DRAW_TYPE.IMAGE;
		} else {
			drawType = DRAW_TYPE.NOTHING;
		}
	}

	/**
	 * スプライト同士ぶつかった時の処理
	 * @param sprite ぶつかった相手のスプライト
	 */
	public void hitSprite(AbstractSprite sprite) {
	}

	/**
	 * 横移動できるか
	 * @return 移動可否
	 */
	public boolean moveVX() {
		float newX = x + vx;

		Point tile = map.getTileCollision(this, newX, y);
		if (tile == null) {
			x = x + vx;
			return true;
		} else {
			if (vx > 0) {
				x = GameMap.tilesToPixels(tile.x) - getWidth();
			} else if (vx < 0) {
				x = GameMap.tilesToPixels(tile.x + 1);
			}
			return false;
		}
	}

	/**
	 * 縦方向に移動可能か
	 * @return 移動可否
	 */
	public boolean moveVY() {
		float newY = y + vy;
		Point tile = map.getTileCollision(this, x, newY);
		if (tile == null) {
			y = y + vy;
			return true;
		} else {
			if (vy > 0) {
				y = GameMap.tilesToPixels(tile.y) - getHeight();
				setOnGround(true);
				vy = 0;
			} else if (vy < 0) {
				y = GameMap.tilesToPixels(tile.y + 1);
				vy = 0;
			}
			return false;
		}
	}

	/**
	 * 着地
	 * @param onGround boolean
	 */
	public void setOnGround(boolean onGround) {
		this.isOnGround = onGround;
	}

	/**
	 * スプライトの横幅を取得
	 * @return width 横幅
	 */
	public int getWidth() {
		int width = 0;
		switch(drawType) {
			case ANIME:
				width = animation.getWidth();
				break;
			case IMAGE:
				width = img.getWidth();
				break;
			case NOTHING:
				break;
			default:
				break;
		}
		return width;
	}

	/**
	 * スプライトの高さ取得
	 * @return height 高さ
	 */
	public int getHeight() {
		int height = 0;
		switch(drawType) {
			case ANIME:
				height = animation.getHeight();
				break;
			case IMAGE:
				height = img.getHeight();
				break;
			case NOTHING:
				break;
			default:
				break;
		}
		return height;
	}

	/**
	 * スプライトのX座標の中心を取得
	 * @return cneterX 横中心
	 */
	public float getCenterX() {
		return  x + getWidth() / 2;
	}

	/**
	 * スプライトのY座標の中心を取得
	 * @return centerY 縦中心
	 */
	public float getCenterY() {
		return  y + getHeight() / 2;
	}

	/**
	 * 短形の当たり判定の大きさ
	 * @return rectangle 短形
	 */
	public Rectangle getRectangle() {
		return new Rectangle(x, y, getWidth(), getHeight());
	}

	/**
	 * 生きているかチェック
	 * @return isUsing 生きているTrue/False
	 */
	public boolean getIsUsing() {
		return isUsing;
	}

	/**
	 * 回転開始
	 * @param spd 回転角度
	 */
	public void setRotate(int spd) {
		isRotate = true;
		rotateSpeed = spd;
	}

	/** 回転停止 */
	public void stopRotate() {
		isRotate = false;
	}

	/** 回転する */
	protected void rotate() {
		if(isRotate && img != null) {
			img.rotate(rotateSpeed);
		}
	}

	/** スプライトを削除。更新・描画しないようにする。 */
	public void delete() {
		isUsing = false;
	}

}
