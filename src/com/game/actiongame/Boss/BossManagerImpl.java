package com.game.actiongame.Boss;

import java.util.LinkedList;

import org.newdawn.slick.Graphics;

import com.game.actiongame.Base.GameObjectInterface;
import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * ボス管理クラス
 * @author moromorocco
 *
 */
public class BossManagerImpl implements GameObjectInterface, BossManagerInterface {
	//メディエータ
	private MediatorInterface mediator;
	//マップ
	private GameMap map;
	//プレイヤー
	private Player player;
	//現在出現中のボス
	private AbstractBoss boss;
	//ファクトリ
	private BossFactory factory;
	//ボスタイプ
	public BossEnum bossType;
	
	//ボス撃破フラグ
	private boolean isDeadBoss1;
	private boolean isDeadBoss2;
	private boolean isDeadBoss3;
	

	/**
	 * ボスの種類
	 * @author moromorocco
	 *
	 */
	public enum BossEnum {
		BOSS1,
		BOSS2,
		BOSS3,
	}

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 * @param player プレイヤー
	 */
	public BossManagerImpl(GameMap map, MediatorInterface mediator, Player player) {
		this.map = map;
		this.mediator = mediator;
		this.player = player;

		mediator.set(this);
		
//		boss = new Boss1(map, mediator, this);
		bossType = BossEnum.BOSS1;
		
		factory = new BossFactory();
		boss = factory.makeBoss(bossType, map);
		
		isDeadBoss1 = false;
		isDeadBoss2 = false;
		isDeadBoss3 = false;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		boss.draw(g, offSetX, offSetY);
	}

	@Override
	public void update(int delta) {
		boss.update(delta);
	}

	/**
	 * ボス種類からボス作成
	 * @param bossType 登場ボス種類
	 */
	public void makeBoss(BossEnum bossType) {
		switch(bossType) {
		case BOSS1:
			if(isDeadBoss1) {
				return;
			}
//			boss = new Boss1(map, mediator, this);
			break;
		case BOSS2:
			if(isDeadBoss2) {
				return;
			}
//			boss = new Boss2(map, mediator, this);
			break;
		case BOSS3:
			if(isDeadBoss3) {
				return;
			}
			break;
		default:
		}

		boss = factory.makeBoss(bossType, map);
		boss.setMediator(mediator);
		boss.setManager(this);

//		boss.make(300, 250);
	}

	/**
	 * ボス作成
	 * @param str ボス情報
	 */
	public void makeBoss(String[] str) {
		int x = Integer.parseInt(str[1]);
		int y = Integer.parseInt(str[2]);
		int vx = Integer.parseInt(str[3]);
		int vy = Integer.parseInt(str[4]);
		BossEnum type = BossEnum.valueOf(str[5]);

		makeBoss(type);

		boss.make(x, y);
	}

	/**
	 * スプライトとボスの当たり判定
	 * @param sprite スプライト
	 */
	public void checkHit(AbstractSprite sprite) {
		if(!boss.getIsUsing()) {
			return;
		}

		if(sprite.getIsUsing() == false) {
			return;
		}

		boss.checkHit(sprite);
	}

	/**
	 * リストとボスの当たり判定
	 * @param list スプライトリスト
	 */
	public void checkHit(LinkedList<AbstractSprite> list) {
		for(AbstractSprite target : list) {
			checkHit(target);
		}
	}

	/**
	 * ボススプライトの取得
	 * @return boss ボス
	 */
	public AbstractBoss getBoss() {
		return boss;
	}

	@Override
	public void bossDead() {
		//20161023 ADD START ボスが復活しないように修正
		if (boss instanceof Boss1) {
			isDeadBoss1 = true;
		} else if (boss instanceof Boss2) {
			isDeadBoss2 = true;
		} else {
			isDeadBoss3 = true;
		}
	
		boss.delete();
		//20161023 ADD END
	}

	@Override
	public float getPlayerCenterX() {
		return player.getCenterX();
	}

	@Override
	public float getPlayerCenterY() {
		return player.getCenterY();
	}
}
