package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.GoosePatrol.ItemCode.BinSpawner;
import io.github.GoosePatrol.ItemCode.Inventory;
import io.github.GoosePatrol.ItemCode.InventoryHud;
import io.github.GoosePatrol.ItemCode.ItemBoxSpawner;
import io.github.GoosePatrol.ItemCode.ItemDeployHandler;
import io.github.GoosePatrol.ItemCode.ItemRoller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private boolean showMenu = true;
    // contains the logic value to tell the game to render the main menu
    private MainMenu mainMenu;
    private EnergyBar energyBar;
    private float sleepTimer = 0f;

    private Player player;
    private CameraLogic camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private boolean paused = false;
    private PlayCountdown countdown;

    private GameWorld world;

    private Texture scareTexture;
    private Texture breadTexture;

    private Audio audio;

    private Inventory inventory;
    private InventoryHud inventoryHud;
    private ItemRoller itemRoller;
    private ItemBoxSpawner boxSpawner;
    private Texture itemBoxTexture;

    private ItemDeployHandler deployHandler;

    private PlayTimer playTimer;
    private GooseSpawner gooseSpawner;
    private BinSpawner binSpawner;
    private Texture binTexture;

  @Override
  public void create() {
    audio = new Audio();
    mainMenu = new MainMenu(audio);

        batch = new SpriteBatch();

        map = new TmxMapLoader().load("map/Map.tmx");
        camera = new CameraLogic(160, 90, map);
        player = new Player(camera.getCamera());

        renderer = new OrthogonalTiledMapRenderer(map, 1f);

        gooseSpawner = new GooseSpawner((TiledMapTileLayer) map.getLayers().get("Collision"), 60, 60, 25);

        world = new GameWorld(gooseSpawner);


        audio.playMusic("menu");

    countdown = new PlayCountdown();
    // pass to scarecrow objects on creation
    //  item spawning will be moved to a inventory class in futre updates
    scareTexture = new Texture("ScareCrow.png");
    // pass to food objects on creation
    breadTexture = new Texture("Bread.png");

    inventory = new Inventory();
    inventoryHud = new InventoryHud(inventory);
    inventoryHud.create();
    itemRoller = new ItemRoller(scareTexture,breadTexture);
    itemBoxTexture = new Texture("ItemBox.png");

    energyBar = new EnergyBar();
    world.setEnergyBar(energyBar);

    playTimer = new PlayTimer();
    playTimer.create();
    binTexture = new Texture("trash.png");
    binSpawner = new BinSpawner(WalkableTiles.findWalkableTiles(map), binTexture);
    MapProperties prop = map.getProperties();
    float mapW = prop.get("width",Integer.class)*prop.get("tilewidth",Integer.class);
    float mapH = prop.get("height",Integer.class)*prop.get("tilewidth",Integer.class);
    boxSpawner = new ItemBoxSpawner(new TextureRegion(itemBoxTexture), map, mapW, mapH);
    deployHandler = new ItemDeployHandler(camera.getCamera(), inventory, world, inventoryHud, player, binSpawner);
  }


    // override the resize function so it works of screenScaler viewport instead of default
    @Override
    public void resize(int width, int height) {
        camera.updateScale(width, height);

    if (showMenu || paused) {
      mainMenu.resize(width, height);
    }
    inventoryHud.resize(width, height);
    energyBar.resize(width, height);
    playTimer.resize(width, height);
  }

    @Override
    public void render() {

        //edited render logic to allow the main menu to render on startup or on pause
        if (showMenu) {
            audio.playMusic("menu");
            mainMenu.render(Gdx.graphics.getDeltaTime());
            if (mainMenu.playGameTriggered) {
                audio.pauseMusic();
                Gdx.input.setInputProcessor(null);
                showMenu = false;
                mainMenu.playGameTriggered = false;
                countdown.startAnimation();
                paused = true;
            } else if (showMenu) {
                Gdx.input.setInputProcessor(mainMenu.getStage());
            }
            return;
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        countdown.updateAnimation(Gdx.graphics.getDeltaTime());
        if (paused && countdown.isAnimationFinished()) {
            paused = false;
        }

        // clamp camera to ensure it cannot show outside of the background
        batch.setProjectionMatrix(camera.getCamera().combined);
        camera.updatePosition(player.getPlayerPosition());
        renderer.setView(camera.getCamera());
        renderer.render();

        if (!paused) {
            player.updateMovement();
            world.update(Gdx.graphics.getDeltaTime(), player.getPlayerPosition());
            audio.playMusic("ambient");
            boolean invChanged = boxSpawner.update(Gdx.graphics.getDeltaTime(), player.getPlayerPosition(), inventory, itemRoller);
            deployHandler.update();
            gooseSpawner.update(Gdx.graphics.getDeltaTime(), player.getPlayerPosition());
            if (invChanged){
              inventoryHud.refresh();
            }
            playTimer.update(Gdx.graphics.getDeltaTime());
        }

        batch.begin();
        binSpawner.render(batch);
        player.draw(batch);
        boxSpawner.render(batch);
        gooseSpawner.render(batch);
        world.render(batch);
        countdown.draw(batch, camera.getCamera().position.x, camera.getCamera().position.y,0.3f,audio);
        batch.end();

        inventoryHud.render();
        energyBar.render(Gdx.graphics.getDeltaTime());
        playTimer.render();
        // check for game loss
        if(!paused && (playTimer.isGameEnd())){
            enterLossState();
        }
       else if (!paused && energyBar.getPlayerEnergy() <= 0 ) {
        if (sleepTimer == 0f) {
            player.setAction(PlayerAction.FALLING_ASLEEP);
        }
        sleepTimer += Gdx.graphics.getDeltaTime();

        if(sleepTimer >= 3.0f) {
            sleepTimer = 0f;
            enterLossState(); // prevents the loss state from triggering before the animation finishes playing
        }

        }
        else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            showMenu = !showMenu;
        }
    }

    @Override
    public void dispose() {
        binSpawner.dispose();
        batch.dispose();
        map.dispose();
        renderer.dispose();
        scareTexture.dispose();
        audio.dispose();
        breadTexture.dispose();
        energyBar.dispose();
        playTimer.dispose();
    }
    /**
     * call when timer runs out or player runs out of energy
     */
    private void enterLossState(){
        resetGameState();
        showMenu = true;
    }
    /**
     * call all reset helpers to re-start game
     */
    private void resetGameState(){
        player.resetPos();
        camera.snapToPositon(player.getPlayerPosition());
        world.clear();
        boxSpawner.reset();
        inventory.clear();
        inventoryHud.refresh();
        energyBar.setEnergyBar(100f);
        playTimer.reset();
    }
}

