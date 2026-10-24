package io.github.GoosePatrol;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;


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
    /**
     * update the positon of the camera to follow the target.
     * @param target
     */
    public void updatePosition(Player target){
        camera.position.x = target.getPlayerPosition().x;
        camera.position.y = target.getPlayerPosition().y;
        clampCamera();
        camera.update();
    }
    /**
     * updates the scale of the viewport inteded to be called in the resize method() of main
     * @param width
     * @param height
     */
    public void updateScale(int width, int height){
        screenScaler.update(width, height, false);
    }
    /**
     * clamps the camera to the background image so it cannot show anything outside of that
     */
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
