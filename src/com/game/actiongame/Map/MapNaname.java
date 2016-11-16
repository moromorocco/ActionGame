package com.game.actiongame.Map;

import org.newdawn.slick.geom.Rectangle;

import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.AbstractSprite;

public class MapNaname  extends AbstractSprite{
	private static final int HIT_DIFF = 1;
	private TypeEnum type;

	/**
	 * 斜め床の種類
	 * 2016/10/26 現在、右上移動、右下移動のみ
	 * @author mozaedon
	 *
	 */
	public enum TypeEnum {
		RIGHT_UP,
		RIGHT_DOWN,
		LEFT_UP,
		LEFT_DOWN,
	}

	/**
	 * 斜め移動用の床
	 * 
	 * @param map
	 * @param type
	 */
	public MapNaname(GameMap map, TypeEnum type) {
		super(map);
		
		this.type = type;
	}

	@Override
	public void loadImage() {
//		switch(type) {
//			case RIGHT_UP:
//				this.img = ImageLoaderUtility.getImage(SheetType.MAP_NANAME, 0, 0);
//				break;
//			case RIGHT_DOWN:
//				this.img = ImageLoaderUtility.getImage(SheetType.MAP_NANAME, 1, 0);
//				break;
//			default:
//				break;
//		}
	}
	
	@Override
	public void update(int delta) {
	}

	/**
	 * 他のスプライトと接触した時
	 * 
	 * @param sprite ぶつかった相手のスプライト
	 */
	@Override
	public void hitSprite(AbstractSprite sprite) {

		if(sprite instanceof Player) {
			Player player = (Player) sprite;

			//接触中にジャンプした場合などは、位置修正を行わない
			if(player.getVy() < 0) {
				return;
			}
			
			player.setY(this.getY());
			player.setOnGround(true);
			
			int diff = (int) Math.abs(player.getX() - this.getX());
			
			player.setY(player.getY() + diff - GameMap.TILE_SIZE);
		}
	}
	
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(
				x + HIT_DIFF,
				y + HIT_DIFF,
				this.getWidth() - HIT_DIFF,
				this.getHeight() - HIT_DIFF);
	}
}
