package main.game.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import main.game.MarioGame;
import main.game.Scenes.Hud;
import main.game.Screens.PlayScreen;
import main.game.Sprites.Mario;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (mario.getMarioIsBig()) {
            setCategoryFilter(MarioGame.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            MarioGame.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
        } else {
            MarioGame.assetManager.get("audio/sounds/bump.wav", Sound.class).play();
        }
    }
}
