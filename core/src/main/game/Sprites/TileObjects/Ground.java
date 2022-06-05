package main.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

import main.game.Screens.PlayScreen;
import main.game.Sprites.Mario;

public class Ground extends InteractiveTileObject {
    public Ground(PlayScreen screen, MapObject object)  {
        super(screen, object);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Contact:", "Ground");
    }
}
