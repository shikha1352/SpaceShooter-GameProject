package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.screens.MainMenuScreen;

import tnt.hollowbit.spacegame.tools.ScrollingBackground;


public class SpaceGame extends Game {

	public static final int WIDTH=480;
	public static final int HEIGHT=720;
	public static boolean IS_MOBOLE = false;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	private OrthographicCamera cam;
	private StretchViewport viewport;

	@Override
	public void create () {

		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		viewport = new StretchViewport(WIDTH, HEIGHT, cam);
		viewport.apply();
		cam.position.set(WIDTH/2, HEIGHT/2, 0);
		cam.update();


		if( Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) IS_MOBOLE = true;
		IS_MOBOLE = true;

		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined);
		super.render();
	}
 
	@Override
	public void resize(int width, int height) {
		this.scrollingBackground.resize(width, height);
		viewport.update(WIDTH, HEIGHT);
		super.resize(width, height);
	}


}
