package com.game.actiongame.Map;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import com.game.actiongame.Base.GameObjectInterface;
import com.game.actiongame.Base.MainStateImpl;
import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Base.Start;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.BGMEnum;
import com.game.actiongame.Utility.SoundManager;

/**
 * マップクラス 移動の可否と、ドアクラスによるマップ移動処理を行う。
 *
 * @author moromorocco
 *
 */
public class GameMap implements GameObjectInterface {
	public static final int ID_NOTHING = 0;
	public static final int ID_WATER = 9;
	private final int ID_RIGHT_UP = 2;
	private final int ID_RIGHT_DOWN = 3;
//	private final int ID_LEFT_UP = 10;
//	private final int ID_LEFT_DOWN = 11;
	private final int ID_HASHIGO = 5;

	public static final float GRAVITY = 0.5f;
	public static final int TILE_SIZE = 32;
	/** メディエータ */
	private MediatorInterface mediator;
	/** 描画中のマップ */
	private TiledMap map;
	/** 読み込んだマップ */
	private HashMap<String, MapInfo> mapList;
	/** メッセージオブジェクトのリスト */
	private LinkedList<MapMessage> messageList;
	/** 斜め床オブジェクトのリスト */
	private LinkedList<MapNaname> nanameFloorList;
	/** 梯子オブジェクトのリスト */
	private LinkedList<MapHashigo> hashigoList;

	/** ショップ */
	private MapShop mapShop;
	/** フィールド */
	private int col;
	private int row;
	private boolean[][] blocked;

	/** スクロール中かどうか */
	private boolean isScrolling;
	/** スクロール速度 */
	private final int SCROLL_SPEED = 10;
	/** プレイヤーの移動後の微調整 */
	private final int DIFF = 10;
	/** スクロール中描画するマップ */
	private TiledMap nextMap;
	/** マップ移動情報 */
	private MapInfo nextDoor;
	/** スクロールで使用するカウンタ */
	private int scrollCountX;
	private int scrollCountY;
	/** 次マップ描画位置 */
	private int nextMapX = 0;
	private int nextMapY = 0;
	/** スクロール用オフセット */
	private float oldOffSetX;
	private float oldOffSetY;
	/** スクロール方向 */
	private MOVE_DIR moveDir;

	/**
	 * 移動方向と、移動先の情報
	 * 
	 * @author moromorocco
	 *
	 */
	public enum MOVE_DIR {
		RIGHT, LEFT, UP, DOWN;

		private MapInfo mapInfo;

		public void setMapInfo(MapInfo mapInfo) {
			this.mapInfo = mapInfo;
		}

		public MapInfo getMapInfo() {
			return mapInfo;
		}
	}

	/**
	 * コンストラクタ
	 * 
	 * @param mediator
	 *            メディエータ
	 */
	public GameMap(MediatorInterface mediator) {
		this.mediator = mediator;
		this.mediator.set(this);

		mapList = new HashMap<String, MapInfo>();
		messageList = new LinkedList<MapMessage>();
		nanameFloorList = new LinkedList<MapNaname>();
		hashigoList = new LinkedList<MapHashigo>();
		
		mapShop = new MapShop(this);

		isScrolling = false;
	}

	@Override
	public void update(int delta) {
		// メッセージ更新
		for (MapMessage message : messageList) {
			message.update(delta);
		}
	}

	/**
	 * マップスクロール処理 =========追加====================================
	 * 
	 * @param player
	 *            プレイヤー
	 */
	public void updateScroll(Player player) {
		// スクロール位置
		int scrollVX = 0;
		int scrollVY = 0;
		int playerVX = 0;
		int playerVY = 0;

		switch (moveDir) {
		case RIGHT:
			scrollVX = -SCROLL_SPEED;
			playerVX = -SCROLL_SPEED + 1;
			break;
		case LEFT:
			scrollVX = SCROLL_SPEED;
			playerVX = SCROLL_SPEED - 1;
			break;
		case DOWN:
			scrollVY = -SCROLL_SPEED;
			playerVY = -SCROLL_SPEED + 1;
			break;
		case UP:
			scrollVY = SCROLL_SPEED;
			playerVY = SCROLL_SPEED - 1;
			break;
		default:
			break;
		}

		scrollCountX += scrollVX;
		scrollCountY += scrollVY;

		if (moveDir == MOVE_DIR.RIGHT || moveDir == MOVE_DIR.LEFT) {
			player.setX(player.getX() + playerVX);
		} else {
			player.setY(player.getY() + playerVY);
		}

		if (Math.abs(scrollCountX) >= Start.WIDTH || Math.abs(scrollCountY) >= Start.HEIGHT) {

			mediator.endScroll();

			loadStageMap(player, nextDoor);

			switch (moveDir) {
			case RIGHT:
				player.setX(DIFF);
				break;
			case LEFT:
				player.setX(getWidth() - player.getWidth() - DIFF);
				break;
			case UP:
				player.setY(getHeight() - player.getHeight() - DIFF);
				break;
			case DOWN:
				player.setY(player.getHeight() + DIFF);
				break;
			default:
				break;
			}

			isScrolling = false;
		}
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		map.render((int) offSetX, (int) offSetY);
		mapShop.draw(g, offSetX, offSetY);

		for (MapMessage message : messageList) {
			message.draw(g, offSetX, offSetY);
		}

		for (MapNaname naname : nanameFloorList) {
			naname.draw(g, offSetX, offSetY);
		}

		for (MapHashigo hashigo : hashigoList) {
			hashigo.draw(g, offSetX, offSetY);
		}

		oldOffSetX = offSetX;
		oldOffSetY = offSetY;
	}

	/**
	 * スクロール中の描画処理
	 * 
	 * @param g
	 *            graphics
	 */
	public void drawScroll(Graphics g) {
		map.render((int) (oldOffSetX + scrollCountX), (int) (oldOffSetY + scrollCountY));

		nextMap.render((int) (nextMapX + scrollCountX), (int) (nextMapY + scrollCountY));
	}

	/**
	 * ショップに入る為のドア作成
	 * 
	 * @param str
	 *            座標とか
	 */
	public void makeShopDoor(String[] str) {
		float x = Integer.parseInt(str[1]);
		float y = Integer.parseInt(str[2]);

		mapShop.make(x, y);
	}

	/**
	 * 移動先作成
	 * 
	 * @param str
	 *            テキストファイル
	 * @param dir
	 *            移動する方向
	 */
	public void makeMoveFloor(String[] str, MOVE_DIR dir) {
		String mapFile = str[1];
		String spriteFile = str[2];
		String musicFile = str[3];

		MapInfo mapInfo;
		if (mapList.containsKey(mapFile)) {
			mapInfo = mapList.get(mapFile);
		} else {
			mapInfo = new MapInfo(mapFile, spriteFile, musicFile);
			mapList.put(mapFile, mapInfo);
		}

		dir.setMapInfo(mapInfo);
	}

	/**
	 * メッセージを表示するオブジェクトを作成し、リストに追加
	 * 
	 * @param str
	 *            座標とか
	 */
	public void makeMessage(String[] str) {
		int x = Integer.parseInt(str[1]);
		int y = Integer.parseInt(str[2]);
		String message = str[3];

		MapMessage mapMessage = new MapMessage(this);
		mapMessage.make(x, y, message);
		messageList.add(mapMessage);
	}

	// /**
	// * 斜めに移動できる床を作成し、リストに追加
	// * @param str
	// * @param type
	// */
	// public void makeNanameFloor(String str[]) {
	// int x = Integer.parseInt(str[1]);
	// int y = Integer.parseInt(str[2]);
	// MapNaname.TypeEnum type = MapNaname.TypeEnum.valueOf(str[3]);
	//
	// MapNaname naname = new MapNaname(this, type);
	// naname.make(x, y);
	// nanameFloorList.add(naname);
	// }

	/**
	 * 上キー押下時、ドアに接触していたかチェック 接触してる場合、ショップシーンに移動
	 *
	 * @param player
	 *            プレイヤー
	 */
	public void checkMove(Player player) {
		// ショップ
		if (mapShop.getIsUsing()) {
			if (isHit(mapShop, player)) {
				Log.debug("enter shop ");
				mediator.changeScene(MainStateImpl.SCENE.SHOP);
				return;
			}
		}
	}

	/**
	 * 触れたらメッセージ表示
	 *
	 * @param player
	 *            プレイヤー
	 */
	public void checkMessage(Player player) {
		for (MapMessage message : messageList) {
			if (isHit(message, player)) {
				message.setHitPlayer();
			} else {
				message.setNotHitPlayer();
			}
		}
	}

	/**
	 * 斜め床とのあたり判定チェック
	 * 
	 * @param player
	 */
	public void checkNanameFloor(Player player) {
		for (MapNaname nanameFloor : nanameFloorList) {
			if (isHit(nanameFloor, player)) {
				nanameFloor.hitSprite(player);
			}
		}
	}
	
	/**
	 * TODO 不要かも
	 * 
	 * プレイヤーと梯子のチェック
	 * @param player
	 */
	public boolean checkHashigo(Player player) {
		for (MapHashigo hashigo : hashigoList) {
			if (isHit(hashigo, player)) {
				hashigo.hitSprite(player);
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 移動先のマップとスプライトの読込。 メディエータクラスで、各スプライト作成する。
	 *
	 * @param player
	 *            プレイヤー
	 * @param mapInfo
	 *            マップ情報
	 */
	public void loadStageMap(Player player, MapInfo mapInfo) {
		map = mapInfo.getTiledMap();
		nextMap = map;

		// Log.debug("mapWidth " + map.getWidth() * TILE_SIZE);

		BGMEnum type = BGMEnum.valueOf(mapInfo.getMusic());
		SoundManager.playBGM(type);

		col = map.getWidth();
		row = map.getHeight();

		// リスト初期化
		clearList();

		// 通行不可能な場所設定
		setBlock();

		// アイテム読み込み
		mediator.laodItems(mapInfo.getList());
	}

	/**
	 * tmxファイルから、通行不可能な位置を設定 レイヤー0の左上(0,0)のタイルを通行不可能なブロックとする。
	 */
	private void setBlock() {
		int id = map.getTileId(0, 0, 0); // 通行不可能なブロックのタイル
		int breakId = map.getTileId(0, 2, 0); // 通行不可能なブロックのタイル

		blocked = new boolean[row][col];
		for (int my = 0; my < row; my++) {
			for (int mx = 0; mx < col; mx++) {
				blocked[my][mx] = false;

				if (map.getTileId(mx, my, 0) == id) {
					blocked[my][mx] = true;
				}
				if (map.getTileId(mx, my, 0) == breakId) {
					blocked[my][mx] = true;
				}

				// 斜めの床の作成
				if (map.getTileId(mx, my, 0) == ID_RIGHT_UP) {
					MapNaname naname = new MapNaname(this, MapNaname.TypeEnum.RIGHT_UP);
					naname.make(mx * TILE_SIZE, my * TILE_SIZE);
					nanameFloorList.add(naname);
				} else if (map.getTileId(mx, my, 0) == ID_RIGHT_DOWN) {
					MapNaname naname = new MapNaname(this, MapNaname.TypeEnum.RIGHT_DOWN);
					naname.make(mx * TILE_SIZE, my * TILE_SIZE);
					nanameFloorList.add(naname);
//				} else if (map.getTileId(mx, my, 0) == ID_LEFT_UP) {
//					MapNaname naname = new MapNaname(this, MapNaname.TypeEnum.LEFT_UP);
//					naname.make(mx * TILE_SIZE, my * TILE_SIZE);
//					nanameFloorList.add(naname);
//				} else if (map.getTileId(mx, my, 0) == ID_LEFT_DOWN) {
//					MapNaname naname = new MapNaname(this, MapNaname.TypeEnum.LEFT_DOWN);
//					naname.make(mx * TILE_SIZE, my * TILE_SIZE);
//					nanameFloorList.add(naname);
				}

				// 梯子の作成
				if (map.getTileId(mx, my, 0) == ID_HASHIGO) {
					MapHashigo hashigo = new MapHashigo(this);
					hashigo.make(mx * TILE_SIZE, my * TILE_SIZE);
					hashigoList.add(hashigo);
				}

			}
		}
	}

	/**
	 * ドア、メッセージリスト、斜め床リストをクリア 移動先マップをクリア
	 */
	private void clearList() {
		messageList.clear();
		mapShop.delete();
		nanameFloorList.clear();
		hashigoList.clear();

		MOVE_DIR.RIGHT.setMapInfo(null);
		MOVE_DIR.LEFT.setMapInfo(null);
		MOVE_DIR.UP.setMapInfo(null);
		MOVE_DIR.DOWN.setMapInfo(null);
	}

	/**
	 * スプライトの座標をマップ座標に変換する。
	 *
	 * @param sprite スプライトクラス
	 * @param newX 移動後のX座標
	 * @param newY 移動後のY座標
	 * @return 移動ポイント
	 */
	public Point getTileCollision(AbstractSprite sprite, double newX, double newY) {
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);

		double fromX = Math.min(sprite.getX(), newX);
		double fromY = Math.min(sprite.getY(), newY);
		double toX = Math.max(sprite.getX(), newX);
		double toY = Math.max(sprite.getY(), newY);

		int fromTileX = pixelsToTiles(fromX);
		int fromTileY = pixelsToTiles(fromY);
		int toTileX = pixelsToTiles(toX + sprite.getWidth() - 1);
		int toTileY = pixelsToTiles(toY + sprite.getHeight() - 1);

		for (int x = fromTileX; x <= toTileX; x++) {
			for (int y = fromTileY; y <= toTileY; y++) {
				if (x < 0 || x >= col) {
					return new Point(x, y);
				}

				if (y < 0 || y >= row) {
					return new Point(x, y);
				}

				if (blocked[y][x] == true) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}

	/**
	 * スプライトの座標をマップ座標に変換する。
	 *
	 * @param sprite スプライトクラス
	 * @param newX 移動後のX座標
	 * @param newY 移動後のY座標
	 * @return 移動ポイント
	 */
	public Point checkTileCollisionHashigo(AbstractSprite sprite, double newX, double newY) {
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);

		double fromX = Math.min(sprite.getX(), newX);
		double fromY = Math.min(sprite.getY(), newY);
		double toX = Math.max(sprite.getX(), newX);
		double toY = Math.max(sprite.getY(), newY);

		int fromTileX = pixelsToTiles(fromX);
		int fromTileY = pixelsToTiles(fromY);
		int toTileX = pixelsToTiles(toX + sprite.getWidth() - 1);
		int toTileY = pixelsToTiles(toY + sprite.getHeight() - 1);

		for (int x = fromTileX; x <= toTileX; x++) {
			for (int y = fromTileY; y <= toTileY; y++) {
				//梯子があるかチェック
				if(x < 0) x = 0;
				if(y < 0) y = 0;
				
				if (map.getTileId(x, y, 0) == ID_HASHIGO) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}
	
	/**
	 * プレイヤーが接触しているかチェック
	 * 
	 * @param sprite
	 *            対象スプライト
	 * @param player
	 *            プレイヤー
	 * @return result 当たってる場合はtrue
	 */
	public static boolean isHit(AbstractSprite sprite, Player player) {
		if (sprite.getRectangle().intersects(player.getRectangle())) {
			// 当たってる場合は、移動
			return true;
		}

		return false;
	}

	/**
	 * ゲーム座標をマップ座標に変換
	 *
	 * @param pixels
	 *            現在の座標
	 * @return pixels / TILE_SIZE
	 */
	public static int pixelsToTiles(double pixels) {
		return (int) Math.floor(pixels / TILE_SIZE);
	}

	/**
	 * タイルの座標からゲーム座標に変換
	 * 
	 * @param x
	 * @return x * TILE_SIZE
	 */
	public static int tilesToPixels(int x) {
		return x * TILE_SIZE;
	}

	/**
	 * マップの横幅取得
	 * 
	 * @return width
	 */
	public int getWidth() {
		return col * TILE_SIZE;
	}

	/**
	 * マップの縦幅取得
	 * 
	 * @return height
	 */
	public int getHeight() {
		return row * TILE_SIZE;
	}

	/**
	 * 現在位置から、マップIDを取得 現在位置をタイルの座標に変換し、レイヤー0のIDを取得
	 *
	 * @param x X座標
	 * @param y Y座標
	 * @return mapId レイヤー0のマップID
	 */
	public int getMapId(float x, float y) {
		int mapX = pixelsToTiles(x);
		int mapY = pixelsToTiles(y);

		return map.getTileId(mapX, mapY, 0);
	}

	/**
	 * スクロール方向をセット
	 * 
	 * @param moveDir
	 *            移動方向
	 */
	public void setMoveDir(MOVE_DIR moveDir) {
		nextDoor = moveDir.getMapInfo();

		// 未設定の場合処理を中断
		if (nextDoor == null) {
			return;
		}

		this.moveDir = moveDir;

		// 初期化
		scrollCountX = 0;
		scrollCountY = 0;
		nextMapX = 0;
		nextMapY = 0;

		nextMap = nextDoor.getTiledMap();

		switch (moveDir) {
		case RIGHT:
			nextMapX = Start.WIDTH;
			break;
		case LEFT:
			nextMapX = -nextMap.getWidth() * TILE_SIZE;
			break;
		case DOWN:
			nextMapY = Start.HEIGHT;
			break;
		case UP:
			nextMapY = -nextMap.getHeight() * TILE_SIZE;
			break;
		default:
			return;
		}

		// スクロール開始
		isScrolling = true;
	}
}
