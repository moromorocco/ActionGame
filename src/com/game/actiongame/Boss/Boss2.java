package com.game.actiongame.Boss;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.EnemyBullet.EnemyBulletMark;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.ShotPattern.MakeShotInterface;
import com.game.actiongame.ShotPattern.ShotBoss2Left;
import com.game.actiongame.ShotPattern.ShotBoss2Rain;
import com.game.actiongame.ShotPattern.ShotBoss2Wave;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.GetRandomUtility;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;
import com.game.actiongame.Utility.OyaSheetEnum;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

/**
 * 水中のボス(鮫？)
 * @author moromorocco
 *
 */
public class Boss2 extends AbstractBoss {
	/** ボスの当たり判定の大きさ */
	private static final int BOSS_X_RIGHT = 50;
	private static final int BOSS_Y_RIGHT = 50;
	private static final int BOSS_X_LEFT = 20;
	private static final int BOSS_Y_LEFT = 50;
	private static final int BOSS_WIDTH = 130;
	private static final int BOSS_HEIGHT = 80;
	/** 触手の数 */
	private static final int SHOKUSHU_NUM = 3;
	private Shokushu[] shokushu;
	
	/** ボス画像 */
	private Image bossRight;
	private Image bossLeft;
	/** ボスの攻撃 */
	private MakeShotInterface bossWave;
	private MakeShotInterface bossShotRain;
	private MakeShotInterface bossShotLeft;
	private DIR dir;
	private STATE state;
	private int frameCount;

	/**
	 * ボスの向き
	 * @author moromorocco
	 *
	 */
	private enum DIR {
		RIGHT,
		LEFT;
	}

	/**
	 * 行動パターン列挙型
	 * @author moromorocco
	 *
	 */
	private enum STATE {
		STOP1(60) {
			@Override
			public STATE getNextState() {
				return MOVE_ROUND;
			}
		},
//		MOVE_ROUND(241),	//TODO コメント外す
		MOVE_ROUND(10) {
			@Override
			public STATE getNextState() {
				return ATTACK_WAVE;
			}
		},
		ATTACK_WAVE(10) {
			@Override
			public STATE getNextState() {
				return SHOT_RAIN;
			}
		},
//		SHOT_RAIN(150),	//TODO コメント外す
//		SHOT_LEFT(180),	//TODO コメント外す
		SHOT_RAIN(10) {
			@Override
			public STATE getNextState() {
				return SHOT_LEFT;
			}
		},
		SHOT_LEFT(10) {
			@Override
			public STATE getNextState() {
				return ATTACK_SHOKUSHU;
			}
		},
		ATTACK_SHOKUSHU(60) {
			@Override
			public STATE getNextState() {
				return MOVE_ROUND;
			}
		},
		DEAD(999) {
			@Override
			public STATE getNextState() {
				return DEAD;
			}
		};

		private final int changeTime;
		public abstract STATE getNextState();

		STATE(int changeTime) {
			this.changeTime = changeTime;
		}

		public int getChangeTime() {
			return changeTime;
		}
		
//		public STATE getNextState() {
//			return state;
//		}
	}

//	/**
//	 * コンストラクタ
//	 * @param map ゲームマップ
//	 * @param mediator メディエータ
//	 * @param bossmanager ボスマネージャ
//	 */
//	public Boss2(GameMap map, MediatorInterface mediator, BossManagerInterface bossmanager) {
//		super(map, mediator, bossmanager);

	/**
	 * コンストラクタ
	 * @param map マップ
	 */
	public Boss2(GameMap map) {
		super(map);
	
		//ボスショット初期化
		bossWave = new ShotBoss2Wave();
		bossShotRain = new ShotBoss2Rain();
		bossShotLeft = new ShotBoss2Left();

		dir = DIR.LEFT;
		state = STATE.STOP1;

		maxLife = 10;
		life = maxLife;
		frameCount = 0;

		//触手の初期化
		shokushu = new Shokushu[SHOKUSHU_NUM];
		for(int i = 0; i < SHOKUSHU_NUM; i++) {
			shokushu[i] = new Shokushu(map);
			subMobList.add(shokushu[i]);
		}
	}

	@Override
	public void loadImage() {
		bossRight = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS2_RIGHT);
		bossLeft = ImageLoaderUtility.loadImage(ImageTypeEnum.BOSS2_LEFT);
		
		img = bossLeft;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(!super.getIsUsing()) {
			return;
		}

		super.draw(g, offSetX, offSetY);

		g.drawRect(x + offSetX, y, getWidth(), getHeight());


		Rectangle rect = getRectangle();
		g.drawRect(rect.getX() + offSetX, rect.getY() + offSetY,
				rect.getWidth(), rect.getHeight());
	}

	@Override
	public void update(int delta) {
		if(!super.getIsUsing()) {
			return;
		}

		super.update(delta);

		//攻撃パターン
		frameCount++;

		switch(state) {
			case STOP1:
				break;
			case MOVE_ROUND:
//				moveRound();	
				break;
			case ATTACK_WAVE:
				attackWave();
				break;
			case SHOT_RAIN:
				makeShotRain();
				break;
			case SHOT_LEFT:
				makeShotStraight();
				break;
			case ATTACK_SHOKUSHU:
				makeShokushu();
				break;
			case DEAD:
				doDead();
				break;
			default:
				break;
		}

		//攻撃パターンを変えるタイミングかチェック
		if(frameCount == state.getChangeTime()) {
			changeState();
			frameCount = 0;
		}
	}

	@Override
	public void checkHit(AbstractSprite sprite) {
		if(state == STATE.DEAD) {
			//倒した時は何もしない
			return;
		}

		//ボスとの接触ダメージ
		if(getRectangle().intersects(sprite.getRectangle())) {
			//当たった時の処理は、各オブジェクトで実装
			hitSprite(sprite);
			sprite.hitSprite(this);
		}
		
		//プレイヤーへの攻撃があたったか判定
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
		}
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		//ボスへのダメージ
		super.hitSprite(sprite);

		//ライフが0以下になった時の処理
		if(life <= 0) {
			state = STATE.DEAD;
			frameCount = 0;
		}
	}

	@Override
	public Rectangle getRectangle() {
		float newX = x;
		float newY = y;

		if(dir == DIR.RIGHT) {
			newX = x + BOSS_X_RIGHT;
			newY = y + BOSS_Y_RIGHT;
		} else {
			newX = x + BOSS_X_LEFT;
			newY = y + BOSS_Y_LEFT;
		}

		return new Rectangle(
				newX,
				newY,
				BOSS_WIDTH,
				BOSS_HEIGHT);
	}

	/** 行動パターンの変更と、画像の変更 */
	private void changeState() {
		switch(state) {
			case STOP1:
				state = STATE.MOVE_ROUND;
				break;
			case MOVE_ROUND:
				state = STATE.ATTACK_WAVE;
				break;
			case ATTACK_WAVE:
				state = STATE.SHOT_RAIN;
				break;
			case SHOT_RAIN:
				state = STATE.SHOT_LEFT;
				break;
			case SHOT_LEFT:
				state = STATE.ATTACK_SHOKUSHU;
				break;
			case ATTACK_SHOKUSHU:
				state = STATE.STOP1;
				break;
				
			default:
				break;
		}
	}

	/**
	 * ぐるりと画面をまわる。
	 */
	private void moveRound() {
		//水中から陸上へ
		if(frameCount < 60) {
			super.move(440, 120);
		}

		//左から右へ
		if(60 <= frameCount && frameCount < 120) {
			super.move(30, 120);
		}

		if(frameCount == 120) {
			turnBoss(DIR.RIGHT);
		}

		//陸上から水中へ
		if(120 <= frameCount && frameCount < 180) {
			super.move(30, 320);
		}

		//左から右へ
		if(180 <= frameCount && frameCount < 240) {
			super.move(440, 320);
		}

		if(frameCount == 240) {
			turnBoss(DIR.LEFT);
		}
	}
	
	
	/**
	 * サインWAVEの玉攻撃
	 */
	private void attackWave() {
		if(10 <= frameCount && frameCount <= 80) {
			if(frameCount % 10 == 0) {
				int shotX = (int)(this.getX());
				int shotY = (int)(this.getCenterY());

				mediator.makeEnemyShot(bossWave, shotX, shotY);
			}
		}
	}

	/**
	 * 右から左に全体的にショットを作成
	 */
	private void makeShotRain() {
		if(10 <= frameCount && frameCount <= 120) {
			if(frameCount % 10 == 0) {
				int shotX = 480 - frameCount / 10 * 32;

				mediator.makeEnemyShot(bossShotRain, shotX, 0);
			}
		}
	}

	/**
	 * 左方向にショットを作成
	 */
	private void makeShotStraight() {
		if(30 <= frameCount && frameCount <= 150) {
			if(frameCount % 5 == 0) {
				mediator.makeEnemyShot(bossShotLeft, x - 30, getCenterY());
			}
		}
	}

	/**
	 * 地面から触手を生やす
	 */
	private void makeShokushu() {
		if(frameCount == 30) {
			for(int i = 0; i < SHOKUSHU_NUM; i++) {
				int randX = 30 + GetRandomUtility.getRand(330);
				int y = HEIGHT;
				shokushu[i].make(randX, y);
			}
		}
	}	
	
	/**
	 * 向きを変える処理
	 * @param dir 変更後の向き
	 */
	private void turnBoss(DIR dir) {
		this.dir = dir;

		if(dir == DIR.RIGHT) {
			img = bossRight;
		} else {
			img = bossLeft;
		}
	}
	
	private void doDead() {
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
	}
}


/**
 * 下から触手が伸びてくる
 * @author moromorocco
 *
 */
class Shokushu extends AbstractSprite implements EnemyBulletMark {
	private final int DIFF_X = 80;
	private final int DIFF_Y = 50;
	private final int MOVE_Y_SPEED = 5;

	//状態
	private STATE state;
	//フレームカウント
	private int frameCount;
	//ダメージ判定の有無
	private boolean isHit;
	
	private enum STATE {
		YOKOKU(30),
		WAIT(25),
		ATTACK(40);
		
		final int time;
		
		STATE(int time) {
			this.time = time;
		}
		
		public int getTime() {
			return time;
		}
	}
	
	/**
	 * コンストラクタ
	 * @param map マップ
	 */
	public Shokushu(GameMap map) {
		super(map);
	}
	
	@Override
	public void make(float x, float y, float vx, float vy) {
		super.make(x, y, vx, vy);

		state = STATE.YOKOKU;
		
		frameCount = 0;
		
		isHit = false;
	}

	@Override
	public void loadImage() {
		SpriteSheet sheet  = ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.EFFECT_BOSS2_ATTACK);

		animation = new Animation2(sheet, 100);
		animation.setLooping(false);
		animation.stop();
	}

	@Override
	public void update(int delta) {
		if(isUsing) {
			frameCount++;
			
			switch(state) {
				case YOKOKU:
					this.setY(getY() - MOVE_Y_SPEED);
					break;
				case WAIT:
					break;
				case ATTACK:
					if(animation.isStopped()) {
						super.delete();
					}
					break;
				default:
					break;
			}
			
			//状態変化
			if(frameCount > state.getTime()) {
				changeState();
			}
		}
	
	}
	
	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		super.draw(g, offSetX, offSetY);

		g.setColor(Color.red);
		g.drawRect(getX() + DIFF_X + offSetX,  getY() + DIFF_Y + offSetY, getWidth() - DIFF_X * 2, this.getHeight() - DIFF_Y);
	}

	@Override
	public Rectangle getRectangle() {
		
		if(isHit) {
			return new Rectangle(x + DIFF_X, y + DIFF_Y, getWidth() - DIFF_X * 2, getHeight() - DIFF_Y);
		} else {
			return new Rectangle(0, 0, 0, 0);
		}
	}

	private void changeState() {
		switch(state) {
			case YOKOKU:
				state = STATE.WAIT;
				break;
			case WAIT:
				state = STATE.ATTACK;
				animation.start();
				isHit = true;
				break;
			case ATTACK:
				break;
			default:
				break;
		}
		
		frameCount = 0;
 	}
}
