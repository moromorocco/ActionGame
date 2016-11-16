package com.game.actiongame.Utility;
//package com.game.actiongame.Utility;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//
//import org.newdawn.slick.Graphics;
//
//import com.game.actiongame.Base.GameObjectInterface;
//
///**
// * スプライト管理クラスの基底クラス
// * @author moromorocco
// */
//public class SpriteList implements GameObjectInterface {
//	/** オブジェクトリスト */
//	private LinkedList<AbstractSprite> spriteList;
//
//	/**
//	 * コンストラクタ
//	 * @param mediator メディエータ
//	 */
//	public SpriteList() {
//		spriteList = new LinkedList<AbstractSprite>();
//	}
//
//	/** 描画処理 */
//	public void draw(Graphics g, float offSetX, float offSetY) {
//		for(AbstractSprite sprite : spriteList) {
//			if(sprite.getIsUsing()) {
//				sprite.draw(g, offSetX, offSetY);
//			}
//		}
//	}
//
//	/** 更新 */
//	public void update(int delta) {
//		for(AbstractSprite sprite : spriteList) {
//			if(sprite.getIsUsing()) {
//				sprite.update(delta);
//			}
//		}
//	}
//
//	/**
//	 * スプライトの取得
//	 * @return スプライト
//	 */
//	public AbstractSprite getSprite() {
//		//再利用可能なスプライトを検索
//		for(AbstractSprite sprite : spriteList) {
//			if(!sprite.getIsUsing()) {
//				return sprite;
//			}
//		}
//
//		return null;
//	}
//
//	/**
//	 * リストのイテレータ取得
//	 * @return iterator
//	 */
//	public Iterator<AbstractSprite> getIterator() {
//		return spriteList.iterator();
//	}
//
//	/**
//	 * スプライトに追加
//	 * @param sprite スプライト
//	 */
//	public void addSprite(AbstractSprite sprite) {
//		spriteList.add(sprite);
//	}
//
//	/**
//	 * スプライトと保持しているリストの当たり判定チェック
//	 * @param target ターゲットとなるゲームオブジェクト
//	 */
//	public void checkHit(AbstractSprite target) {
//		for(AbstractSprite sprite : spriteList) {
//			if(!sprite.getIsUsing()) {
//				continue;
//			}
//
//			if(target.getRectangle().intersects(sprite.getRectangle())) {
//				//当たった時の処理は、各オブジェクトで実装
//				target.hitSprite(sprite);
//				sprite.hitSprite(target);
//			}
//		}
//	}
//
//	/**
//	 * 他のリストと保持しているリストの当たり判定
//	 * @param itr 他のObserverクラス
//	 */
//	public void checkHit(Iterator<AbstractSprite> itr) {
//		for(AbstractSprite sprite : spriteList) {
//			if(!sprite.getIsUsing()) {
//				continue;
//			}
//
//			checkHit(sprite);
//		}
//	}
//
//	/** マネージャの中身を全て空にする */
//	public void clearAll() {
//		for(AbstractSprite sprite : spriteList) {
//			sprite.delete();
//		}
//	}
//}
