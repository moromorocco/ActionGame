package com.game.actiongame.Boss;

import com.game.actiongame.Boss.BossManagerImpl.BossEnum;
import com.game.actiongame.Map.GameMap;

public class BossFactory {
	public AbstractBoss makeBoss(BossEnum type, GameMap gameMap) {
		switch(type) {
		case BOSS1:
			return new Boss1(gameMap);
		case BOSS2:
			return new Boss2(gameMap);
		case BOSS3:
			break;
		default:
			break;
		}
		
		return null;
	}
}
