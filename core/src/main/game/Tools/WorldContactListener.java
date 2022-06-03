package main.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import main.game.MarioGame;
import main.game.Sprites.Mario;
import main.game.Sprites.Enemies.Enemy;
import main.game.Sprites.Enemies.Goomba;
import main.game.Sprites.TileObjects.InteractiveTileObject;
import main.game.Sprites.Items.Item;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        if (fixA.getUserData() == "marioHead" || fixB.getUserData() == "marioHead") {
            Fixture head = fixA.getUserData() == "marioHead" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case MarioGame.ENEMY_HEAD_BIT | MarioGame.MARIO_BIT:
                Gdx.app.log("switch enemy head hit", "");
                if (fixA.getFilterData().categoryBits == MarioGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).onHeadHit();
                else
                    ((Enemy) fixB.getUserData()).onHeadHit();
                break;
            
            case MarioGame.ENEMY_BIT | MarioGame.BRICK_BIT:
                if (fixA.getFilterData().categoryBits == MarioGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            
            case MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MarioGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            
            case MarioGame.ENEMY_BIT | MarioGame.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case MarioGame.ITEM_BIT | MarioGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MarioGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case MarioGame.ITEM_BIT | MarioGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == MarioGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).useItem((Mario) fixB.getUserData());
                else if (!(fixA.getUserData() instanceof String))
                    ((Item) fixB.getUserData()).useItem((Mario) fixA.getUserData());
                break;
            
            case MarioGame.MARIO_BIT | MarioGame.ENEMY_BIT:
                Gdx.app.log("Mario", "died");

        }       

    }

    @Override
    public void endContact(Contact contact) {
                
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
                
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
                
    }
    
}
