package main.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;

public class Goomba extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 64, 0, 16, 16));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioGame.PPM, 32 / MarioGame.PPM);
        bodyDef.type = BodyType.DynamicBody;
        
        body = world.createBody(bodyDef);

        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fixture.filter.categoryBits = MarioGame.MARIO_BIT;
        fixture.filter.maskBits = MarioGame.GROUND_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT;

        fixture.shape = shape;
        body.createFixture(fixture);

        // EdgeShape feetShape = new EdgeShape();
        // feetShape.set(new Vector2(-2 / MarioGame.PPM, -6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, -6 / MarioGame.PPM));
        // fixture.shape = feetShape;
        // body.createFixture(fixture);

        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-2 / MarioGame.PPM, 7 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 7 / MarioGame.PPM));
        fixture.shape = headShape;
        fixture.isSensor = true;

        body.createFixture(fixture).setUserData("marioHead");
    }
    
}
