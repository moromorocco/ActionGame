package com.game.actiongame.Enemy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.Item.Item.ITEM_TYPE;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.PlayerAttack;
import com.game.actiongame.Player.PlayerBullet;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.SoundManager;
import com.game.actiongame.Utility.SoundEnum;

/**
 * 敵を描画・更新するためのクラス
 * @author moromorocco
 *
 */
public class BaseEnemy extends AbstractSprite {
	/** マップ */
	protected GameMap map;
	/** メディエータ */
	protected MediatorInterface mediator;
	/** 敵クラス(委譲) */
	private AbstractSprite enemy = null;

	/**
	 * コンストラクタ
	 * @param map マップ
	 * @param mediator メディエータ
	 */
	public BaseEnemy(GameMap map, MediatorInterface mediator) {
		super(map);
		this.mediator = mediator;
	}

	@Override
	public void loadImage() {
		//使わない
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		if(enemy == null || !enemy.getIsUsing()) {
			return;
		}

		enemy.draw(g, offSetX, offSetY);
	}

	@Override
	public void update(int delta) {
		if(enemy == null || !enemy.getIsUsing()) {
			return;
		}

		enemy.update(delta);
	}

	/**
	 * 敵を作成
	 * @param enemy 敵クラス
	 * @param life 敵ライフ
	 */
	public void setEnemy(AbstractSprite enemy, int life) {
		this.enemy = enemy;
		this.life = life;

		super.make(0, 0);
	}

	@Override
	public Rectangle getRectangle() {
		return enemy.getRectangle();
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		//倒せない敵はキャンセル
		if(enemy instanceof CannotBreakMark) {
			return;
		}

		if(sprite instanceof PlayerAttack) {
			SoundManager.playSE(SoundEnum.PLAYER_ATTACK);
		}

		//敵のライフはこっちで管理するように変更
		if(sprite instanceof PlayerAttack) {
			life -= 3;
		}

		if(sprite instanceof PlayerBullet) {
			life -= 1;
		}

		if(life <= 0) {
			/**
			 * 敵を倒した時の音
			 * ここで処理をしないと、リストクリア時、音が鳴ってしまう。
			 */
			SoundManager.playSE(SoundEnum.ENEMY_DEAD);

			//消滅時のアイテム、エフェクト作成は親で行うように変更
			delete();
		}
	}

	@Override
	public void delete() {
		/** 消滅エフェクト作成 */
		mediator.makeEffect(enemy.getX(), enemy.getY(), EFFECT_TYPE.ENEMY_DEAD);

		/** アイテムの作成 */
		mediator.makeItem(enemy.getX(), enemy.getY(), ITEM_TYPE.COIN);

		//敵と自分自身を削除
		enemy.delete();
		super.delete();
	}
}
