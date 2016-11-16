package com.game.actiongame.Map;

import java.util.LinkedList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import com.game.actiongame.Utility.FileReader;

/**
 * txtファイルから読み込んだ文字列
 * tmxファイルから読み込んだマップファイル
 * 音楽ファイルの情報
 * を保持するクラス
 *
 * @author moromorocco
 */
public class MapInfo {
	/** テキストファイル読込ユーティリティ */
	private FileReader fileReader = new FileReader();
	private LinkedList<String> spriteList;
	private TiledMap tiledMap;
	private String music;

	/**
	 * コンストラクタ
	 * @param mapFile 読み込むマップのファイル名
	 * @param spriteFile 読み込むテキストのファイル名
	 */
	public MapInfo(String mapFile, String spriteFile, String music) {
		Log.debug("mapFile " + mapFile);
		Log.debug("spriteFile " + spriteFile);
		Log.debug("music " + music);

		//マップファイルの読込
		final String DIR = "mapData/";
		try {
			tiledMap = new TiledMap(DIR + mapFile);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		//テキストファイルの読込
		spriteList = fileReader.readFile(spriteFile);

		//音楽情報のセット
		this.music = music;
	}

	/**
	 * 読込んだマップ情報を取得
	 * @return tiledMap マップ
	 */
	public TiledMap getTiledMap() {
		return tiledMap;
	}

	/**
	 * 読込んだスプライトリストを取得
	 * @return spriteList スプライトリスト
	 */
	public LinkedList<String> getList() {
		return spriteList;
	}

	/**
	 * 音楽情報の取得
	 * @return music
	 */
	public String getMusic() {
		return music;
	}
}
