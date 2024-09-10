package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.GameCamera;
import tnt.hollowbit.spacegame.tools.ScrollingBackground;

public class SpaceGame extends Game {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	public static final boolean IS_MOBOLE = true;
	public static boolean IS_MOBILE = false;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	public GameCamera cam;  // Use GameCamera only
 
	@Override
	public void create() {
		batch = new SpriteBatch();
		cam = new GameCamera(WIDTH, HEIGHT);  // Initialize the camera

		// Detect if the game is running on mobile
		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
			IS_MOBILE = true;
		}

		// Initialize scrolling background and set the screen
		this.scrollingBackground = new ScrollingBackground();
		int score = 0;
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined());  // Set the camera's projection matrix
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		cam.update(width, height);  // Update the camera on resize
		super.resize(width, height);
	}
}
