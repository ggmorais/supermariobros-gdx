package main.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import main.game.Screens.PlayScreen;

public class Ground extends InteractiveTileObject {
    public Ground(PlayScreen screen, Rectangle bounds)  {
        super(screen, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact:", "Ground");
    }
}
