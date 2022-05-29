package main.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;

public class Mario extends Sprite {
    public World world;
    public Body body;

    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;

    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i <= 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }

        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }

        marioJump = new Animation<TextureRegion>(0.1f, frames);
        // frames.clear();
        
        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);

        defineMario();
        setBounds(0, 0, 16 / MarioGame.PPM, 16 / MarioGame.PPM);
        setRegion(marioStand);
    }

    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta) {
        TextureRegion region;
        currentState = getState();

        switch(currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break; 
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        return region;
    }

    public State getState() {
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioGame.PPM, 32 / MarioGame.PPM);
        bodyDef.type = BodyType.DynamicBody;
        
        body = world.createBody(bodyDef);

        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fixture.filter.categoryBits = MarioGame.MARIO_BIT;
        fixture.filter.maskBits = MarioGame.DEFAULT_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT;

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
