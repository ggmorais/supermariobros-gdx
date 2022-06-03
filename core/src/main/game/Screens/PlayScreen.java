package main.game.Screens;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import main.game.MarioGame;
import main.game.Scenes.Hud;
import main.game.Sprites.Mario;
import main.game.Sprites.Enemies.Enemy;
import main.game.Sprites.Enemies.Goomba;
import main.game.Sprites.Items.Item;
import main.game.Sprites.Items.ItemDef;
import main.game.Sprites.Items.Mushroom;
import main.game.Tools.B2WorldCreator;
import main.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private MarioGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    
    private Mario player;
    private Goomba goomba;
    private B2WorldCreator worldCreator;

    // box2d variables
    private World world;

    // debugger
    private Box2DDebugRenderer b2dr;
    private boolean isB2drEnabled = true;

    private TextureAtlas atlas;

    private Music music;

    private float musicVolume = 0.2f;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MarioGame game) {
        atlas = new TextureAtlas("mario_and_enemies.pack");

        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(MarioGame.V_WIDTH / MarioGame.PPM, MarioGame.V_HEIGHT / MarioGame.PPM, camera);
        hud = new Hud(game.sb);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map0.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MarioGame.PPM);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        worldCreator = new B2WorldCreator(this);
        
        player = new Mario(this);
        // goomba = new Goomba(this, 600 / MarioGame.PPM, 600 / MarioGame.PPM);

        world.setContactListener(new WorldContactListener());

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        // music = MarioGame.assetManager.get("audio/music/mario_music.ogg", Music.class);
        // music.setLooping(true);
        // music.setVolume(musicVolume);
        // music.play();
    }

    public void spawnItem(ItemDef def) {
        itemsToSpawn.add(def);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    
    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef def = itemsToSpawn.poll();
            if (def.type == Mushroom.class) {
                items.add(new Mushroom(this, def.position.x, def.position.y));
            }
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public void update(float delta) {
        handleInput(delta);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);
        player.update(delta);
        
        for (Enemy enemy : worldCreator.getGoombas()) {
            enemy.update(delta);
            if (enemy.getX() < player.getX() + 224 / MarioGame.PPM) {
                enemy.body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(delta);
        }

        camera.position.x = player.body.getPosition().x;
        camera.update();
        mapRenderer.setView(camera);
        hud.update(delta);
    }

    public void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.body.getLinearVelocity().y == 0) {
            player.body.applyLinearImpulse(new Vector2(0, 4f), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            isB2drEnabled = !isB2drEnabled;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        if (isB2drEnabled)
            b2dr.render(world, camera.combined);
        
        game.sb.setProjectionMatrix(camera.combined);
        game.sb.begin();
        
        player.draw(game.sb);

        for (Enemy enemy : worldCreator.getGoombas()) {
            enemy.draw(game.sb);
        }

        for (Item item : items) {
            item.draw(game.sb);
        }

        game.sb.end();

        game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
    
}
