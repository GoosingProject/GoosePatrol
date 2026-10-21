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
    // view Width and hieght maintain specific aspect ratio of the camera
    private float viewWidth = 160f;
    private float viewHieght = 90f;
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
        this.viewHieght = height;
        this.viewWidth = width;
        this.camera = new OrthographicCamera();
        this.screenScaler = new FitViewport(viewWidth, viewHieght,camera);
        this.background = back;
    }

    public void updatePosition(CameraTarget target){
        camera.position.x = target.getX();
        camera.position.y = target.getY();
        clampCamera();
        camera.update();
    }

    public void updateScale(int width, int height){
        screenScaler.update(width, height, false);
    }

    private void clampCamera(){
        // stores the width and hieght to the centre of the camera
        float halfW = camera.viewportWidth *camera.zoom/2f;
        float halfH = camera.viewportHeight *camera.zoom/2f;
        // stores the width an hieght of the background image
        float imgW = background.getWidth();
        float imgH = background.getHeight();
        // camera.positon is the cente of the view
        // clamps the positon half a view away from the image edges
        camera.position.x = MathUtils.clamp(camera.position.x, halfW, imgW-halfW);
        camera.position.y = MathUtils.clamp(camera.position.y, halfH, imgH-halfH);

    }
}
