package com.game.actiongame.Base;

import java.util.LinkedList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.game.actiongame.Base.MainStateImpl.SCENE;
import com.game.actiongame.Boss.BossManagerImpl;
import com.game.actiongame.Boss.BossManagerImpl.BossEnum;
import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.Effect.EffectManager;
import com.game.actiongame.Enemy.EnemyManager;
import com.game.actiongame.EnemyBullet.EnemyBulletManagerImpl;
import com.game.actiongame.Item.Item.ITEM_TYPE;
import com.game.actiongame.Item.ItemManager;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Map.MapNaname;
import com.game.actiongame.Mapobject.MoveObjectManager;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Player.PlayerAttack;
import com.game.actiongame.Player.PlayerBullet;
import com.game.actiongame.Player.PlayerBulletManager;
import com.game.actiongame.Player.PlayerGuard;
import com.game.actiongame.ShotPattern.MakeShotInterface;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

public class MediatorImpl implements MediatorInterface {
	private static int GAME_WIDTH;
	private static int GAME_HEIGHT;
	private LinkedList<GameObjectInterface> spriteList;
	private GameMap map;
	private Player player;
	private PlayerBulletManager playerBullet;
	private EnemyManager enemyManager;
	private EnemyBulletManagerImpl enemyBullet;
	private BossManagerImpl bossManager;
	private ItemManager itemManager;
	private MoveObjectManager moveObject;
	private EffectManager effectManager;
	private MainStateInterface mainState;
	private STATE gameState;

	/** 読み込むファイルの種類 */
	private enum FILE_TYPE {
		PLAYER,
		ENEMY,
		BOSS,
		MOVE_FLOOR,
		MAP_MESSAGE,
		MAP_SHOP,
		//マップスクロール方向
		MAP_RIGHT,
		MAP_LEFT,
		MAP_DOWN,
		MAP_UP,
//		//斜め床
//		MAP_NANAME,
	}

	/** 状態 */
	private enum STATE {
		NORMAL,
		SCROLL,
	}

	/**
	 * コンストラクタ
	 * @param mainState メインクラス
	 */
	public MediatorImpl(MainStateInterface mainState) {
		this.mainState = mainState;

		spriteList = new LinkedList<GameObjectInterface>();

		GAME_WIDTH = Start.WIDTH;
		GAME_HEIGHT = Start.HEIGHT;

		gameState = STATE.NORMAL;
	}

	@Override
	public void set(GameObjectInterface gameObject) {
		if(gameObject instanceof GameMap) this.map = (GameMap)gameObject;
		if(gameObject instanceof Player) this.player = (Player)gameObject;
		if(gameObject instanceof PlayerBulletManager) this.playerBullet = (PlayerBulletManager)gameObject;
		if(gameObject instanceof EnemyManager) this.enemyManager = (EnemyManager)gameObject;
		if(gameObject instanceof EnemyBulletManagerImpl) this.enemyBullet = (EnemyBulletManagerImpl)gameObject;
		if(gameObject instanceof BossManagerImpl) this.bossManager = (BossManagerImpl)gameObject;
		if(gameObject instanceof ItemManager) this.itemManager = (ItemManager)gameObject;
		if(gameObject instanceof MoveObjectManager) this.moveObject = (MoveObjectManager)gameObject;
		if(gameObject instanceof EffectManager) this.effectManager = (EffectManager)gameObject;

		spriteList.add(gameObject);
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		switch(gameState) {
			case NORMAL:
				drawNormal(g);
				break;
			case SCROLL:
				drawScroll(g);
				break;
			default:
				break;
		}
	}

	/**
	 * スクロール中の描画処理
	 * @param g graphics
	 */
	private void drawScroll(Graphics g) {
		map.drawScroll(g);
		player.drawScroll(g);
	}

	/**
	 * 通常の描画処理
	 * @param g graphics
	 */
	private void drawNormal(Graphics g) {
		float offSetX;
		float offSetY;

		offSetX = (int)(GAME_WIDTH / 2 - player.getX());
		offSetY = (int)(GAME_HEIGHT / 2 - player.getY());

		offSetX = Math.min(offSetX, 0);
		offSetX = Math.max(offSetX, GAME_WIDTH - map.getWidth());
		offSetY = Math.min(offSetY, 0);
		offSetY = Math.max(offSetY,  GAME_HEIGHT - map.getHeight());

		for(GameObjectInterface obj : spriteList) {
			obj.draw(g, offSetX, offSetY);
		}
	}

	@Override
	public void update(int delta) {
		switch(gameState) {
			case NORMAL:
				updateNormal(delta);
				break;
			case SCROLL:
				updateScroll(delta);
				break;
			default:
				break;
		}
	}

	/**
	 * 通常の更新処理
	 * @param delta delta
	 */
	private void updateNormal(int delta) {
		//スプライトの更新
		for(GameObjectInterface obj : spriteList) {
			obj.update(delta);
		}

		//マップとプレイヤーの当たり判定
		map.checkMessage(player);
		//マップと斜め床の当たり判定
		map.checkNanameFloor(player);
//		//マップと斜め床の当たり判定
//		map.checkHashigo(player);

		//プレイヤーの攻撃(魔法)と敵の当たり判定
		enemyManager.checkHit(playerBullet.getList());
		bossManager.checkHit(playerBullet.getList());

		//プレイヤーの攻撃(物理)と敵の当たり判定
		PlayerAttack playerAttack = player.getAttack();
		enemyManager.checkHit(playerAttack);
		bossManager.checkHit(playerAttack);

		//盾と攻撃の当たり判定
		PlayerGuard playerGuard = player.getGuard();
		if(playerGuard != null) {
			enemyBullet.checkHit(playerGuard);
		}

		//プレイヤーと敵の当たり判定
		enemyManager.checkHit(player);
		bossManager.checkHit(player);

		//敵の攻撃とプレイヤーの当たり判定
		enemyBullet.checkHit(player);
		bossManager.checkHit(player);

		//プレイヤーとアイテム
		itemManager.checkHit(player);
		//プレイヤーと移動床の当たり判定
		moveObject.checkHit(player);
	}

	/**
	 * スクロール処理
	 * @param delta delta
	 */
	private void updateScroll(int delta) {
		map.updateScroll(player);
	}

	@Override
	public void makePlayerShot(float x, float y, float vx, float vy, PlayerBullet.TYPE type) {
		playerBullet.makeShot(x, y, vx, vy, type);
	}

	@Override
	public double getRad(float x, float y) {
		return Math.atan2(player.getX() - x, player.getY() - y);
	}

	@Override
	public float getPlayerX() {
		return player.getX();
	}

	@Override
	public float getPlayerY() {
		return player.getY();
	}

	@Override
	public void keyPressed(int key, char c) {
		if(gameState != STATE.NORMAL) {
			return;
		}

		//ポーズ画面に移動
		if(key == Input.KEY_ESCAPE) {
			changeScene(SCENE.PAUSE);
		}

		//上キー押下時は、移動チェック
		if(key == Input.KEY_UP) {
			//ボス戦中は移動不可
			if(bossManager.getBoss().getIsUsing()) {
				return;
			}

			//ショップ移動の確認
			map.checkMove(player);
		}

		if(key == Input.KEY_J) {
			//敵作成
			makeEnemy(300, 300, 2.0f, GameMap.GRAVITY, EnemyManager.TYPE.MOVE_SIN);
		}

		if(key == Input.KEY_K) {
			//ボス作成
			bossManager.makeBoss(BossEnum.BOSS1);
		}

		if(key == Input.KEY_S) {
			//シールド作成
			player.makeSield();
		}
		
//		if(key == Input.KEY_N) {
//			//斜め床作成
//			String[] str = new String[]{"", "96", "320", "RIGHT_UP"};
//			map.makeNanameFloor(str);
//
//			String[] str2 = new String[]{"", "288", "256", "RIGHT_DOWN"};
//			map.makeNanameFloor(str2);
//		}
		
		player.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
	}

	/** EnemyInterface */
	@Override
	public void makeEnemy(float x, float y, float vx, float vy, EnemyManager.TYPE type) {
		enemyManager.makeEnemy(x, y, vx, vy, type);
	}

	@Override
	public void makeEnemyShot(MakeShotInterface shotType, float x, float y) {
		SoundManager.playSE(SoundEnum.ENEMY_SHOT);
		shotType.make(enemyBullet, x, y);
	}

	@Override
	public void makeItem(float x, float y, ITEM_TYPE itemType) {
		itemManager.makeItem(x, y, itemType);
	}

	@Override
	public void makeEffect(float x, float y, EFFECT_TYPE effectType) {
		effectManager.makeEffect(x, y, effectType);
	}

	@Override
	public void laodItems(LinkedList<String> list) {
		clearManager();

		for(String line : list) {
			String[] str = line.split("\t", 0);

			//移動床
			if(str[0].equals(FILE_TYPE.MOVE_FLOOR.toString())) {
				moveObject.make(str);
			}

			//敵作成
			if(str[0].equals(FILE_TYPE.ENEMY.toString())) {
				enemyManager.makeEnemy(str);
			}

			//ボス作成
			if(str[0].equals(FILE_TYPE.BOSS.toString())) {
				bossManager.makeBoss(str);
			}

			//メッセージ
			if(str[0].equals(FILE_TYPE.MAP_MESSAGE.toString())) {
				map.makeMessage(str);
			}

			//ショップ
			if(str[0].equals(FILE_TYPE.MAP_SHOP.toString())) {
				map.makeShopDoor(str);
			}
			
//			//斜めの床
//			if(str[0].equals(FILE_TYPE.MAP_NANAME.toString())) {
//				map.makeNanameFloor(str);
//			}

			//移動
			if(str[0].equals(FILE_TYPE.MAP_RIGHT.toString())) {
				map.makeMoveFloor(str, GameMap.MOVE_DIR.RIGHT);
			}
			if(str[0].equals(FILE_TYPE.MAP_LEFT.toString())) {
				map.makeMoveFloor(str, GameMap.MOVE_DIR.LEFT);
			}
			if(str[0].equals(FILE_TYPE.MAP_UP.toString())) {
				map.makeMoveFloor(str, GameMap.MOVE_DIR.UP);
			}
			if(str[0].equals(FILE_TYPE.MAP_DOWN.toString())) {
				map.makeMoveFloor(str, GameMap.MOVE_DIR.DOWN);
			}
		}
	}

	@Override
	public void changeScene(SCENE scene) {
		mainState.changeScene(scene);
	}

	@Override
	public void setScroll(GameMap.MOVE_DIR moveDir) {
		if(checkMoveable() == false) {
			return;
		}
	
		map.setMoveDir(moveDir);
		gameState = STATE.SCROLL;

		player.setVx(0);
		player.setVy(0);

		clearManager();
	}

	private boolean checkMoveable() {
		if(bossManager.getBoss().getIsUsing()) {
			return false;
		}

		return true;
	}

	@Override
	public void endScroll() {
		gameState = STATE.NORMAL;
	}

	/**
	 * マネージャクラスを初期化
	 */
	//20161026 private → publicに変更
	public void clearManager() {
		playerBullet.clearAll();
		enemyManager.clearAll();
		enemyBullet.clearAll();
		itemManager.clearAll();
		moveObject.clearAll();
		effectManager.clearAll();
	}

	@Override
	public boolean checkHashigo(Player player) {
		return map.checkHashigo(player);
	}
}
