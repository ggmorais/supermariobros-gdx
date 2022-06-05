package main.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;
import main.game.Sprites.Mario;

public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixture = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGame.PPM);
    
        fixture.shape = shape;
        fixture.filter.categoryBits = MarioGame.ITEM_BIT;
        fixture.filter.maskBits = (
            MarioGame.MARIO_BIT | 
            MarioGame.GROUND_BIT | 
            MarioGame.OBJECT_BIT | 
            MarioGame.COIN_BIT | 
            MarioGame.BRICK_BIT
        );
        body.createFixture(fixture).setUserData(this);
    }

    @Override
    public void useItem(Mario player) {
        destroy();
        player.grow();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
    
}
