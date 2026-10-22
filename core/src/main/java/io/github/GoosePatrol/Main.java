package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    // viewport manages the size and aspect ratio of contained objects
    private CameraTarget target;
    private CameraLogic camera;
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");
        // placeholder target for the camera to look at 
        target = new CameraTarget(image.getWidth()/2+103, image.getHeight()/2);
        camera = new CameraLogic(160,90,image);
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
        batch.end();
        // clamp camera to ensure it cannot show outside of the background
        camera.updatePosition(target);
        batch.setProjectionMatrix(camera.camera.combined);
    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();

        player.getPlayerSprite().dispose();
    }
}
