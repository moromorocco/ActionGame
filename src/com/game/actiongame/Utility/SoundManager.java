package com.game.actiongame.Utility;

import java.util.HashMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.Sound;

public class SoundManager implements MusicListener {
	/** 効果音を保存するハッシュマップ */
	private static HashMap<String, Sound> SEMap = new HashMap<String, Sound>();
	/** BGMを保存するハッシュマップ */
	private static HashMap<String, Music> BGMMap = new HashMap<String, Music>();
	
	/** ファクトリ */
	private static BGMFactory bgmFactory = new BGMFactory();
	private static SoundFactory soundFactory = new SoundFactory();
	
	/** 再生する効果音 */
	public static Sound sound = new SoundFactory().getSound(SoundEnum.PLAYER_SHOT.getFilename());
	/** 再生する音楽 */
	public static Music bgm = new BGMFactory().getMusic(BGMEnum.STAGE1.getFilename());

	/** SEの大きさ */
	private static float volumeSE = 1.0f;
	/** BGMの大きさ */
	private static float volumeBGM = 1.0f;

	/** 現在再生中のBGM */
	private static BGMEnum nowBGMType = BGMEnum.STAGE1;

	
	
	/**
	 * SE再生
	 * @param type SE種類
	 */
	public static void playSE(SoundEnum type) {
		if(SEMap.containsKey(type.toString()) == false) {
			sound = soundFactory.getSound(type.getFilename());
			SEMap.put(type.toString(), sound);
		} else {
			sound = SEMap.get(type.toString());
		}

		sound.play(1.0f, volumeSE);
	}

	public static void stopSE() {
		sound.stop();
	}

	public static void setVolumeSE(int volume) {
		volumeSE = (float)volume / 100;
	}

	public static void setVolumeSE(float volume) {
		volumeSE = volume;
	}

	/**
	 * BGM再生
	 * @param type BGM種類
	 */
	public static void playBGM(BGMEnum type) {

		if(type == nowBGMType) {
			return;
		}

		if(BGMMap.containsKey(type.toString()) == false) {
//			bgm = type.getMusic();
			bgm = bgmFactory.getMusic(type.getFilename());
			BGMMap.put(type.toString(), bgm);
		} else {
			bgm = BGMMap.get(type.toString());
		}

		nowBGMType = type;

		bgm.setVolume(volumeBGM);
		bgm.play();
		bgm.loop();
	}

	/** 停止 */
	public static void stopBGM() {
		bgm.stop();
	}

	/**
	 * 音量セット
	 * @param volume 音量
	 */
	public static void setVolumeBGM(int volume) {
		volumeBGM = ((float)volume / 100);
		bgm.setVolume(volumeBGM);
	}

	/**
	 * 音量セット
	 * @param volume 音量
	 */
	public static void setVolumeBGM(float volume) {
		volumeBGM = volume;
		bgm.setVolume(volumeBGM);
	}

	@Override
	public void musicEnded(Music music) {
		music.play();
	}

	@Override
	public void musicSwapped(Music arg0, Music arg1) {
	}

	public static BGMEnum getBGMType() {
		return nowBGMType;
	}
}

