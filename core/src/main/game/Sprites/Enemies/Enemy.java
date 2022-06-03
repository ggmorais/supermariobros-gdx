package main.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import main.game.Screens.PlayScreen;

abstract public class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        setPosition(x, y);
        defineEnemy();

        velocity = new Vector2(-1, -2);
        body.setActive(false);
    }
    
    public abstract void update(float delta);
    public abstract void onHeadHit();
    protected abstract void defineEnemy();

    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}
