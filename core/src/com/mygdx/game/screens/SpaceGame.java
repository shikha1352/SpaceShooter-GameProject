package com.mygdx.game.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tools.GameCamera;

import tnt.hollowbit.spacegame.tools.ScrollingBackground;


public class SpaceGame extends Game {

	public static final int WIDTH=480;
	public static final int HEIGHT=720;
	public static boolean IS_MOBOLE = false;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;

	public GameCamera cam;
    public Object camera;


	@Override
	public void create () {

		batch = new SpriteBatch();
		cam = new GameCamera(WIDTH, HEIGHT);


		if( Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) IS_MOBOLE = true;
		IS_MOBOLE = true;

		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined());
		super.render();
	}
 
	@Override
	public void resize(int width, int height) {
		cam.update(width, height);
		super.resize(width, height);
	}


}
