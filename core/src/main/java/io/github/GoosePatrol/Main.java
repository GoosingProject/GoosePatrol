package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    // viewport manages the size and aspect ratio of contained objects
    private CameraTarget target;

    private boolean showMenu = true;
    //contains the logic value to tell the game to render the main menu
    private MainMenu mainMenu;


    private Player player;
    private CameraLogic camera;

    private boolean paused = false;
    @Override
    public void create() {
        mainMenu = new MainMenu();
        mainMenu.create();

        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");
        camera = new CameraLogic(160,90,image);

        player = new Player();
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

        //edited render logic to allow the main menu to render on startup or on pause
        if (showMenu || paused) {
            mainMenu.render();
            
            if (mainMenu.playGameTriggered){
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
        camera.updatePosition(player);
        batch.setProjectionMatrix(camera.camera.combined);
        batch.begin();
        batch.draw(image,0f,0f);
        batch.end();
        // ensure the player and character move with the same world units
        player.getPlayerSprite().setProjectionMatrix(camera.camera.combined);
        player.getPlayerSprite().begin();
        player.drawPlayer();
        player.getPlayerSprite().end();

        if (!paused) {
            player.updateMovement();
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            paused = !paused;
        }
    }

    

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();

        player.getPlayerSprite().dispose();
        mainMenu.dispose();
    }
}
