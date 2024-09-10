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

    private static final int MIRROR_WIDTH = 400;
    private static final int MIRROR_HIGHT = 300;
    private static final int TRY_AGAIN_BUTTON_Y=200;
    private static final int MAIN_MENU_BUTTON_Y=150;
    SpaceGame game;
    int score, highscore;
    Texture gameOverBanner;
    Texture backgroundTexture; // New background texture
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
        backgroundTexture = new Texture("Over.png"); // Load background texture
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

        // Draw the background first
        game.batch.draw(backgroundTexture, 0, 0, SpaceGame.WIDTH, SpaceGame.HEIGHT);

        // Render scrolling background if you still want it
        // game.scrollingBackground.updateAndRender(delta, game.batch);
        //game.batch.setColor(Color.GREEN);
        // Draw game over banner
        game.batch.draw(gameOverBanner, SpaceGame.WIDTH / 2 - MIRROR_WIDTH / 2, SpaceGame.HEIGHT - MIRROR_HIGHT - 50, MIRROR_WIDTH, MIRROR_HIGHT);
        //game.batch.setColor(Color.WHITE);
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score:" + score, Color.ORANGE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "HighScore:" + highscore, Color.ORANGE, 0, Align.left, false);

        scoreFont.draw(game.batch, scoreLayout, 130, SpaceGame.HEIGHT - MIRROR_HIGHT - 15 * 5);
        scoreFont.draw(game.batch, highscoreLayout, 5, SpaceGame.HEIGHT - MIRROR_HIGHT - scoreLayout.height - 15 * 6);

        // Handle input and draw buttons
        float touchX = game.cam.getInputInGameWorld().x, touchY = SpaceGame.HEIGHT - game.cam.getInputInGameWorld().y;

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        float tryAgainX = SpaceGame.WIDTH / 2 - tryAgainLayout.width / 2;
        float tryAgainY = TRY_AGAIN_BUTTON_Y;
        float mainMenuX = SpaceGame.WIDTH / 2 - mainMenuLayout.width / 2;
        float mainMenuY = MAIN_MENU_BUTTON_Y;

        // Check if hovering over try again button
        if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
            tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0, Align.left, false);
        }

        // Check if hovering over Main Menu button
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);
        }

        // Handle button clicks
        if (Gdx.input.isTouched()) {
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainGameScreen(game));
                return;
            }
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        // Draw buttons
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

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
        gameOverBanner.dispose();
        backgroundTexture.dispose(); // Dispose of background texture
        scoreFont.dispose();
    }
}