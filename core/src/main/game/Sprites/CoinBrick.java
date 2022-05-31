package main.game.Sprites;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import main.game.MarioGame;
import main.game.Scenes.Hud;
import main.game.Screens.PlayScreen;

public class CoinBrick extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private static int BLANK_COIN = 28;

    public CoinBrick(PlayScreen screen, Rectangle bounds)  {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact:", "Coin");

        if (getCell().getTile().getId() == BLANK_COIN) {
            MarioGame.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
        } else {
            MarioGame.assetManager.get("audio/sounds/coin.wav", Sound.class).play();
        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
