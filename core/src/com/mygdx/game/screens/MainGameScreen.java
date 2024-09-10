package com.mygdx.game.screens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.SpaceGame;
import com.mygdx.game.entities.Asteroid;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Explosion;
import com.mygdx.game.tools.CollisionRect;

public class MainGameScreen implements Screen {

    public static final float SPEED = 300;

    public static final float SHIP_ANIMATION_SPEED = 0.5f;

    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;

    public static final float ROLL_TIMER_SWITCH_TIME=0.15f;
    public static final float SHOOT_WAIT_TIME=0.3f;

    public static final float MIN_ASTEROID_SPAWN_TIME=0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME=0.6f;

    Animation<TextureRegion>[] rolls;



    float x;
    float y;
    int roll;

    float rollTimer;
    float stateTime;
    float shootTimer;
    float asteroidSpawnTimer;

    Random random;

    SpaceGame game;

    ArrayList<Bullet>bullets;
    ArrayList<Asteroid>asteroids;
    ArrayList<Explosion>explosions;

    Texture blank;
    Texture controls;

    BitmapFont scoreFont;
    float health=1;//0=dead,1=full health
    int score;
    CollisionRect playerRect;

    public MainGameScreen(SpaceGame game) {
        this.game = game;
        y = 15;
        x = SpaceGame.WIDTH / 2 - SHIP_WIDTH / 2;

        bullets = new ArrayList<Bullet>();
        asteroids=new ArrayList<Asteroid>();
        explosions=new ArrayList<Explosion>();
        scoreFont=new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        playerRect=new CollisionRect(0,0,SHIP_WIDTH,SHIP_HEIGHT);

        blank=new Texture("blank.png");

        if(SpaceGame.IS_MOBOLE)
            controls = new Texture("controls.png");

        score =0;

        random=new Random();
        asteroidSpawnTimer=random.nextFloat()*(MAX_ASTEROID_SPAWN_TIME-MIN_ASTEROID_SPAWN_TIME)+MIN_ASTEROID_SPAWN_TIME;

        roll = 2;
        rollTimer=0;
        shootTimer=0;
        rolls = new Animation[5];

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);

        rolls[0] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]);//all left
        rolls[1] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
        rolls[2] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);//no tilt
        rolls[3] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
        rolls[4] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);//all right

        game.scrollingBackground.setSpeedFixed(false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //shooting code
        shootTimer+=delta;

        if((isRight() || isLeft()) && shootTimer>=SHOOT_WAIT_TIME ) {

            shootTimer=0;

            int offset=4;
            if(roll==1 || roll==3) {//slightly tilted
                offset=8;
            }
            if(roll ==0 || roll==4) {//fully tilted
                offset=16;
            }

            bullets.add(new Bullet(x+offset));
            bullets.add(new Bullet(x+SHIP_WIDTH-offset));
        }
        //Asteroid Spawn code
        asteroidSpawnTimer-=delta;
        if(asteroidSpawnTimer<=0) {
            asteroidSpawnTimer=random.nextFloat()*(MAX_ASTEROID_SPAWN_TIME-MIN_ASTEROID_SPAWN_TIME)+MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(Gdx.graphics.getWidth()-Asteroid.WIDTH)));
        }

        //update asteroids
        ArrayList<Asteroid>asteroidsToRemove=new ArrayList<Asteroid>();
        for(Asteroid asteroid:asteroids) {
            asteroid.update(delta);
            if(asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }


        //update bullets
        ArrayList<Bullet>bulletsToRemove=new ArrayList<Bullet>();
        for(Bullet bullet:bullets) {
            bullet.update(delta);
            if(bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }

        //update explosions
        ArrayList<Explosion>explosionsToRemove=new ArrayList<Explosion>();
        for(Explosion explosion:explosions){
            explosion.update(delta);
            if(explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);

        //MOVEMENT CODE

        if (isLeft()) {   //LEFT
            x -= SPEED * Gdx.graphics.getDeltaTime();
            if(x<0)
                x=0;

            //update roll if button just click
            if(isJustLeft() && !isRight() && roll>0) {
                rollTimer=0;
                roll--;

            }

            //update the roll
            rollTimer-=Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer)>ROLL_TIMER_SWITCH_TIME && roll>0) {
                rollTimer-=ROLL_TIMER_SWITCH_TIME;
                roll--;

            }
        }
        else {
            if(roll<2) {
                //update the roll to make it go back to center
                rollTimer+=Gdx.graphics.getDeltaTime();
                if(Math.abs(rollTimer)>ROLL_TIMER_SWITCH_TIME && roll<4) {
                    rollTimer-=ROLL_TIMER_SWITCH_TIME;
                    roll++;

                }
            }

        }

        if (isRight()) {   //RIGHT
            x += SPEED * Gdx.graphics.getDeltaTime();
            if(x+SHIP_WIDTH > Gdx.graphics.getWidth())
                x=Gdx.graphics.getWidth()-SHIP_WIDTH;

            if(isJustRight() && !isLeft() && roll>0) {
                rollTimer=0;
                roll--;

            }

            //update the roll
            rollTimer+=Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer)>ROLL_TIMER_SWITCH_TIME && roll<4) {
                rollTimer-=ROLL_TIMER_SWITCH_TIME;
                roll++;

            }
        }
        else {
            if(roll>2) {
                //update the roll
                rollTimer-=Gdx.graphics.getDeltaTime();
                if(Math.abs(rollTimer)>ROLL_TIMER_SWITCH_TIME && roll>0) {
                    rollTimer-=ROLL_TIMER_SWITCH_TIME;
                    roll--;

                }
            }

        }

        //After player moves update collisionRect
        playerRect.move(x,y);

        //After all update check for collisions
        for(Bullet bullet:bullets){
            for(Asteroid asteroid:asteroids){
                if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())){
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(),asteroid.getX()));
                    score+=100;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        for(Asteroid asteroid:asteroids){
            if(asteroid.getCollisionRect().collidesWith(playerRect)){
                asteroidsToRemove.add(asteroid);
                health-=0.1f;

                //If health is depleted, go to game over screen
                if(health<=0){
                    this.dispose();
                    game.setScreen(new GameOverScreen(game,score));
                    return;
                }

            }
        }
        asteroids.removeAll(asteroidsToRemove);
        stateTime += delta;

        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);

        GlyphLayout scoreLayout =new GlyphLayout(scoreFont,""+score);
        scoreFont.draw(game.batch,scoreLayout,Gdx.graphics.getWidth()/2-scoreLayout.width/2,Gdx.graphics.getHeight()-scoreLayout.height-10);
        for(Bullet bullet:bullets) {
            bullet.render(game.batch);
        }
        for(Asteroid asteroid:asteroids) {
            asteroid.render(game.batch);
        }
        for(Explosion explosion:explosions){
            explosion.render(game.batch);
        }
        //Draw health
        if(health>0.6f){
            game.batch.setColor(Color.GREEN);
        }
        else if(health>0.2f){
            game.batch.setColor(Color.ORANGE);
        }
        else{
            game.batch.setColor(Color.RED);
        }

        game.batch.draw(blank,0,0,Gdx.graphics.getWidth()*health,5);

        game.batch.setColor(Color.WHITE);

        TextureRegion currentFrame = rolls[roll].getKeyFrame(stateTime, true);
        game.batch.draw(currentFrame, x, y, SHIP_WIDTH, SHIP_HEIGHT);

        if(SpaceGame.IS_MOBOLE){
            // Draw Left
            game.batch.setColor(Color.RED);
            game.batch.draw(controls, 0, 0, SpaceGame.WIDTH/2, SpaceGame.HEIGHT, 0, 0, SpaceGame.WIDTH/2, SpaceGame.HEIGHT, false, false);
             
            // Draw Right
            game.batch.setColor(Color.BLUE);
            game.batch.draw(controls, SpaceGame.WIDTH/2, 0, SpaceGame.WIDTH/2, SpaceGame.HEIGHT, 0, 0, SpaceGame.WIDTH/2, SpaceGame.HEIGHT, true, false);
        
            game.batch.setColor(Color.WHITE);
        }

        game.batch.end();
    }

      private boolean isRight() {
        return Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isTouched() && Gdx.input.getX() >= SpaceGame.WIDTH / 2);
      }
      private boolean isLeft() {
        return Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && Gdx.input.getX() < SpaceGame.WIDTH / 2);
      }
      private boolean isJustRight() {
        return Gdx.input.isKeyJustPressed(Keys.RIGHT) || (Gdx.input.justTouched() && Gdx.input.getX() >= SpaceGame.WIDTH / 2);
      }
      private boolean isJustLeft() {
        return Gdx.input.isKeyJustPressed(Keys.LEFT) || (Gdx.input.justTouched() && Gdx.input.getX() < SpaceGame.WIDTH / 2);
      }

    @Override
    public void resize(int width, int height) {

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
