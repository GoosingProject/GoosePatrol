package io.github.GoosePatrol;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.MapProperties;

/**
 * Class to control the logic of the camera.
 */
public class CameraLogic {
    // View width and height to maintain the specific aspect ratio (16:9) of the camera.
    private float viewWidth = 160f;
    private float viewHeight = 90f;
    private OrthographicCamera camera;
    private TiledMap map;
    private Viewport screenScaler;

    /**
     * Constructor method to instantiate a camera.
     *
     * @param width  The width of the viewport
     * @param height The height of the viewport
     * @param map    The map
     */
    public CameraLogic(float width, float height, TiledMap map) {
        this.viewWidth = width;
        this.viewHeight = height;
        this.camera = new OrthographicCamera();
        this.screenScaler = new FitViewport(viewWidth, viewHeight, camera);
        this.map = map;
    }

    /**
     * Update the positon of the camera to follow the target.
     *
     * @param cameraPosition A vector containing the position to move the camera to
     */
    public void updatePosition(Vector2 cameraPosition) {
        camera.position.lerp(
            new Vector3(cameraPosition.x, cameraPosition.y, 0f), Gdx.graphics.getDeltaTime() * 10);
        clampCamera();
        camera.update();
    }

    /**
     * Updates the scale of the viewport intended to be called in the resize method() of main.
     *
     * @param width  The width of the viewport
     * @param height The height of the viewport
     */
    public void updateScale(int width, int height) {
        screenScaler.update(width, height, false);
    }

    /**
     * Clamps the camera to the background image so it cannot show anything outside.
     */
    private void clampCamera() {
        // stores the width and height to the centre of the camera
        float halfWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfHeight = camera.viewportHeight * camera.zoom / 2f;

        // stores the width and height of the map
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        int mapHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);

        // camera.position is the centre of the view
        // clamps the position half a view away from the maps edges
        camera.position.x = MathUtils.clamp(camera.position.x, halfWidth, mapWidth - halfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, halfHeight, mapHeight - halfHeight);
    }

    /**
     * Getter function that returns the camera.
     *
     * @return The orthographic camera object
     */
    public OrthographicCamera getCamera() {
        return camera;
    }
}
