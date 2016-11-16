package com.game.actiongame.Utility;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ImageLoaderUtility {
	private static final int IMAGE_SIZE_32 = 32;
	/** 読み込んだ画像を保管 */
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	/** 読み込んだスプライトシートを保管 */
	private static HashMap<String, SpriteSheet> sheetMap = new HashMap<String, SpriteSheet>();
	private static SheetFactory factory = new SheetFactory();
	private static ImageFactory imageFactory = new ImageFactory();

	/** コンストラクタ */
	private ImageLoaderUtility() {
	}

	/**
	 * 画像を読み込む
	 * 
	 * @param type 読み込む種類
	 * @return 読み込んだ画像
	 */
	public static Image loadImage(ImageTypeEnum type) {

		if (imageMap.containsKey(type.toString())) {
			return imageMap.get(type.toString());
		}

		return imageFactory.getImage(type.getFilename());
	}
	
	/**
	 * 親スプライトシートを読み込み、一部切り出す
	 * @param koEnum 読み込むシートの種類
	 * @return 読み込んだ画像
	 */
	public static SpriteSheet loadKoSheet(KoSheetInterface32 koEnum) {
		SpriteSheet sheet = loadOyaSheet(koEnum.getOya());
		
		if(sheet != null) {
			return new SpriteSheet(sheet.getSubImage(koEnum.getSubX(), koEnum.getSubY()), IMAGE_SIZE_32, IMAGE_SIZE_32);
		}
		
		return null;
	}

	
	/**
	 * 親スプライトシートを読み込み、一部切り出す
	 * @param koEnum 読み込むシートの種類
	 * @return 読み込んだ画像
	 */
	public static SpriteSheet loadKoSheet(KoSheetInterface koEnum) {
		SpriteSheet sheet = loadOyaSheet(koEnum.getOya());
		
		if(sheet != null) {
			return new SpriteSheet(sheet.getSubImage(koEnum.getSubX(), koEnum.getSubY()), koEnum.getWidth(), koEnum.getHeight());
		}
		
		return null;
	}
	
	/**
	 * 大元の親スプライトシートの読み込む
	 * @param oyaEnum 読み込む種類
	 * @return 読み込んだ画像
	 */
	public static SpriteSheet loadOyaSheet(OyaSheetEnum oyaEnum) {
		if (sheetMap.containsKey(oyaEnum.toString())) {
			return sheetMap.get(oyaEnum.toString());
		}

		SpriteSheet sheet = factory.getSheet(oyaEnum);
		sheetMap.put(oyaEnum.toString(), sheet);

		return sheet;
	}
}
