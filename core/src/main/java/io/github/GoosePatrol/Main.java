package io.github.GoosePatrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    // viewport manages the size and aspect ratio of contained objects
    private Viewport screenScaler;
    // view Width and height maintain specific aspect ratio of the camera
    private float viewWidth = 160f;
    private float viewHeight = 90f;

    private OrthographicCamera camera;
    private CameraTarget target;

    private boolean paused = true;
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");
        // orthographic camera can work in 2d space
        camera = new OrthographicCamera();
        // make use of fit viewport in order to maintain aspect ratio
        screenScaler = new FitViewport(viewWidth, viewHeight,camera);
        // placeholder target for the camera to look at 
        target = new CameraTarget(image.getWidth()/2, image.getHeight()/2);
        // position camera to target
        camera.position.x = target.getX();
        camera.position.y = target.getY();
    }
    // override the resize function so it works of screenScaler viewport instead of default
   @Override
    public void resize(int width, int height) {
        screenScaler.update(width, height, false);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image,0f,0f);
        batch.end();
        
        if (!paused) {
            /// clamp camera to ensure it cannot show outside of the background
            clampCamera();
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            // render sprite here
        }
    }

    private void clampCamera(){
        // stores the width and height to the centre of the camera
        float halfW = camera.viewportWidth *camera.zoom/2f;
        float halfH = camera.viewportHeight *camera.zoom/2f;
        // stores the width an height of the background image
        float imgW = image.getWidth();
        float imgH = image.getHeight();
        // camera.positon is the cente of the view
        // clamps the positon half a view away from the image edges
        camera.position.x = MathUtils.clamp(camera.position.x, halfW, imgW-halfW);
        camera.position.y = MathUtils.clamp(camera.position.y, halfH, imgH-halfH);

    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
