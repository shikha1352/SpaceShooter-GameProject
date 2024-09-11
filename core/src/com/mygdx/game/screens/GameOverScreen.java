package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import tnt.hollowbit.spacegame.tools.ScrollingBackground;

import com.badlogic.gdx.Preferences;

public class GameOverScreen implements Screen {
    
    private static final int MIRROR_WIDTH = 350;
    private static final int MIRROR_HIGHT = 100;
    SpaceGame game;
    int score, highscore;
    Texture gameOverBanner;
    BitmapFont scoreFont;
    private float delta;

    public GameOverScreen(SpaceGame game, int score) {
        this.game = game;
        this.score = score;
        // Get high score from save file
        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush(); // Save changes to preferences
        }

        // Load textures and fonts
        gameOverBanner = new Texture("game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();
        game.scrollingBackground.updateAndRender(delta, game.batch);
        
        game.batch.draw(gameOverBanner, SpaceGame.WIDTH / 2 - MIRROR_WIDTH / 2, SpaceGame.HEIGHT - MIRROR_HIGHT - 15, MIRROR_WIDTH, MIRROR_HIGHT);
        GlyphLayout scoreLayout=new GlyphLayout(scoreFont,"Score: \n"+score, Color.WHITE,0, Align.left,false);
        GlyphLayout highscoreLayout=new GlyphLayout(scoreFont,"highscore: \n"+highscore, Color.WHITE,0, Align.left,false);
        scoreFont.draw(game.batch,scoreLayout,SpaceGame.WIDTH/2-scoreLayout.width/2,SpaceGame.HEIGHT-MIRROR_HIGHT-15*2);

        scoreFont.draw(game.batch,highscoreLayout,SpaceGame.WIDTH/2-scoreLayout.width/2,SpaceGame.HEIGHT-MIRROR_HIGHT-scoreLayout.height-15*3);

        GlyphLayout tryAgainLayout=new GlyphLayout(scoreFont,"Try Again");
        GlyphLayout mainMenuLayout=new GlyphLayout(scoreFont,"Main Menu");

        float tryAgainX=SpaceGame.WIDTH/2-tryAgainLayout.width/2;
        float tryAgainY=SpaceGame.HEIGHT/2-tryAgainLayout.height/2;
        float mainMenuX=SpaceGame.WIDTH/2-mainMenuLayout.width/2;
        float mainMenuY=SpaceGame.WIDTH/2-mainMenuLayout.width/2-tryAgainLayout.height-15;

        float touchX = game.cam.getInputInGameWorld().x,  touchY=SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y;
        //if try again and main menu is being pressed
        if(Gdx.input.isTouched()){
            //Try again
            if(touchX>tryAgainX && touchX<tryAgainX+tryAgainLayout.width && touchY>tryAgainY-tryAgainLayout.height && touchY<tryAgainY){
                this.dispose();
                game.batch.end();
                game.setScreen((new MainGameScreen(game)));
                return;
            }
            if(touchX>mainMenuX && touchX<mainMenuX+mainMenuLayout.width && touchY>mainMenuY-mainMenuLayout.height && touchY<mainMenuY){
                this.dispose();
                game.batch.end();
                game.setScreen((new MainMenuScreen(game)));
                return;
            }
        }
        //draw bottoms
        scoreFont.draw(game.batch,tryAgainLayout,tryAgainX,tryAgainY);
        scoreFont.draw(game.batch,mainMenuLayout,mainMenuX,mainMenuY);
        game.batch.end();
    }


    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}