package com.game.actiongame.ShotPattern;

public class ShotTypeFactory {

	public MakeShotInterface makeInstance(ShotTypeEnum type) {
		switch (type) {
		case PLAYER_NERAI:
			return new ShotPlayer();
		case ENEMY_NAGE:
			return new ShotEnemyNage();
		case ENEMY_FLYING:
			return new ShotEnemyFlying();
		case ENEMY_TURN:
			return new ShotEnemyTurn();
		case NOTHING:
			break;
		default:
			break;
		}

		return null;
	}

}
