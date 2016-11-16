package com.game.actiongame.Utility;

import java.util.LinkedList;

import org.newdawn.slick.Graphics;

import com.game.actiongame.Base.GameObjectInterface;
import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;

/**
 * スプライト管理クラスの基底クラス
 * @author moromorocco
 */
public abstract class AbstractManager implements GameObjectInterface {
	/** オブジェクトリスト */
	private LinkedList<AbstractSprite> spriteList;
	/** マップ */
	protected GameMap map;
	/** メディエータ */
	protected MediatorInterface mediator;

	/**
	 * コンストラクタ
	 * @param map ゲームマップ
	 * @param mediator メディエータ
	 */
	public AbstractManager(GameMap map, MediatorInterface mediator) {
		this.map = map;
		this.mediator = mediator;

		spriteList = new LinkedList<AbstractSprite>();

		mediator.set(this);
	}

	/** 初期化 */
	public void init() {
		clearAll();
	}

	/** ゲームオブジェクトインターフェース */

	/** 描画処理 */
	public void draw(Graphics g, float offSetX, float offSetY) {
		for(AbstractSprite sprite : spriteList) {
			if(sprite.getIsUsing()) {
				sprite.draw(g, offSetX, offSetY);
			}
		}
	}

	/** 更新 */
	public void update(int delta) {
		for(AbstractSprite sprite : spriteList) {
			if(sprite.getIsUsing()) {
				sprite.update(delta);
			}
		}
	}

	/**
	 * スプライトの取得
	 *
	 * @return スプライト
	 */
	public AbstractSprite getSprite() {
		//再利用可能なスプライトを検索
		for(AbstractSprite sprite : spriteList) {
			if(!sprite.getIsUsing()) {
				return sprite;
			}
		}

		//新しいスプライトを作成
		AbstractSprite newSprite = createSprite(spriteList.size());
		spriteList.add(newSprite);

		return newSprite;
	}

	/**
	 * リストに追加するインスタンスの作成
	 *
	 * @return createSprite
	 */
	protected abstract AbstractSprite createSprite(int spriteCount);

	/**
	 * スプライトと保持しているリストの当たり判定チェック
	 * @param target ターゲットとなるゲームオブジェクト
	 */
	public void checkHit(AbstractSprite target) {
		for(AbstractSprite sprite : spriteList) {
			if(sprite.getIsUsing() == false) {
				continue;
			}

			if(target.getIsUsing() == false) {
				continue;
			}

			if(target.getRectangle().intersects(sprite.getRectangle())) {
				//当たった時の処理は、各オブジェクトで実装
				target.hitSprite(sprite);
				sprite.hitSprite(target);
			}
		}
	}

	/**
	 * 他のリストと保持しているリストの当たり判定
	 * @param list 他のリスト
	 */
	public void checkHit(LinkedList<AbstractSprite> list) {
		for(AbstractSprite target : list) {
			if(target.getIsUsing() == false) {
				continue;
			}

			checkHit(target);
		}
	}

	/**
	 * リストのイテレータ取得
	 * @return iterator
	 */
	public LinkedList<AbstractSprite> getList() {
		return spriteList;
	}

	/** マネージャの中身を全て空にする */
	public void clearAll() {
		//再利用可能なスプライトを検索
		for(AbstractSprite sprite : spriteList) {
			sprite.delete();
		}
	}

	/**
	 * プレイヤーとの角度を取得
	 * @param x x座標
	 * @param y x座標
	 * @return rad 角度(ラジアン)
	 */
	public double getPlayerRad(float x, float y) {
		return mediator.getRad(x, y);
	}
}
