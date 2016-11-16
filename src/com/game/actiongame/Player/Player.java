package com.game.actiongame.Player;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Boss.AbstractBoss;
import com.game.actiongame.Enemy.BaseEnemy;
import com.game.actiongame.EnemyBullet.EnemyBulletMark;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;
import com.game.actiongame.Utility.MyKeyListener;
import com.game.actiongame.Utility.OyaSheetEnum;
import com.game.actiongame.Utility.SheetTypeEnum32;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

import lombok.Getter;

/**
 * プレイヤークラス
 * 
 * @author moromorocco
 *
 */
public class Player extends AbstractSprite {
	public static final int HP_DIFF = 2;
	public static final float GRAVITY = 0.5f;
	public static final float HASHIGO_SPEED = 4.0f;

	private MediatorInterface mediator;
	// 攻撃クラス
	private PlayerAttack playerAttack;
	// シールドクラス
	private PlayerGuard playerGuard;

	private Image coinImg;
	private Image hpFrame;
	private Animation2 animeRight;
	private Animation2 animeLeft;
	private Animation2 animeDown;

	@Getter
	private DIR_TYPE dir;
	// 無敵時間
	private int mutekiTime;
	// 弾速度
	private float bulletSpeed = 4;
	// 盾や武器を描画するか
	private DRAW_TYPE drawStyle;
	// プレイヤーがどこに居るか。
	private MAP_TYPE mapType;

	// 盾を持っているか
	private boolean hasSield;
	// ダメージを受けて移動中かどうか
	private boolean isDamage;
	// 所持コイン数
	@Getter
	private int coins;

	// 20161029追加
	// 梯子に捕まっているか
//	private boolean isOnHashigo;
	
	//スクロール処理中か
	private boolean isScrolling;

	/** スクロール用オフセット */
	private float oldOffsetX;
	private float oldOffsetY;

	// プレイヤーの方向
	public enum DIR_TYPE {
		RIGHT, LEFT, DOWN,
	}

	// 武器や防具を描画するか
	public enum DRAW_TYPE {
		NOTHING, SIELD, ATTACK,
	}

	// プレイヤーがマップ上のどこにいるか。
	private enum MAP_TYPE {
		NOTHING(0.5f, 5, 4, 10), 
		WATER(0.2f, 2, 3, 6);

		final float gravity;
		final float maxGravity;
		final float moveSpeed;
		final int jumpSpeed;

		// コンストラクタ
		MAP_TYPE(float gravity, float maxGravity, float moveSpeed, int jumpSpeed) {
			this.gravity = gravity;
			this.maxGravity = maxGravity;
			this.moveSpeed = moveSpeed;
			this.jumpSpeed = jumpSpeed;
		}

		// 下移動速度取得
		public float getGravity() {
			return gravity;
		}

		// 最高落下速度
		public float getMaxGravity() {
			return maxGravity;
		}

		// 横移動速度取得
		public float getMoveSpeed() {
			return moveSpeed;
		}

		// ジャンプ速度
		public int getJumpSpeed() {
			return jumpSpeed;
		}
	}

	/**
	 * コンストラクタ
	 * 
	 * @param map
	 *            ゲームマップ
	 * @param mediator
	 *            メディエータ
	 */
	public Player(GameMap map, MediatorInterface mediator) {
		super(map);
		this.mediator = mediator;
		this.mediator.set(this);

		life = 200;
		mutekiTime = 0;
		coins = 0;

		drawStyle = DRAW_TYPE.NOTHING;
		mapType = MAP_TYPE.NOTHING;

		playerAttack = new PlayerAttack(map, this);
		playerGuard = new PlayerGuard(map, this);
		hasSield = false;
		isDamage = false;
		isScrolling = false;
	}

	@Override
	public void loadImage() {
//		SpriteSheet right = ImageLoaderUtility.getSheet(SheetType.PLAYER_RIGHT);
//		SpriteSheet left = ImageLoaderUtility.getSheet(SheetType.PLAYER_LEFT);
//		SpriteSheet down = ImageLoaderUtility.getSheet(SheetType.PLAYER_DOWN);

		SpriteSheet right = ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.PLAYER_RIGHT);
		SpriteSheet left = ImageLoaderUtility.loadKoSheet(SheetTypeEnum32.PLAYER_LEFT);
		SpriteSheet down = ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.PLAYER_DOWN);

		animeRight = new Animation2(right, 60);
		animeRight.setAutoUpdate(false);
		animeLeft = new Animation2(left, 60);
		animeLeft.setAutoUpdate(false);
		animeDown = new Animation2(down, 60);
		animeLeft.setAutoUpdate(false);

		dir = DIR_TYPE.RIGHT;
		animation = animeRight;

//		coinImg = ImageLoaderUtility.getImage(ImageType.ITEM_COIN);
//		hpFrame = ImageLoaderUtility.getImage(ImageType.PLAYER_FRAME);
		
		coinImg = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_COIN);
		hpFrame = ImageLoaderUtility.loadImage(ImageTypeEnum.PLAYER_FRAME);
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		// 共通部分の描画
		drawHpbar(g);

		// DEBUG 当たり判定の描画
		g.drawRect(x, y, getWidth(), getHeight());

		// 武器描画(左右攻撃)
		if (drawStyle == DRAW_TYPE.ATTACK) {
			playerAttack.draw(g, offSetX, offSetY);
		}

		// ダメージ中は点滅
		if (mutekiTime > 0) {
			if (mutekiTime % 2 == 0) {
				return;
			}
		} else if(mutekiTime < 30) {
			isDamage = false;
		}

		// プレイヤー描画
		super.draw(g, offSetX, offSetY);

		// 手前に武器描画(下攻撃時)
		playerAttack.drawFront(g, offSetX, offSetY);

		// 盾描画
		if (drawStyle == DRAW_TYPE.SIELD) {
			playerGuard.draw(g, offSetX, offSetY);
		}

		// DEBUG 座標の表示
		g.drawString("x:" + x, 20, 20);
		g.drawString("y:" + y, 20, 40);
		// マップID
		g.drawString("id:" + map.getMapId(x, y), 20, 60);

		// マップID
		g.drawString("vx:" + vx, 20, 80);
		g.drawString("vy:" + vy, 20, 100);

		oldOffsetX = offSetX;
		oldOffsetY = offSetY;
	}

	/**
	 * スクロール処理中の描画処理
	 * 
	 * @param g
	 *            graphics
	 */
	public void drawScroll(Graphics g) {
		drawHpbar(g);
		super.draw(g, oldOffsetX, oldOffsetY);
	}

	/**
	 * 通常・スクロール中表示する、共通部分
	 * 
	 * @param g
	 *            graphics
	 */
	private void drawHpbar(Graphics g) {
		// HPバー
		hpFrame.draw(0, 0);
		g.setColor(Color.green);
		g.fillRect(0 + HP_DIFF, 0 + HP_DIFF, life, 10);

		// 取得コイン数
		coinImg.draw(230, 0, 20, 20);
		g.setColor(Color.white);
		g.drawString("" + coins, 250, 0);
	}

	@Override
	public void update(int delta) {
		if (mutekiTime > 0) {
			mutekiTime--;
		}

		playerAttack.update(delta);
		playerGuard.update(delta);

		// キー入力チェック
		getMoveKey();
		// マップ移動チェック
		moveFloorMove();

		// ダメージ中は何もできない
		if (isDamage) {
		} else {
			//キー入力チェック
			checkMove(delta);
			checkAttack(delta);
		}

		// プレイヤーの移動
		doMove();
		
		//移動先のマップID確認
		getMapId();
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		boolean checkDamage = false;

		// 無敵中は何もしない
		if (mutekiTime > 0 || isDamage) {
			return;
		}

		// 敵弾
		if (sprite instanceof EnemyBulletMark) {
			damage(6);
			checkDamage = true;
		}

		// 敵
		if (sprite instanceof BaseEnemy) {
			damage(6);
			checkDamage = true;
		}

		// ボス
		if (sprite instanceof AbstractBoss) {
			damage(6);
			checkDamage = true;
		}

		// ダメージ処理の場合だけ処理
		if (checkDamage) {
			SoundManager.playSE(SoundEnum.PLAYER_DAMAGE);
			doDamageMove(sprite);
		}
	}

	/**
	 * ダメージ処理
	 * 
	 * @param damage
	 *            ダメージ量
	 */
	private void damage(int damage) {
		if (hasSield) {
			damage = damage / 2;
		}

		life -= damage;

		if (life < 0) {
			life = 0;
		}

		mutekiTime = 50;
	}

	/** シールド購入 */
	public void makeSield() {
		playerGuard.make();
		hasSield = true;

		if (drawStyle == DRAW_TYPE.NOTHING) {
			drawStyle = DRAW_TYPE.SIELD;
		}
	}

	/**
	 * キー入力チェック
	 */
	private void getMoveKey() {
		if (MyKeyListener.isPressRight()) {
			vx = mapType.getMoveSpeed();
		}

		if (MyKeyListener.isPressLeft()) {
			vx = -mapType.getMoveSpeed();
		}
	}

	/**
	 * マップ移動チェック処理
	 */
	private void moveFloorMove() {
		isScrolling = false;
		
		float newX = x + vx;
		if (newX < 0) {
			mediator.setScroll(GameMap.MOVE_DIR.LEFT);
			isScrolling = true;
		}
		if (newX > map.getWidth() - getWidth()) {
			mediator.setScroll(GameMap.MOVE_DIR.RIGHT);
			isScrolling = true;
		}

		// TODO 上下キーで移動先を設定
		float newY = y;
		
		if (MyKeyListener.isPressUp()) {
			newY = y - HASHIGO_SPEED;
			// 下キーなら下に移動
		} else if (MyKeyListener.isPressDown()) {
			newY = y + HASHIGO_SPEED;
		} else {
			newY = y + vy;
		}
		
		if (newY < 0) {
			mediator.setScroll(GameMap.MOVE_DIR.UP);
			isScrolling = true;
		} else if (newY > map.getHeight() - getHeight()) {
			mediator.setScroll(GameMap.MOVE_DIR.DOWN);
			isScrolling = true;
		}
	}

	/**
	 * 移動処理 左右キー、Xキーによるジャンプ処理を行う。
	 * 
	 * @param map map
	 * @param delta delta
	 */
	private void checkMove(int delta) {
		vx = 0;

		boolean moveFlag = false;
		if (MyKeyListener.isPressRight()) {
			moveFlag = true;
			vx = mapType.getMoveSpeed();

			if (dir != DIR_TYPE.RIGHT) {
				animation = animeRight;
				dir = DIR_TYPE.RIGHT;
			}
		}

		if (MyKeyListener.isPressLeft()) {
			moveFlag = true;
			vx = -mapType.getMoveSpeed();

			if (dir != DIR_TYPE.LEFT) {
				animation = animeLeft;
				dir = DIR_TYPE.LEFT;
			}
		}

		// ジャンプキー押下
		if (MyKeyListener.isPressX()) {
			doJump();
		}

		if (moveFlag) {
			animation.update(delta);
		}
	}

	/**
	 * 攻撃できるかチェック
	 * 
	 * @param delta
	 */
	private void checkAttack(int delta) {
		// 攻撃中はキャンセル
		if (playerAttack.getCoolTime() > 0) {
			return;
		}

		// 下キー押下かつ、ジャンプ中の場合、下向きに攻撃
		if (MyKeyListener.isPressDown() && !isOnGround) {
			dir = DIR_TYPE.DOWN;
			animation = animeDown;

			playerAttack.makeAttack(dir);
			setDrawStyle(Player.DRAW_TYPE.ATTACK);
		}

		// 攻撃キー押下時の処理
		if (MyKeyListener.isPressZ()) {
			playerAttack.makeAttack(dir);
			setDrawStyle(Player.DRAW_TYPE.ATTACK);
		}
	}

	/**
	 * プレイヤーの移動処理。 下に移動出来ない場合は、onGroundをtrueに変更 ダメージ中の場合、着地したらフラグをクリア
	 */
	private void doMove() {
		moveVX();

		if (!hashigoCheck()) {
			vy = (float) (vy + mapType.getGravity());
			if (vy > mapType.getMaxGravity()) {
				vy = mapType.getMaxGravity();
			}

			float newY = y + vy;

			Point tile = map.getTileCollision(this, x, newY);
			if (tile == null) {
				// 落下先が梯子なら、何もしない
				if (map.checkTileCollisionHashigo(this, x, newY) != null) {
					vy = 0;
					setOnGround(true);
				} else {
					y = newY;
				}
			} else {
				if (vy > 0) {
					y = GameMap.tilesToPixels(tile.y) - getHeight();
					setOnGround(true);
					vy = 0;
				} else if (vy < 0) {
					y = GameMap.tilesToPixels(tile.y + 1);
					vy = 0;
				}
			}
		}
	}

	/**
	 * 梯子で登れるかチェック
	 */
	private boolean hashigoCheck() {
		float newVy = 0;
		
		//マップ移動する場合は、チェックを行わない
		if(isScrolling) {
			return false;
		}

		// 上キーを押していたら上に移動
		if (MyKeyListener.isPressUp()) {
			newVy = -HASHIGO_SPEED;
			// 下キーなら下に移動
		} else if (MyKeyListener.isPressDown()) {
			newVy = HASHIGO_SPEED;
		}

		// キー入力時の移動先
		float newY = y + newVy;

		// 上下キー押下時、移動先に梯子があるかチェック
		Point hasigoPoint = map.checkTileCollisionHashigo(this, x, newY);
		if (hasigoPoint != null) {
			vy = 0;
			y = newY;

			return true;
		}

		return false;
	}

	/**
	 * プレイヤーのジャンプ処理
	 */
	public void doJump() {
		doJump(mapType.getJumpSpeed());
	}

	/**
	 * プレイヤージャンプ処理
	 * 
	 * @param speed
	 *            高さ
	 */
	private void doJump(int speed) {
		if (isOnGround || mapType == MAP_TYPE.WATER) {
			setVy(-speed);
			setOnGround(false);
		}
	}

	/**
	 * ダメージを受け飛ばされる処理 左右移動を不可能にする。
	 *
	 * @param sprite
	 *            対象のスプライト
	 */
	private void doDamageMove(AbstractSprite sprite) {
		setOnGround(true);

		doJump(5);

		if (getX() < sprite.getX()) {
			vx = -3;
		} else {
			vx = 3;
		}

		isDamage = true;
	}

	/**
	 * キー入力処理。 Cキーで遠距離攻撃を行う。
	 * 
	 * @param key key
	 * @param c char
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_C) {
			float vx = 0;
			float vy = 0;

			if (dir == DIR_TYPE.RIGHT) {
				vx = bulletSpeed;
			} else {
				vx = -bulletSpeed;
			}

			mediator.makePlayerShot(x, y + 12, vx, vy, PlayerBullet.TYPE.RED);
		}
	}

	/**
	 * コイン取得時の処理
	 * 
	 * @param num
	 *            取得枚数
	 */
	public void addCoins(int num) {
		coins += num;
	}

	/**
	 * x座標のセット
	 * 
	 * @param x
	 *            x座標
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * y座標のセット
	 * 
	 * @param y
	 *            y座標
	 */
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setOnGround(boolean onGround) {
		super.setOnGround(onGround);

		// 下向きの時だけ、右向きにする。
		if (dir == DIR_TYPE.DOWN) {
			animation = animeRight;
		}

		// 着地したので、下攻撃を止める
		playerAttack.playerOnGround();
		isDamage = false;
	}

	/**
	 * プレイヤーの武器描画スタイルをセット
	 * 
	 * @param drawStype
	 *            DRAW_STYLE
	 */
	private void setDrawStyle(DRAW_TYPE drawStype) {
		this.drawStyle = drawStype;
	}

	/** 攻撃終了後の描画スタイルをセット */
	public void setDrawStyleNormal() {
		if (hasSield) {
			drawStyle = DRAW_TYPE.SIELD;
		} else {
			drawStyle = DRAW_TYPE.NOTHING;
		}
	}

	/**
	 * 攻撃の取得
	 * 
	 * @return playerAttack プレイヤーの攻撃
	 */
	public PlayerAttack getAttack() {
		return playerAttack;
	}

	/**
	 * シールドの取得 盾を構えている場合のみ、盾を返す。 それ以外の場合は、nullを返す
	 * 
	 * @return playerGuard プレイヤーの盾スプライト
	 */
	public PlayerGuard getGuard() {
		if (drawStyle == DRAW_TYPE.SIELD) {
			return playerGuard;
		}

		return null;
	}

	/**
	 * 現在位置のマップID取得
	 */
	private void getMapId() {
		int id = map.getMapId(x, y);

		switch (id) {
		case GameMap.ID_NOTHING:
			mapType = MAP_TYPE.NOTHING;
			break;
		case GameMap.ID_WATER:
			mapType = MAP_TYPE.WATER;
			break;
		default:
			mapType = MAP_TYPE.NOTHING;
		}
	}
}
