package com.game.actiongame.Mapobject;

import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * 移動床管理クラス
 * @author moromorocco
 *
 */
public class MoveObjectManager extends AbstractManager {
	/**
	 * コンストラクタ
	 * @param map ゲームマップ
	 * @param mediator メディエータ
	 */
	public MoveObjectManager(GameMap map, MediatorInterface mediator) {
		super(map, mediator);
	}

//	/**
//	 * プレイヤーとの当たり判定チェック
//	 * @param player プレイヤー
//	 * @param map マップ
//	 */
//	public void checkPlayerHit(Player player, GameMap map) {
//		for(AbstractSprite sprite : super.getList()) {
//			MoveFloor floor = (MoveFloor)sprite;
//
//			if(!floor.getIsUsing()) {
//				return;
//			}
//
//			//当たった時の処理
//			if(player.getRectangle().intersects(sprite.getRectangle())) {
//				player.hitMooveFloor(floor);
//			} else {
//				floor.notHit();
//			}
//		}
//	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new MoveFloor(map, mediator);
	}

//	/** 作成 */
//	public void make() {
//		MoveFloor moveFloor = (MoveFloor)super.getSprite();
//		moveFloor.make(200, 350, 100, 16, 50, 0);
//
//		MoveFloor moveFloor2 = (MoveFloor)super.getSprite();
//		moveFloor2.make(300, 300, 100, 16, 0, 50);
//
//		MoveFloor moveFloor3 = (MoveFloor)super.getSprite();
//		moveFloor3.make(400, 300, 50, 16, 50, 50);
//	}

	/**
	 * 移動床作成
	 * @param str 敵情報
	 */
	public void make(String[] str) {
		int x = Integer.parseInt(str[1]);
		int y = Integer.parseInt(str[2]);
		int width = Integer.parseInt(str[3]);
		int heigth = Integer.parseInt(str[4]);
		int moveWidth = Integer.parseInt(str[5]);
		int moveHeigth = Integer.parseInt(str[6]);
		int dec = Integer.parseInt(str[7]);

		MoveFloor moveFloor = (MoveFloor)super.getSprite();
		moveFloor.make(x, y, width, heigth, moveWidth, moveHeigth, dec);
	}
}
