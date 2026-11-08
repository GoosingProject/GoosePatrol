package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
  private SpriteBatch batch;

  private boolean showMenu = true;
  // contains the logic value to tell the game to render the main menu
  private MainMenu mainMenu;

  private Player player;
  private CameraLogic camera;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

  private boolean paused = false;
  private PlayCountdown countdown;



    private GameWorld world;

    private Texture scareTexture;
    private Texture breadTexture;

    private Inventory inventory;
    private InventoryHud inventoryHud;
    private ItemRoller itemRoller;
    private ItemBoxSpawner boxSpawner;
    private Texture itemBoxTexture;

    private ItemDeployHandler deployHandler;
  @Override
  public void create() {
    mainMenu = new MainMenu();
    mainMenu.create();

    batch = new SpriteBatch();

    map = new TmxMapLoader().load("map/map.tmx");
    camera = new CameraLogic(160, 90, map);
    player = new Player(camera.getCamera());

    renderer = new OrthogonalTiledMapRenderer(map);

    world = new GameWorld();

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

    MapProperties prop = map.getProperties();
    float mapW = prop.get("width",Integer.class)*prop.get("tilewidth",Integer.class);
    float mapH = prop.get("height",Integer.class)*prop.get("tilewidth",Integer.class);
    boxSpawner = new ItemBoxSpawner(new TextureRegion(itemBoxTexture), mapW, mapH);
    deployHandler = new ItemDeployHandler(camera.getCamera(), inventory, world, inventoryHud, player);
  }


  // override the resize function so it works of screenScaler viewport instead of default
  @Override
  public void resize(int width, int height) {
    camera.updateScale(width, height);

    if (showMenu || paused) {
      mainMenu.resize(width, height);
    }
    inventoryHud.resize(width, height);
  }

    @Override
    public void render() {

        //edited render logic to allow the main menu to render on startup or on pause
        if (showMenu) {
            mainMenu.render();
            if (mainMenu.playGameTriggered){
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
            world.update(Gdx.graphics.getDeltaTime());
            boolean invChanged = boxSpawner.update(Gdx.graphics.getDeltaTime(), player.getPlayerPosition(), inventory, itemRoller);
            deployHandler.update();
            if (invChanged){
              inventoryHud.refresh();
            }
        }
        batch.setProjectionMatrix(camera.getCamera().combined);
        batch.begin();
        player.draw(batch);
        boxSpawner.render(batch);
        world.render(batch);
        countdown.draw(batch, camera.getCamera().position.x, camera.getCamera().position.y,0.3f);
        batch.end();
        inventoryHud.render();
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            showMenu = !showMenu;
        }
    }

  @Override
  public void dispose() {
    batch.dispose();
    map.dispose();
    renderer.dispose();
    scareTexture.dispose();
  }
}
