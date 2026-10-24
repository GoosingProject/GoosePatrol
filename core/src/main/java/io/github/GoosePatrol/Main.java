package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private Player player;
    private CameraLogic camera;

    private boolean paused = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");

        camera = new CameraLogic(160,90,image);
        player = new Player(camera.camera);
    }

    // override the resize function so it works of screenScaler viewport instead of default
   @Override
    public void resize(int width, int height) {
        camera.updateScale(width, height);
    }

    @Override
    public void render() {
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

            //System.out.println("player: " + player.getPlayerPosition());
            //System.out.println("camera: " + camera.camera.position);
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            paused = !paused;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
