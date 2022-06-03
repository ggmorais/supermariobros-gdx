package main.game.Sprites.TileObjects;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import main.game.MarioGame;
import main.game.Scenes.Hud;
import main.game.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact:", "Brick");
        setCategoryFilter(MarioGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioGame.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
