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
<<<<<<< HEAD
    private CameraTarget target;
    private CameraLogic camera;
=======
    private Viewport screenScaler;
    // view Width and height maintain specific aspect ratio of the camera
    private float viewWidth = 160f;
    private float viewHeight = 90f;

    private Player player;

    private OrthographicCamera camera;
    private CameraTarget target;

    private boolean paused = true;
>>>>>>> 32c0e8ac123b4fb7e0390c1e31f85ac72cb3d866
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("placeholder_background.png");
<<<<<<< HEAD
        // placeholder target for the camera to look at 
        target = new CameraTarget(image.getWidth()/2+103, image.getHeight()/2);
        camera = new CameraLogic(160,90,image);
=======
        // orthographic camera can work in 2d space
        camera = new OrthographicCamera();
        // make use of fit viewport in order to maintain aspect ratio
        screenScaler = new FitViewport(viewWidth, viewHeight,camera);
        // placeholder target for the camera to look at
        target = new CameraTarget(image.getWidth()/2, image.getHeight()/2);
        // position camera to target
        camera.position.x = target.getX();
        camera.position.y = target.getY();

        player = new Player ();
>>>>>>> 32c0e8ac123b4fb7e0390c1e31f85ac72cb3d866
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
<<<<<<< HEAD
        // clamp camera to ensure it cannot show outside of the background
        camera.updatePosition(target);
        batch.setProjectionMatrix(camera.camera.combined);
=======
      
        if (!paused) {
            /// clamp camera to ensure it cannot show outside of the background
            clampCamera();
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            player.getPlayerSprite().begin();
            player.updatePlayer();
            player.getPlayerSprite().end();
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

>>>>>>> 32c0e8ac123b4fb7e0390c1e31f85ac72cb3d866
    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();

        player.getPlayerSprite().dispose();
    }
}
