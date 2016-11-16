package com.game.actiongame.Base;

import java.util.LinkedList;

import com.game.actiongame.Effect.Effect.EFFECT_TYPE;
import com.game.actiongame.Enemy.EnemyManager;
import com.game.actiongame.Item.Item;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Player.PlayerBullet;
import com.game.actiongame.ShotPattern.MakeShotInterface;

/**
 * メディエータインターフェース
 * @author moromorocco
 */
public interface MediatorInterface extends GameSceneInterface{
	/**
	 * メディエータに登録
	 * @param gameObject 登録するオブジェクト
	 */
	public void set(GameObjectInterface gameObject);

	/**
	 * プレイヤーショットの作成
	 *
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 * @param type 描画種類
	 */
	public void makePlayerShot(float x, float y, float vx, float vy,
			PlayerBullet.TYPE type);

	/**
	 * 敵の作成
	 *
	 * @param x x座標
	 * @param y y座標
	 * @param vx 横加速度
	 * @param vy 縦加速度
	 * @param type 敵種類
	 */
	public void makeEnemy(float x, float y, float vx, float vy, EnemyManager.TYPE type);

	/**
	 * 敵弾を作成
	 * @param x x座標
	 * @param y y座標
	 * @param shotType 弾パターン
	 */
	public void makeEnemyShot(MakeShotInterface shotType, float x, float y);

	/**
	 * プレイヤーとの角度(ラジアン)を取得
	 * @param x x座標
	 * @param y y座標
	 * @return rad 角度
	 */
	public double getRad(float x, float y);

	/**
	 * アイテムの作成
	 * @param x x座標
	 * @param y y座標
	 * @param itemType 種類
	 */
	public void makeItem(float x, float y, Item.ITEM_TYPE itemType);

	/**
	 * エフェクトの作成
	 * @param x x座標
	 * @param y y座標
	 * @param effectType エフェクト種類
	 */
	public void makeEffect(float x, float y, EFFECT_TYPE effectType);

	/**
	 * プレイヤーのX座標を取得
	 * @return x x座標
	 */
	public float getPlayerX();

	/**
	 * プレイヤーのY座標を取得
	 * @return y y座標
	 */
	public float getPlayerY();

	/**
	 * 読み込んだファイルから、オブジェクトを作成
	 * @param list 読込テキストリスト
	 */
	public void laodItems(LinkedList<String> list);

	public void changeScene(MainStateImpl.SCENE scene);

	/** スクロール処理 */
	public void setScroll(GameMap.MOVE_DIR dir);
	public void endScroll();
	
	/** 梯子に接触しているかチェック */
	public boolean checkHashigo(Player player);
}
