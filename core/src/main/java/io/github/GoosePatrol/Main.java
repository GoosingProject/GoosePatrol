package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
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


    private Player player;
    private CameraLogic camera;

    private boolean paused = false;

    private float timeRemaining = 90f;
    private float nextShakeTime = 60f;
    private float shakeTimeLeft = 0f;
    private float shakeAmount = 3f;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private BitmapFont font;
    private OrthographicCamera uiCamera;

    @Override
    public void create() {
        mainMenu = new MainMenu();
        mainMenu.create();

        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");

        camera = new CameraLogic(160,90,image);
        player = new Player(camera.camera);
        font = new BitmapFont();
        uiCamera = new OrthographicCamera();
    }

    // override the resize function so it works of screenScaler viewport instead of default
      @Override
    public void resize(int width, int height) {
        camera.updateScale(width, height);
        uiCamera.setToOrtho(false, width, height);

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
        batch.begin();
        batch.draw(image,0f,0f);
        player.drawPlayer(batch);
        batch.end();

        if (!paused) {
            // clamp camera to ensure it cannot show outside of the background
            batch.setProjectionMatrix(camera.camera.combined);
            camera.updatePosition(player.getPlayerPosition());

            player.updateMovement();
            updateTimer(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            paused = !paused;
        }
    }

    private void updateTimer(float delta) {
        timeRemaining = timeRemaining - delta;
        if (timeRemaining < 0) {
            timeRemaining = 0;
        }
        if (timeRemaining <= nextShakeTime && nextShakeTime >= 3) {
            shakeTimeLeft = 0.5f;
            nextShakeTime = nextShakeTime / 2;
        }

        if (shakeTimeLeft > 0) {
            shakeTimeLeft = shakeTimeLeft - delta;
            offsetX = (float) (Math.random() * 2 - 1) * shakeAmount;
            offsetY = (float) (Math.random() * 2 - 1) * shakeAmount;
        } else {
            offsetX = 0;
            offsetY = 0;
        }
    }

 private String getTimeString() {
        int totalSeconds = (int) Math.ceil(timeRemaining);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        if (seconds < 10) {
            return minutes + ":0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
