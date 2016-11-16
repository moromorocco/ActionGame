//package com.game.actiongame.EnemyBullet;
//
//import org.newdawn.slick.SpriteSheet;
//import org.newdawn.slick.geom.Rectangle;
//
//import com.game.actiongame.Base.MediatorInterface;
//import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
//import com.game.actiongame.Map.GameMap;
//import com.game.actiongame.Player.Player;
//import com.game.actiongame.Player.PlayerGuard;
//import com.game.actiongame.Utility.AbstractSprite;
//import com.game.actiongame.Utility.Animation2;
//import com.game.actiongame.Utility.ImageLoaderUtility2;
//import com.game.actiongame.Utility.SheetTypeEnum.EnemyBulletUniqueEnum;
//
///**
// * 敵弾クラス
// * @author moromorocco
// *
// */
//public class EnemyBulletUnique extends AbstractSprite
//		implements EnemyBulletMark {
//
//	private static final int AREA = 30;
//	private static final float RAKKA = 0.3f;
//	
//	private static final int WAVE_COUNT = 5;
//	private static final int WAVE_HEIGHT = 5;
//
//	private MediatorInterface mediator;
//	private EnemyBulletUniqueEnum type;
//	private int frameCount;
//	private int waveHeightCount = 0;
//
//	/**
//	 * コンストラクタ
//	 * @param map マップ
//	 * @param mediator メディエータ
//	 */
//	public EnemyBulletUnique(GameMap map, MediatorInterface mediator) {
//		super(map);
//
//		this.mediator = mediator;
//	}
//
//	@Override
//	public void loadImage() {
////		SpriteSheet sheet = type.getSheet();
//		SpriteSheet sheet = ImageLoaderUtility2.loadKoSheet(type);
//
//		animation = new Animation2(sheet, 60);
//	}
//
//	/**
//	 * 敵ショットを作成
//	 *
//	 * @param x 出現X座標
//	 * @param y 出現Y座標
//	 * @param vx X方向の角加速度
//	 * @param vy Y方向の角加速度
//	 * @param type 作成種類。イメージの種類に使用
//	 */
//	public void make(float x, float y, float vx, float vy,
//			EnemyBulletUniqueEnum type) {
//
//		this.type = type;
//
//		super.make(x, y, vx, vy);
//
//		frameCount = 0;
//		waveHeightCount = 0;
//	}
//
//	@Override
//	public void update(int delta) {
//		x += vx;
//		y += vy;
//		frameCount++;
//
//		switch(type) {
//			case BULLET_RAKKA:
//				vy += RAKKA;
//				break;
//			case BULLET_TURN:
//				if(frameCount == 30) {
//					vx = -vx;
//				}
//
//				if(frameCount > 60) {
//					super.delete();
//				}
//
//				break;
//			case BULLET_BOSS2_WAVE:
//				waveHeightCount += WAVE_COUNT;
//				vy = (float)(WAVE_HEIGHT * Math.sin(Math.toRadians(waveHeightCount)));
//				break;
//			default:
//				break;
//		}
//
//		if(map.getTileCollision(this, x, y) != null) {
//			delete();
//		}
//
//		if(x < -AREA) {
//			super.delete();
//		}
//
//		if(x > WIDTH + AREA) {
//			super.delete();
//		}
//
//		if(y < -AREA) {
//			super.delete();
//		}
//
//		if(y > HEIGHT + AREA) {
//			super.delete();
//		}
//
//		super.rotate();
//	}
//
//	@Override
//	public Rectangle getRectangle() {
//		return new Rectangle(
//				x + type.getSize(),
//				y + type.getSize(),
//				getWidth() - type.getSize() * 2,
//				getHeight() - type.getSize() * 2);
//	}
//
//	@Override
//	public void hitSprite(AbstractSprite sprite) {
//		if(sprite instanceof PlayerGuard) {
//			delete();
//		}
//
//		if(sprite instanceof Player) {
//			delete();
//		}
//	}
//
//	@Override
//	public void delete() {
//		makeEffect();
//
//		super.delete();
//	}
//
//	private void makeEffect() {
//		mediator.makeEffect(x, y, EFFECT_TYPE.SHOT_HIT);
//	}
//}
