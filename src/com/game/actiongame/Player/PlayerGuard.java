package com.game.actiongame.Player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.game.actiongame.Map.GameMap;
import com.game.actiongame.Player.Player.DIR_TYPE;
import com.game.actiongame.Utility.AbstractSprite;
import com.game.actiongame.Utility.ImageLoaderUtility;
import com.game.actiongame.Utility.ImageTypeEnum;

public class PlayerGuard extends AbstractSprite {
	private final int COOL_TIME = 10;
	private final int LIFE = 5;
	private Player player;
	private Image right;
	private Image left;
	private int coolTime;
	private int life;

	public PlayerGuard(GameMap map, Player player) {
		super(map);

		this.player = player;
	}

	@Override
	public void loadImage() {
//		right = ImageLoaderUtility.getImage(ImageType.ITEM_SEALD_RIGHT);
//		left = ImageLoaderUtility.getImage(ImageType.ITEM_SEALD_LEFT);
		
		right = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_SEALD_RIGHT);
		left = ImageLoaderUtility.loadImage(ImageTypeEnum.ITEM_SEALD_LEFT);
		
		img = right;
	}

	@Override
	public void draw(Graphics g, float offSetX, float offSetY) {
		super.draw(g, offSetX, offSetY);

//		g.drawRect(x, y, getWidth(), getHeight());
	}

	@Override
	public void update(int delta) {
		if(coolTime > 0) {
			coolTime--;
		}

		if(player.getDir() == DIR_TYPE.RIGHT) {
			x = player.getX() + player.getWidth() - getWidth() / 2;
			img = right;
		} else {
			x = player.getX() - getWidth() / 2;
			img = left;
		}
		y = player.getY() + player.getHeight() - getHeight();
	}

	public void make() {
		super.make(player.getX(), player.getY());

		float setX;
		float setY = y;

		if(player.getDir() == DIR_TYPE.RIGHT) {
			setX = player.getX() + getWidth() / 2;
		} else {
			setX = player.getX() - getWidth() / 2;
		}

		this.x = setX;
		this.y = setY;

		life = LIFE;
	}

	@Override
	public void hitSprite(AbstractSprite sprite) {
		life--;

		coolTime = COOL_TIME;

		if(life < 0) {
			delete();
		}
	}
}
