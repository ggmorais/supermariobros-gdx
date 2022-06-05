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
import com.badlogic.gdx.utils.Array;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;
import main.game.Sprites.Enemies.Goomba;
import main.game.Sprites.TileObjects.Brick;
import main.game.Sprites.TileObjects.CoinBrick;
import main.game.Sprites.TileObjects.Ground;
import main.game.Sprites.TileObjects.Pipe;

public class B2WorldCreator {
    private Array<Goomba> goombas;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        // create ground
        for (MapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            new Ground(screen, object);
        }   

        // create pipes
        for (MapObject object : map.getLayers().get("pipes").getObjects().getByType(RectangleMapObject.class)) {
            new Pipe(screen, object);
        }   

        // create bricks
        for (MapObject object : map.getLayers().get("bricks").getObjects().getByType(RectangleMapObject.class)) {
            new Brick(screen, object);
        }   

        // create coins
        for (MapObject object : map.getLayers().get("coins").getObjects().getByType(RectangleMapObject.class)) {
            new CoinBrick(screen, object);
        }

        // create goombas
        goombas = new Array<Goomba>();

        for (MapObject object : map.getLayers().get("goombas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle bounds = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, bounds.getX() / MarioGame.PPM, bounds.getY() / MarioGame.PPM));
        }

    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
}
