package com.game.actiongame.Utility;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 * キーリスナ
 * @author moromorocco
 *
 */
public final class MyKeyListener implements KeyListener{
	private static boolean isPressLeft = false;
	private static boolean isPressUp= false;
	private static boolean isPressDown = false;
	private static boolean isPressRight = false;
	private static boolean isPressZ = false;
	private static boolean isPressX = false;
	private static boolean isPressC = false;
	private static boolean isPressLSHIFT = false;

	public static boolean isPressJ = false;
	public static boolean isPressK = false;
	public static boolean isPressL = false;
	public static boolean isPressM = false;

	public void inputEnded() {
	}

	public void inputStarted() {
	}

	public boolean isAcceptingInput() {
		return true;
	}

	public void setInput(Input arg0) {
	}

	public void keyPressed(int key, char ch) {
		switch(key) {
			case Input.KEY_LEFT:
				isPressLeft = true;
				break;
			case Input.KEY_UP:
				isPressUp = true;
				break;
			case Input.KEY_DOWN:
				isPressDown = true;
				break;
			case Input.KEY_RIGHT:
				isPressRight = true;
				break;
			case Input.KEY_Z:
				isPressZ = true;
				break;
			case Input.KEY_X:
				isPressX = true;
				break;
			case Input.KEY_C:
				isPressC = true;
				break;
			case Input.KEY_LSHIFT:
				isPressLSHIFT = true;
				break;
			case Input.KEY_J:
				isPressJ = true;
				break;
			case Input.KEY_K:
				isPressK = true;
				break;
			case Input.KEY_L:
				isPressL = true;
				break;
			case Input.KEY_M:
				isPressM = true;
				break;
			default:
		}
	}

	public void keyReleased(int key, char ch) {
		switch(key) {
			case Input.KEY_LEFT:
				isPressLeft = false;
				break;
			case Input.KEY_UP:
				isPressUp = false;
				break;
			case Input.KEY_DOWN:
				isPressDown = false;
				break;
			case Input.KEY_RIGHT:
				isPressRight = false;
				break;
			case Input.KEY_Z:
				isPressZ = false;
				break;
			case Input.KEY_X:
				isPressX = false;
				break;
			case Input.KEY_C:
				isPressC = false;
				break;
			case Input.KEY_LSHIFT:
				isPressLSHIFT = false;
				break;
			case Input.KEY_J:
				isPressJ = false;
				break;
			case Input.KEY_K:
				isPressK = false;
				break;
			case Input.KEY_L:
				isPressL = false;
				break;
			case Input.KEY_M:
				isPressM = false;
				break;
			default:
		}
	}

	public static boolean isPressUp() {
		return isPressUp;
	}

	public static boolean isPressDown() {
		return isPressDown;
	}

	public static boolean isPressLeft() {
		return isPressLeft;
	}

	public static boolean isPressRight() {
		return isPressRight;
	}

	public static boolean isPressZ() {
		return isPressZ;
	}

	public static boolean isPressX() {
		return isPressX;
	}

	public static boolean isPressC() {
		return isPressC;
	}

	public static boolean isPressLShift() {
		return isPressLSHIFT;
	}

	public static boolean isPressJ() {
		return isPressJ;
	}

	public static boolean isPressK() {
		return isPressK;
	}

	public static boolean isPressL() {
		return isPressL;
	}

	public static boolean isPressM() {
		return isPressM;
	}

	public static boolean checkNaname() {
		if(isPressUp && isPressLeft) return true;
		if(isPressUp && isPressRight)  return true;
		if(isPressDown && isPressLeft) return true;
		if(isPressDown && isPressRight)  return true;

		return false;
	}
}
