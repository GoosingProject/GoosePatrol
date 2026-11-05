package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
  private SpriteBatch batch;

    private boolean showMenu = true;
    //contains the logic value to tell the game to render the main menu
    private MainMenu mainMenu;

    private EnergyBar energyBar;


  private Player player;
  private CameraLogic camera;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

  private boolean paused = false;

    private PlayCountdown countdown;
    @Override
    public void create() {
        mainMenu = new MainMenu();
        mainMenu.create();

        energyBar = new EnergyBar();
        energyBar.create();

    batch = new SpriteBatch();

    map = new TmxMapLoader().load("map/map.tmx");
    camera = new CameraLogic(160, 90, map);
    player = new Player(camera.getCamera());

    renderer = new OrthogonalTiledMapRenderer(map);
  }

  // override the resize function so it works of screenScaler viewport instead of default
  @Override
  public void resize(int width, int height) {
    camera.updateScale(width, height);

    if (showMenu || paused) {
      mainMenu.resize(width, height);
    }
  }

  @Override
  public void render() {
    // edited render logic to allow the main menu to render on startup or on pause
    if (showMenu || paused) {
      mainMenu.render();

      if (mainMenu.playGameTriggered) {
        Gdx.input.setInputProcessor(null);
        showMenu = false;
        paused = false;
        mainMenu.playGameTriggered = false;
      } else if (paused) {
        Gdx.input.setInputProcessor(mainMenu.getStage());
      }
      return;
    }

    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // clamp camera to ensure it cannot show outside of the background
        batch.setProjectionMatrix(camera.getCamera().combined);
        camera.updatePosition(player.getPlayerPosition());
        renderer.setView(camera.getCamera());
        renderer.render();
        // only unpause once the animation has finished playing
        if (!countdown.isAnimationFinished()){
            paused = true;
        }
        else{
            paused = false;
        }
        if (!paused) {
            player.updateMovement();
            
            // Sends player back to main menu when energy reaches zero
            if (energyBar.getPlayerEnergy() == 0) {
                showMenu = true;
                mainMenu.playGameTriggered = false;
                Gdx.input.setInputProcessor(mainMenu.getStage());
                energyBar.setEnergyBar(100f);       
            }
        }
        countdown.updateAnimation(Gdx.graphics.getDeltaTime());
        batch.begin();
        countdown.draw(batch, camera.getCamera().position.x, camera.getCamera().position.y, 0.2f);
        batch.end();
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            showMenu = !showMenu;
        }
    }

    

  @Override
  public void dispose() {
    batch.dispose();
    map.dispose();
    renderer.dispose();
  }
}
