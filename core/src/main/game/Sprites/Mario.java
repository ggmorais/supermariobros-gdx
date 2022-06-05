package main.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;

public class Mario extends Sprite {
    public World world;
    public Body body;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD };
    public State currentState;
    public State previousState;

    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;

    private float stateTimer;
    private boolean runningRight;
    public boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;

    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i <= 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        }

        marioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for (int i = 1; i <= 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        }

        bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation<TextureRegion>(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        defineMario();
        setBounds(0, 0, 16 / MarioGame.PPM, 16 / MarioGame.PPM);
        setRegion(marioStand);
    }

    public void hit() {
        if (marioIsBig) {
            marioIsBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            MarioGame.assetManager.get("audio/sounds/powerdown.wav", Sound.class).play();
        } else {
            killMario();
        }
    }

    public void killMario() {
        MarioGame.assetManager.get("audio/music/mario_music.ogg", Music.class).stop();
        MarioGame.assetManager.get("audio/sounds/mariodie.wav", Sound.class).play();
        marioIsDead = true;
        Filter filter = new Filter();
        filter.maskBits = MarioGame.NOTHING_BIT;
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
    }

    public boolean getMarioIsBig() {
        return marioIsBig;
    }

    public boolean getMarioIsDead() {
        return marioIsDead;
    }

    public void grow() {
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        MarioGame.assetManager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public void update(float delta) {
        if (body.getPosition().y <= -8 / MarioGame.PPM && !marioIsDead) {
            killMario();
        }

        if (marioIsBig)
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 6 / MarioGame.PPM);
        else
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));

        if (timeToDefineBigMario)
            defineBigMario();
        if (timeToRedefineMario)
            redefineMario();
    }

    public TextureRegion getFrame(float delta) {
        TextureRegion region;
        currentState = getState();

        switch(currentState) {
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break; 
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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
        if (marioIsDead) {
            return State.DEAD;
        }
        if (runGrowAnimation) {
            return State.GROWING;
        } else if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void redefineMario() {
        Vector2 position = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyType.DynamicBody;
        
        body = world.createBody(bodyDef);

        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fixture.filter.categoryBits = MarioGame.MARIO_BIT;
        fixture.filter.maskBits = (
            MarioGame.GROUND_BIT | 
            MarioGame.COIN_BIT | 
            MarioGame.BRICK_BIT |
            MarioGame.OBJECT_BIT |
            MarioGame.ENEMY_BIT |
            MarioGame.ENEMY_HEAD_BIT |
            MarioGame.ITEM_BIT
        );

        fixture.shape = shape;
        body.createFixture(fixture).setUserData(this);;

        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-2 / MarioGame.PPM, 7 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 7 / MarioGame.PPM));
        fixture.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fixture.shape = headShape;
        fixture.isSensor = true;

        body.createFixture(fixture).setUserData(this);
        
        timeToRedefineMario = false;
    }

    public void defineBigMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, 10 / MarioGame.PPM));
        bodyDef.type = BodyType.DynamicBody;
        
        body = world.createBody(bodyDef);

        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
        fixture.filter.categoryBits = MarioGame.MARIO_BIT;
        fixture.filter.maskBits = (
            MarioGame.GROUND_BIT | 
            MarioGame.COIN_BIT | 
            MarioGame.BRICK_BIT |
            MarioGame.OBJECT_BIT |
            MarioGame.ENEMY_BIT |
            MarioGame.ENEMY_HEAD_BIT |
            MarioGame.ITEM_BIT
        );

        fixture.shape = shape;
        body.createFixture(fixture).setUserData(this);

        shape.setPosition(new Vector2(0, -14 / MarioGame.PPM));
        body.createFixture(fixture).setUserData(this);
        

        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-2 / MarioGame.PPM, 7 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 7 / MarioGame.PPM));
        fixture.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fixture.shape = headShape;
        fixture.isSensor = true;

        body.createFixture(fixture).setUserData(this);
        timeToDefineBigMario = false;
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
        fixture.filter.maskBits = (
            MarioGame.GROUND_BIT | 
            MarioGame.COIN_BIT | 
            MarioGame.BRICK_BIT |
            MarioGame.OBJECT_BIT |
            MarioGame.ENEMY_BIT |
            MarioGame.ENEMY_HEAD_BIT |
            MarioGame.ITEM_BIT
        );

        fixture.shape = shape;
        body.createFixture(fixture).setUserData(this);;

        // EdgeShape feetShape = new EdgeShape();
        // feetShape.set(new Vector2(-2 / MarioGame.PPM, -6 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, -6 / MarioGame.PPM));
        // fixture.shape = feetShape;
        // body.createFixture(fixture);

        EdgeShape headShape = new EdgeShape();
        headShape.set(new Vector2(-2 / MarioGame.PPM, 7 / MarioGame.PPM), new Vector2(2 / MarioGame.PPM, 7 / MarioGame.PPM));
        fixture.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fixture.shape = headShape;
        fixture.isSensor = true;

        body.createFixture(fixture).setUserData(this);
    }
}
