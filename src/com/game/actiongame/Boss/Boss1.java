package com.game.actiongame.Boss;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.EnemyBullet.EnemyBulletMark;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.ShotPattern.MakeShotInterface;
import com.game.actiongame.ShotPattern.ShotBoss1Baramaki;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.GetRandomUtility;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

/**
 * ボス１クラス(トカゲ？)
 * @author moromorocco
 *
 */
public class Boss1 extends AbstractBoss {
	private static final int JUMP = 2;
	/** 落下する岩の最大数 */
	private static final int ROCK_NUM = 6;
	/** ボスの当たり判定の大きさ */
	private static final int BOSS_X = 0;
	private static final int BOSS_Y = 58;
	private static final int BOSS_WIDTH = 130;
	private static final int BOSS_HEIGHT = 135;
	/** 攻撃位置を修正するための変数 */
	private static final int ATTACK_X = -120;
	private static final int ATTACK_Y = 83;
	/** ボスの攻撃 */
	private MakeShotInterface bossShotBaramaki;
	/** 落下する岩 */
	private Rock[] rock;
	/** ボス画像 */
	private Image bossNormal;
	private Image bossShot;
	private Image bossAttack;
	private Image bossWepon;
	private STATE state;
	private int frameCount;

	/**
	 * 行動パターン列挙型
	 * @author moromorocco
	 *
	 */
	private enum STATE {
		ATTACK(40),
		STOP1(50),
		SHOT(60),
		STOP2(50),
		JUMP(120),
		STOP3(50),
		DEAD(99);

		private final int changeTime;

		STATE(int changeTime) {
			this.changeTime = changeTime;
		}

		public int getChangeTime() {
			return changeTime;
		}
	}

//	/**
//	 * コンストラクタ
//	 * @param map マップ
//	 * @param mediator メディエータ
//	 * @param manager ボスマネージャ
//	 */
//	public Boss1(GameMap map,MediatorInterface mediator, BossManagerInterface manager) {
//		super(map, mediator, manager);

	/**
	 * コンストラクタ
	 * @param map マップ
	 */
	public Boss1(GameMap map) {
		super(map);
	
		bossShotBaramaki = new ShotBoss1Baramaki();

		state = STATE.STOP1;

		//岩の初期化
		rock = new Rock[ROCK_NUM];
		for(int i = 0; i < ROCK_NUM; i++) {
			rock[i] = new Rock(map);
			subMobList.add(rock[i]);
		}

		maxLife = 10;
		life = maxLife;
		frameCount = 0;
	}

	@Override
	public void loadImage() {
		bossNormal = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS1_NORMAL);
		bossShot = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS1_SHOT);
		bossAttack = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS1_ATTACK);
		bossWepon = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS1_WEPON);
		bossWepon.setCenterOfRotation(bossWepon.getWidth() - 13, 13);

		img = bossNormal;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(!super.getIsUsing()) {
			return;
		}

		super.draw(g, offSetX, offSetY);

		if(state == STATE.ATTACK) {
			bossWepon.draw(x + offSetX + ATTACK_X, y + offSetY + ATTACK_Y);
			g.drawRect(x + offSetX + ATTACK_X, y + offSetY + ATTACK_Y + frameCount * 8, 130, 50);
		}

//		g.drawRect(x + BOSS_X, y + offSetY + BOSS_Y, BOSS_WIDTH, BOSS_HEIGHT);
	}

	@Override
	public void update(int delta) {
		if(!super.getIsUsing()) {
			return;
		}

		super.update(delta);
		frameCount++;

		switch(state) {
			case ATTACK:
				patternAttack();
				break;
			case STOP1:
				break;
			case SHOT:
				patternShot();
				break;
			case STOP2:
				break;
			case JUMP:
				patternJump();
				break;
			case STOP3:
				break;
			case DEAD:
				if(frameCount % 10 == 0) {
					SoundManager.playSE(SoundEnum.BOSS_EXPLOTION);

					int randX = GetRandomUtility.getRand(100) - 50;
					int randY = GetRandomUtility.getRand(100) - 50;
					mediator.makeEffect(x + randX, y + randY, EFFECT_TYPE.BOSS_DEAD);
				}

				if(frameCount > 60) {
					bossManager.bossDead();
					delete();
				}
				break;
			default:
				break;
		}

		if(frameCount == state.getChangeTime()) {
			changeState();
			frameCount = 0;
		}
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(
				x + BOSS_X,
				y + BOSS_Y,
				BOSS_WIDTH,
				BOSS_HEIGHT);
	}

	@Override
	public void checkHit(AbstractSprite sprite) {
		if(state == STATE.DEAD) {
			//倒した時は何もしない
			return;
		}

		if(getRectangle().intersects(sprite.getRectangle())) {
			//当たった時の処理は、各オブジェクトで実装
			hitSprite(sprite);
			sprite.hitSprite(this);
		}

		if(sprite instanceof Player) {
			//スプライトリストの当たり判定
			for(AbstractSprite subSprite : subMobList) {
				if(!subSprite.getIsUsing()) {
					continue;
				}

				if(subSprite.getRectangle().intersects(sprite.getRectangle())) {
					//当たった時の処理は、各オブジェクトで実装
					sprite.hitSprite(subSprite);
				}
			}

			if(state == STATE.ATTACK) {
				Rectangle attack = new Rectangle(x + ATTACK_X, y + ATTACK_Y + frameCount * 8, 130, 50);
				if(attack.intersects(sprite.getRectangle())) {
					sprite.hitSprite(this);
				}
			}
		}
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		super.hitSprite(sprite);

		//ライフが0以下になった時の処理
		if(life <= 0) {
			state = STATE.DEAD;
			frameCount = 0;
		}
	}

	/** 行動パターンの変更と、画像の変更 */
	private void changeState() {
		switch(state) {
			case ATTACK:
				state = STATE.STOP1;
				img = bossNormal;
				break;
			case STOP1:
				state = STATE.SHOT;
				img = bossShot;
				break;
			case SHOT:
				state = STATE.STOP2;
				img = bossNormal;
				break;
			case STOP2:
				state = STATE.JUMP;
				img = bossNormal;
				break;
			case JUMP:
				state = STATE.STOP3;
				img = bossNormal;
				break;
			case STOP3:
				state = STATE.ATTACK;
				img = bossAttack;
				bossWepon.setRotation(30);
				break;
			default:
				break;
		}
	}

	/** 攻撃 */
	private void patternAttack() {
		if(5 <= frameCount && frameCount <= 25) {
			bossWepon.rotate(-5);
		}
	}

	/** ショットの作成 */
	private void patternShot() {
		if(10 <= frameCount && frameCount < 30) {
			if(frameCount % 2 == 0) {
				return;
			}

			mediator.makeEnemyShot(bossShotBaramaki,x, y);
		}
	}

	/** ジャンプ→岩落下 */
	private void patternJump() {
		if(frameCount < 20) {
			y -= JUMP;
		}
		if(20 <= frameCount && frameCount < 40) {
			y += JUMP;
		}

		if(frameCount == 40) {
			for(int i = 0; i < ROCK_NUM; i++) {
				int randX = 30 + GetRandomUtility.getRand(330);
				rock[i].make(randX, 60);
			}
		}
	}
}

/**
 * 落下してくる岩クラス
 * @author moromorocco
 *
 */
class Rock extends AbstractSprite implements EnemyBulletMark {
	private final float SPEED = 0.2f;
	private int frameCount;

	/**
	 * コンストラクタ
	 * @param map マップ
	 */
	public Rock(GameMap map) {
		super(map);
	}

	@Override
	public void loadImage() {
		img = ImageLoaderUtility.loadImage(ImageTypeEnum.MAP_ROCK);
	}

	@Override
	public void update(int delta) {
		frameCount++;

		x += vx;
		y += vy;

		if(frameCount >= 30) {
			vy += SPEED;
		}

		//画面外に出たら消す
		if(y > 500) {
			super.delete();
		}
	}

	@Override
	public void make(float x, float y) {
		super.make(x, y);

		frameCount = 0;
	}
}

