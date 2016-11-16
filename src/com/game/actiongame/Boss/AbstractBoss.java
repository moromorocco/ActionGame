package com.game.actiongame.Boss;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Base.Start;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Player.PlayerAttack;
import com.game.actiongame.Player.PlayerBullet;
import com.game.actiongame.ShotPattern.MakeShotInterface;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

/**
 * ボス基底クラス
 * @author moromorocco
 */
public abstract class AbstractBoss extends AbstractSprite{
	/** 画面横幅 */
	public static final int WIDTH = Start.WIDTH;
	/** 画面高さ */
	public static final int HEIGHT = Start.HEIGHT;
	/** フレームの表示位置 */
	public static final int FRAME_Y = 30;
	/** 描画エリアの幅 */
	public static final int FRAME_HEIGHT = 350;
	/** 枠の幅 */
	public static final int FRAME_DIFF = 2;
	/** 移動速度 */
	private static final int DEFAULT_ANGLE_SPEED = 2;
	/** メディエータ */
	protected MediatorInterface mediator;
	/** マネージャ */
	protected BossManagerInterface bossManager;
	/** 攻撃タイプ */
	protected MakeShotInterface shotType;
	/** ビットリスト */
	protected LinkedList<AbstractSprite> subMobList;
	/** ライフの背景画像 */
	private Image frameLife;
	/** 最大ライフ */
	protected float maxLife;
	/** 合計ライフ */
	protected float life;
	/** ライフゲージ表示処理判定フラグ */
	private boolean isReady;
	/** 移動前の座標 */
	private float prevX;
	private float prevY;
	/** ライフの描画位置 */
	private int frameX;
	private int frameY;
	private int lifeHeight;
	/** 移動にかける時間 */
	private int angle;
	private int angleSpeed;
	/** 移動中か */
	private boolean isMoving;
	/** 物理攻撃による無敵時間。attackMutekiTime > 0 で無敵*/
	private int attackMutekiTime;

//	/**
//	 * コンストラクタ
//	 * @param map マップ
//	 * @param mediator メディエータ
//	 */
//	public AbstractBoss(GameMap map, MediatorInterface mediator, BossManagerInterface bossManager) {
//		super(map);
//		this.mediator = mediator;
//		this.bossManager = bossManager;
//
//		subMobList = new LinkedList<AbstractSprite>();
//
////		frameLife = ImageLoaderUtility.getImage(ImageType.BOSS_HP_FRAME);
//		frameLife = ImageLoaderUtility2.loadImage(ImageTypeEnum.BOSS_HP_FRAME);
//
//		init();
//	}

	
	public AbstractBoss(GameMap map) {
		super(map);
//		this.mediator = mediator;
//		this.bossManager = bossManager;

		subMobList = new LinkedList<AbstractSprite>();
		frameLife = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS_HP_FRAME);

		init();
	}
	
	public void setMediator(MediatorInterface mediator) {
		this.mediator = mediator;
	}

	public void setManager(BossManagerInterface bossManager) {
		this.bossManager = bossManager;
	}

	
	/** 初期化 */
	public void init() {
		isReady = false;
		frameX = WIDTH + 20;
		frameY = FRAME_Y;
		lifeHeight = 0;

		angle = 0;
		isMoving = false;
	}

	@Override
	public void update(int delta) {
		if(super.getIsUsing() == false) {
			return;
		}

		//無敵時間の減少
		if(attackMutekiTime > 0) {
			attackMutekiTime--;
		}

		//通常の移動
		x += vx;
		y += vy;

		//リストの更新
		for(AbstractSprite sprite : subMobList) {
			sprite.update(delta);
		}

		//指定した座標に移動中
		if(isMoving) {
			angle += angleSpeed;

			double temp = Math.sin(Math.toRadians(angle));
			x = (float)(prevX + temp * vx);
			y = (float)(prevY + temp * vy);

			if(angle >= 90) {
				stop();
				isMoving = false;
			}
		}
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(!super.getIsUsing()) {
			return;
		}

		//ボスの描画
		super.draw(g, offSetX, offSetY);

		//リストの描画
		for(AbstractSprite sprite : subMobList) {
			sprite.draw(g, offSetX, offSetY);
		}

		//ライフゲージフレームの描画
		g.drawImage(frameLife, frameX, frameY);
		g.setColor(Color.red);

		//登場時のアニメーション処理
		if(isReady == false) {
			//フレームを画面に出現させる
			if(frameX > WIDTH - frameLife.getWidth()) {
				frameX--;
			} else {
				//ライフゲージ出現
				lifeHeight += 5;
			}

			if(lifeHeight >= FRAME_HEIGHT) {
				lifeHeight = FRAME_HEIGHT;
				isReady = true;
			}

			g.fillRect(frameX + FRAME_DIFF,
					frameY + FRAME_DIFF + FRAME_HEIGHT - lifeHeight,
					8,
					lifeHeight);
		} else {
			//ライフの表示
			int lifeHeight = (int) (life / maxLife * FRAME_HEIGHT);
			g.fillRect(frameX + FRAME_DIFF,
					frameY + FRAME_DIFF + FRAME_HEIGHT - lifeHeight,
					8,
					lifeHeight);
		}
	}

	/**
	 * ボスとの当たり判定
	 * @param sprite 対象スプライト
	 */
	public abstract void checkHit(AbstractSprite sprite);

	/**
	 * ボスとスプライトリストの当たり判定
	 * @param manager マネージャ基底リスト
	 */
	public void checkHit(AbstractManager manager) {
		for(AbstractSprite sprite : manager.getList()) {
			if(!sprite.getIsUsing()) {
				continue;
			}

			checkHit(sprite);
		}
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		if(sprite instanceof Player) {
			return;
		}

		//通常攻撃
		if(sprite instanceof PlayerAttack) {
			if(attackMutekiTime == 0) {
				life = life - 5;
				attackMutekiTime = 30;
			}
		}

		//魔法攻撃
		if(sprite instanceof PlayerBullet) {
			life = life - 1;
		}

		if(life < 0) {
			life = 0;
		}
	}

	/**
	 * リストをクリア
	 */
	public void clearList() {
		subMobList.clear();
	}

	/**
	 * 指定した場所に移動
	 * @param newX 移動先のX座標
	 * @param newY 移動先のY座標
	 */
	protected void move(float newX, float newY) {
		move(newX, newY, DEFAULT_ANGLE_SPEED);
	}

	/**
	 * 指定した場所に移動するためのスピードを計算
	 * @param newX 移動先のX座標
	 * @param newY 移動先のY座標
	 * @param angleSpeed 移動速度
	 */
	protected void move(float newX, float newY, int angleSpeed) {
		prevX = x;
		prevY = y;
		this.angleSpeed = angleSpeed;

		vx = newX - x;
		vy = newY - y;

		angle = 0;
		isMoving = true;
	}

	/** 停止 */
	protected void stop() {
		prevX = x;
		prevY = y;

		vx = 0;
		vy = 0;
	}

	/**
	 * 移動中かどうかチェック
	 * @return isMoving 移動
	 */
	protected boolean isMoving() {
		return isMoving;
	}
}
