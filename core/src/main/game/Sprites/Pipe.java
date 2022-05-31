package main.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;

public class Pipe extends InteractiveTileObject {
    public Pipe(PlayScreen screen, Rectangle bounds)  {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.OBJECT_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact:", "Pipe");
    }
}
