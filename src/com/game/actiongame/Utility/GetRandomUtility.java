package com.game.actiongame.Utility;
import java.util.Random;

/**
 * 乱数を取得するユーティリティクラス
 * @author moromorocco
 *
 */
public class GetRandomUtility {
	private static Random rand = new Random(System.currentTimeMillis());

	private GetRandomUtility() {
	}

	public static int getRand(int num) {
		return rand.nextInt(num);
	}
}
