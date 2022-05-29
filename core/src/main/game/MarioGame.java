package main.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import main.game.Screens.PlayScreen;

public class MarioGame extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public SpriteBatch sb;

	@Override
	public void create() {
		sb = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
