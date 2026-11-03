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

    private boolean showMenu = true;
    //contains the logic value to tell the game to render the main menu
    private MainMenu mainMenu;

    private EnergyBar energyBar;


    private Player player;
    private CameraLogic camera;

    private boolean paused = false;

    private PlayCountdown countdown;
    @Override
    public void create() {
        mainMenu = new MainMenu();
        mainMenu.create();

        energyBar = new EnergyBar();
        energyBar.create();

        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");

        camera = new CameraLogic(160,90,image);
        player = new Player(camera.camera);
        countdown = new PlayCountdown();
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
        if (showMenu) {
            mainMenu.render();
            if (mainMenu.playGameTriggered){
                Gdx.input.setInputProcessor(null);
                showMenu = false;
                mainMenu.playGameTriggered = false;
                countdown.startAnimation();
                energyBar.render();
            } else if (showMenu) {
                Gdx.input.setInputProcessor(mainMenu.getStage());
            }
            return;
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image,0f,0f);
        player.drawPlayer(batch);
        batch.end();

        // clamp camera to ensure it cannot show outside of the background
        batch.setProjectionMatrix(camera.camera.combined);
        camera.updatePosition(player.getPlayerPosition());
        // only unpause once the animation has finished playing
        if (!countdown.isAnimationFinished()){
            paused = true;
        }
        else{
            paused = false;
            energyBar.render();
        }
        if (!paused) {
            player.updateMovement();
        }
        countdown.updateAnimation(Gdx.graphics.getDeltaTime());
        batch.begin();
        countdown.draw(batch, camera.camera.position.x, camera.camera.position.y, 0.2f);
        batch.end();
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            showMenu = !showMenu;
        }
    }

    

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
