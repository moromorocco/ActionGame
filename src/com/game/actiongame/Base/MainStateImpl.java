package com.game.actiongame.Base;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.game.actiongame.Boss.BossManagerImpl;
import com.game.actiongame.Effect.EffectManager;
import com.game.actiongame.Enemy.EnemyManager;
import com.game.actiongame.EnemyBullet.EnemyBulletManagerImpl;
import com.game.actiongame.Item.ItemManager;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Map.MapInfo;
import com.game.actiongame.Mapobject.MoveObjectManager;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Player.PlayerBulletManager;
import com.game.actiongame.Scene.OptionScene;
import com.game.actiongame.Scene.PauseScene;
import com.game.actiongame.Scene.ShopScene;
import com.game.actiongame.Scene.TitleScene;
import com.game.actiongame.Utility.BGMEnum;
import com.game.actiongame.Utility.SoundManager;

public class MainStateImpl extends BasicGameState
		implements MainStateInterface {

	public static final int MAIN_ID = 10;

	private MediatorImpl mediator;
	private GameMap map;
	private Player player;
	private PlayerBulletManager playerBullet;
	private EnemyManager enemyManager;
	private EnemyBulletManagerImpl enemyBullet;
	private BossManagerImpl bossManager;
	private ItemManager itemManager;
	private MoveObjectManager moveObject;
	private EffectManager effectManager;

	private TitleScene titleScene;
	private OptionScene optionScene;
	private ShopScene shopScene;
	private PauseScene pauseScene;

	private GameSceneInterface gameScene;
	private SCENE scene;

	public enum SCENE {
		TITLE,
		NEW_GAME,
		GAME,
		SHOP,
		OPTION,
		PAUSE
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//メディエータ
		mediator = new MediatorImpl(this);
		//マップ
		map = new GameMap(mediator);

		//プレイヤー
		player = new Player(map, mediator);
		player.make(100, 200);
		//プレイヤー攻撃
		playerBullet = new PlayerBulletManager(map, mediator);
		//敵
		enemyManager = new EnemyManager(map, mediator);
		//敵弾
		enemyBullet = new EnemyBulletManagerImpl(map, mediator);
		//ボスマネージャ
		bossManager = new BossManagerImpl(map, mediator, player);
		//アイテム
		itemManager = new ItemManager(map, mediator);
		//移動床マネージャ
		moveObject = new MoveObjectManager(map, mediator);
		//エフェクト
		effectManager = new EffectManager(map, mediator);

		titleScene = new TitleScene(this);
		shopScene = new ShopScene(this, player);
		optionScene = new OptionScene(this);
		pauseScene = new PauseScene(this);
		gameScene = titleScene;

		scene = SCENE.TITLE;

		init();
	}

	private void init() {
		//最初のマップをロード
		MapInfo mapInfo = new MapInfo("mapB4.tmx", "mapB4.csv", "STAGE1");
		map.loadStageMap(player, mapInfo);
		player.setX(200);
		player.setY(200);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//描画
		gameScene.draw(g, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		//更新
		gameScene.update(delta);
	}

	@Override
	public int getID() {
		return MAIN_ID;
	}

	@Override
	public void keyPressed(int key, char c) {
		gameScene.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		gameScene.keyReleased(key, c);
	}

	@Override
	public void changeScene(SCENE scene) {
		Log.debug("scene:" + scene.toString());

		switch(scene) {
			case TITLE:
				gameScene = titleScene;
				
				//201628 追加
				mediator.clearManager();
				break;
			case NEW_GAME:
				gameScene = mediator;
				init();
				break;
			case GAME:
				gameScene = mediator;
				break;
			case SHOP:
				shopScene.init();
				shopScene.saveBGM(SoundManager.getBGMType());
				SoundManager.playBGM(BGMEnum.SHOP);
				gameScene = shopScene;
				break;
			case OPTION:
				optionScene.setScene(this.scene);
				gameScene = optionScene;
				break;
			case PAUSE:
				gameScene = pauseScene;
				break;
			default:
				break;
		}

		this.scene = scene;
	}

	@Override
	public void getSeald() {
		player.makeSield();
	}
}
