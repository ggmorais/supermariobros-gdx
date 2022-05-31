package main.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;
import main.game.Sprites.Brick;
import main.game.Sprites.CoinBrick;
import main.game.Sprites.Ground;
import main.game.Sprites.Pipe;

public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        // create ground
        for (MapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Ground(screen, bounds);
        }   

        // create pipes
        for (MapObject object : map.getLayers().get("pipes").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Pipe(screen, bounds);
        }   

        // create bricks
        for (MapObject object : map.getLayers().get("bricks").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, bounds);
        }   

        // create coins
        for (MapObject object : map.getLayers().get("coins").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new CoinBrick(screen, bounds);
        }  

    }
}
