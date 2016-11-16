package com.game.actiongame.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * ファイル読込ユーティリティクラス
 * @author moromorocco
 *
 */
public class FileReader {
	private static final String DIR = "/mapSprite/";

	public LinkedList<String> readFile(String filename) {
		LinkedList<String> list = new LinkedList<String>();

		//開発時
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(
						this.getClass().getClassLoader().getResourceAsStream(filename)))) {

			Debugger.log("file : " + filename);

////		JARファイル作成時コメントを外す
//		System.out.println("file : " + filename);
//		try(BufferedReader br = new BufferedReader(
//				new InputStreamReader(
//						FileReader.class.getClass().getResourceAsStream(DIR + filename)))) {

			String line;
			while((line = br.readLine()) != null) {
				//空白行は読み飛ばす
				if(line.equals(""))		continue;
				//コメント行は読み飛ばす
				char first = line.charAt(0);
				if(first == '#')			continue;

				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
}
