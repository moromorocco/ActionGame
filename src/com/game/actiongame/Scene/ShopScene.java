package com.game.actiongame.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;

import com.game.actiongame.Base.GameSceneInterface;
import com.game.actiongame.Base.MainStateImpl;
import com.game.actiongame.Base.MainStateInterface;
import com.game.actiongame.Base.Start;
import com.game.actiongame.Player.Player;
import com.game.actiongame.Utility.Animation2;
import com.game.actiongame.Utility.BGMEnum;
import com.game.actiongame.Utility.FontUtility;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;
import com.game.actiongame.Utility.OyaSheetEnum;
import com.game.actiongame.Utility.SoundManager;

/**
 * アイテム購入画面
 * @author moromorocco
 *
 */
public class ShopScene implements GameSceneInterface {
	private final int WIDTH = Start.WIDTH;		//画面の横幅
	private final int HEIGHT = Start.HEIGHT;	//画面の縦幅
	private final int DIFF = 5;
	private final String DEFAULT_MESSAGE =
			"いらっしゃいませ\n"
			+ "購入するにはZキー\n"
			+ "店を出るにはXキー、もしくは↓キーを押して下さい";
	private MainStateInterface mainState;
	private Player player;
	private UnicodeFont font;
	private Image backImage;
	private Image shopChar;
	private Image seald;
	private Image mitei1;
	private Image mitei2;
	private Animation2 cursol;
	private String message;
	private boolean isShowingKounyuuMenu;
	private int kounyuuSelect;
	private ITEM_LIST selectPoint;
	private BGMEnum bgmType;

	/** アイテム */
	private enum ITEM_LIST {
		SEALD(100, 10, "正面からの攻撃を防いだり、被ダメを軽減してくれます。"),
		MITEI1(150, 999, "未入荷のアイテムです。"),
		MITEI2(200, 999, "未入荷のアイテムです。");

		final int drawPoint;	//描画位置
		final int coins;		//金額
		final String setumei;	//選択時の説明文

		ITEM_LIST(int drawPoint, int coins, String setumei) {
			this.drawPoint = drawPoint;
			this.coins = coins;
			this.setumei = setumei;
		}

		public int getDrawPoint() {
			return drawPoint;
		}

		public int getCoins() {
			return coins;
		}

		public String getSetumei() {
			return setumei;
		}
	}

	/**
	 * コンストラクタ
	 * @param mainState メインクラス
	 * @param player プレイヤー
	 */
	public ShopScene(MainStateInterface mainState, Player player) {
		this.mainState = mainState;
		this.player = player;

		loadImage();

		font = FontUtility.getFont();

		init();
	}

	/** 初期化 */
	public void init() {
		selectPoint = ITEM_LIST.SEALD;

		message = DEFAULT_MESSAGE;

		isShowingKounyuuMenu = false;

		kounyuuSelect = 0;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		g.setFont(font);

		backImage.draw(0, 0);
		shopChar.draw(250, 50);
		seald.draw(ITEM_LIST.SEALD.getDrawPoint() - seald.getWidth()/2, 200);
		g.drawString("" + ITEM_LIST.SEALD.getCoins(), ITEM_LIST.SEALD.getDrawPoint()-10, 230);

		mitei1.draw(ITEM_LIST.MITEI1.getDrawPoint() - mitei1.getWidth()/2, 200);
		g.drawString("" + ITEM_LIST.MITEI1.getCoins(), ITEM_LIST.MITEI1.getDrawPoint()-10, 230);

		mitei2.draw(ITEM_LIST.MITEI2.getDrawPoint() - mitei2.getWidth()/2, 200);
		g.drawString("" + ITEM_LIST.MITEI2.getCoins(), ITEM_LIST.MITEI2.getDrawPoint()-10, 230);

		int cursolPoint = selectPoint.getDrawPoint() - cursol.getWidth()/2;

		cursol.draw(cursolPoint, 250);

		int messageX = 0;
		int messageY = 350;
		g.setColor(Color.white);
        g.fillRect(messageX, messageY, WIDTH, 100);

		g.setColor(Color.black);
        g.fillRect(messageX + DIFF, messageY + DIFF, WIDTH - DIFF * 2, 100 - DIFF * 2);

    	g.setColor(Color.white);
    	g.drawString(message, 20 + DIFF + 5 , messageY + DIFF + 5);

    	if(isShowingKounyuuMenu) {
    		int selectX = 200;
    		int selectY = 200;
    		int selectWidth = 200;
    		int selectHeight = 130;

    		g.setColor(Color.white);
            g.fillRect(selectX, selectY, selectWidth, selectHeight);

    		g.setColor(Color.black);
            g.fillRect(selectX + DIFF, selectY + DIFF, selectWidth - DIFF * 2, selectHeight - DIFF * 2);

            String selectMessage = "購入しますか？\n\n"
            		+ "はい\n"
            		+ "いいえ";
        	g.setColor(Color.white);
        	g.drawString(selectMessage, selectX + 20 + DIFF + 5 , selectY + DIFF + 5);

			g.drawRect(selectX + DIFF, selectY - (kounyuuSelect * 30) + 80, selectWidth - DIFF * 2, 30);
    	}
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public void keyPressed(int key, char c) {
		//店を出る。キャンセル
		if(key == Input.KEY_ESCAPE ||
				key == Input.KEY_X) {

			if(isShowingKounyuuMenu) {
				isShowingKounyuuMenu = false;
			} else {
//				mainState.changeScene(MainStateImpl.SCENE.GAME);
				exitShop();
			}
		}

		//購入
		if(key == Input.KEY_Z) {
			if(isShowingKounyuuMenu) {
				//TODO 「いいえ」選択時の処理

				//購入処理
				if(player.getCoins() < selectPoint.getCoins()) {
					message = "お金が足りません";
					return;
				} else {
					//お金減らす
					player.addCoins(-selectPoint.getCoins());
				}

				switch(selectPoint) {
					case SEALD:
						mainState.getSeald();
						break;
					case MITEI1:
						break;
					case MITEI2:
						break;
					default:
						break;
				}

				isShowingKounyuuMenu = false;
			} else {
				message = selectPoint.getSetumei();
				kounyuuSelect = 0;

				//シールドしかないので、シールド以外は何もしない
				if(selectPoint == ITEM_LIST.SEALD) {
					isShowingKounyuuMenu = true;
				}
			}
		}

		//カーソル移動
		if(key == Input.KEY_RIGHT) {
			message = DEFAULT_MESSAGE;

			switch(selectPoint) {
				case SEALD:
					selectPoint = ITEM_LIST.MITEI1;
					break;
				case MITEI1:
					selectPoint = ITEM_LIST.MITEI2;
					break;
				case MITEI2:
					selectPoint = ITEM_LIST.MITEI2;
					break;
				default:
					break;
			}
		}

		if(key == Input.KEY_LEFT) {
			message = DEFAULT_MESSAGE;

			switch(selectPoint) {
				case SEALD:
					break;
				case MITEI1:
					selectPoint = ITEM_LIST.SEALD;
					break;
				case MITEI2:
					selectPoint = ITEM_LIST.MITEI1;
					break;
				default:
					break;
			}
		}

		//上キー
		if(key == Input.KEY_UP) {
			if(kounyuuSelect == 0) {
				kounyuuSelect = 1;
			}
		}

		//下キー
		if(key == Input.KEY_DOWN) {
			if(isShowingKounyuuMenu) {
				if(kounyuuSelect == 1) {
					kounyuuSelect = 0;
				}
			} else {
				exitShop();
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
	}

	public void saveBGM(BGMEnum bgmType) {
		this.bgmType = bgmType;
	}

	/* 画像の読込 */
	private void loadImage() {
//		backImage = ImageLoaderUtility.getImage(ImageType.SHOP_BACK);
//		shopChar = ImageLoaderUtility.getImage(ImageType.SHOP_CHAR);
//		seald = ImageLoaderUtility.getImage(ImageType.ITEM_SEALD);
//		mitei1 = ImageLoaderUtility.getImage(ImageType.ITEM_MITEI);
//		mitei2 = ImageLoaderUtility.getImage(ImageType.ITEM_MITEI);
		
		backImage = ImageLoaderUtility.loadImage(ImageTypeEnum.SHOP_BACK);
		shopChar = ImageLoaderUtility.loadImage(ImageTypeEnum.SHOP_CHAR);
		seald = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_SEALD);
		mitei1 = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_MITEI);
		mitei2 = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_MITEI);

		SpriteSheet sheet = ImageLoaderUtility.loadOyaSheet(OyaSheetEnum.SHOP_CURSOL);
		
		cursol = new Animation2(sheet, 60);
	}

	private void exitShop() {
		mainState.changeScene(MainStateImpl.SCENE.GAME);

		SoundManager.playBGM(bgmType);
	}
}
