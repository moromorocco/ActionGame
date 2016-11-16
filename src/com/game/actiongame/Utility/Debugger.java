package com.game.actiongame.Utility;

/**
 * デバッグ時のみメッセージを出力
 * @author moromorocco
 *
 */
public class Debugger {
	private Debugger() {
	}

	public static boolean isEnabled() {
		return true;
	}

	public static void log(Object obj) {
		if(Debugger.isEnabled()) {
			System.out.println(obj.toString());
		}
	}
}
