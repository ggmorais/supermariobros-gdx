package main.game.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

import main.game.MarioGame;
import main.game.Screens.PlayScreen;
import main.game.Sprites.Mario;

public class Pipe extends InteractiveTileObject {
    public Pipe(PlayScreen screen, MapObject object)  {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.OBJECT_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Contact:", "Pipe");
    }
}
