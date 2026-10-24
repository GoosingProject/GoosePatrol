package io.github.GoosePatrol;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Controls the logic of the camera
 */

public class CameraLogic {
    // View width and height maintain specific aspect ratio of the camera
    private float viewWidth = 160f;
    private float viewHeight = 90f;
    public OrthographicCamera camera;
    private Texture background;
    private Viewport screenScaler;
    /**
     * constructor for cameraLogic.
     * @param width = the width of the viewport.
     * @param height = the height of the viewport.
     * @param back = the background image.
     */
    public CameraLogic(float width, float height, Texture back){
        this.viewHeight = height;
        this.viewWidth = width;
        this.camera = new OrthographicCamera();
        this.screenScaler = new FitViewport(viewWidth, viewHeight,camera);
        this.background = back;
    }

    public void updatePosition(Vector2 cameraPosition){
        camera.position.lerp(new Vector3(cameraPosition.x, cameraPosition.y, 0f), Gdx.graphics.getDeltaTime() * 10);
        clampCamera();
        camera.update();
    }

    public void updateScale(int width, int height){
        screenScaler.update(width, height, false);
    }

    private void clampCamera(){
        // stores the width and height to the centre of the camera
        float halfW = camera.viewportWidth *camera.zoom/2f;
        float halfH = camera.viewportHeight *camera.zoom/2f;
        // stores the width and height of the background image
        float imgW = background.getWidth();
        float imgH = background.getHeight();
        // camera.position is the cente of the view
        // clamps the position half a view away from the image edges
        camera.position.x = MathUtils.clamp(camera.position.x, halfW, imgW-halfW);
        camera.position.y = MathUtils.clamp(camera.position.y, halfH, imgH-halfH);
    }
}
