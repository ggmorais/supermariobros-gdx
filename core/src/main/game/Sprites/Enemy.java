package main.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import main.game.Screens.PlayScreen;

abstract public class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }
    
    public abstract void onHeadHit();
    protected abstract void defineEnemy();
}
