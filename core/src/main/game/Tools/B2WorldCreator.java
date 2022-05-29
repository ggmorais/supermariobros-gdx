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
import main.game.Sprites.Brick;
import main.game.Sprites.Coin;
import main.game.Sprites.Ground;
import main.game.Sprites.Pipe;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {

        // create ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Ground(world, map, bounds);
        }   

        // create pipes
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Pipe(world, map, bounds);
        }   

        // create bricks
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Brick(world, map, bounds);
        }   

        // create coins
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            new Coin(world, map, bounds);
        }  

    }
}
