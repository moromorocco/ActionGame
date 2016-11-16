package com.game.actiongame.Enemy;
import com.game.actiongame.Base.MediatorInterface;
import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Utility.AbstractManager;
import com.game.actiongame.Utility.AbstractSprite;

/**
 * 敵管理クラス
 * @author moromorocco
 *
 */
public class EnemyManager extends AbstractManager {
	/** 作成する敵の種類 */
	public enum TYPE {
		NORMAL {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeNormal(map, mediator);
			}
		},
		ATTACK {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeAttack(map, mediator, this);
			}
		},
		NAGE {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeAttack(map, mediator, this);
			}
		},
		FLYING {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeFlying(map, mediator);
			}
		},
		TURN {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeTurn(map, mediator);
			}
		},
		TRAP {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeTrap(map, mediator);
			}
		},
		MOVE_LR {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeMoveLR(map, mediator);
			}
		},
		MOVE_SIN {
			@Override
			public AbstractSprite getInstance(GameMap map, MediatorInterface mediator) {
				return new EnemyTypeSinmove(map, mediator);
			}
		}
		;

		public abstract AbstractSprite getInstance(GameMap map, MediatorInterface mediator);
	}

	public EnemyManager(GameMap map, MediatorInterface mediator) {
		super(map, mediator);
	}

	public void makeEnemy(String[] str) {
		int x = Integer.parseInt(str[1]);
		int y = Integer.parseInt(str[2]);
		int vx = Integer.parseInt(str[3]);
		int vy = Integer.parseInt(str[4]);
		TYPE type = TYPE.valueOf(str[5]);

		BaseEnemy enemy = (BaseEnemy)super.getSprite();
		AbstractSprite sprite = type.getInstance(map, mediator);
		sprite.make(x, y, vx, vy);
		enemy.setEnemy(sprite, 1);
	}

	public void makeEnemy(float x, float y, float vx, float vy, TYPE type) {
		BaseEnemy enemy = (BaseEnemy)super.getSprite();
		AbstractSprite sprite = type.getInstance(map, mediator);
		sprite.make(x, y, vx, vy);
		enemy.setEnemy(sprite, 1);
	}

	@Override
	protected AbstractSprite createSprite(int spriteCount) {
		return new BaseEnemy(map, mediator);
	}
}
