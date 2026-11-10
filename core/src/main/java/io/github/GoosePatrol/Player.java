package io.github.GoosePatrol;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * Class with methods to instantiate, control, animate and render a player in the game.
 *
 * @version 1.1
 */
public class Player extends MovementEntity {
    // Loads the texture atlas from assets/sprites.atlas
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");

    private PlayerAction action = PlayerAction.IDLE;

    private Map<PlayerAction, Map<SpriteDirection, Animation<TextureRegion>>> animations =
        new HashMap<>();

    // Stores the player position in a Vector2 to make manipulating it with maths easier
    private Vector2 playerPosition = new Vector2(0, 0);
    // Initialises a degree offset to be used to determine which way the player is facing
    private float degreeOffset = 0f;
    // Controls how fast the player can move around the canvas (might be good for later debuffs)
    private float speed = 100f;

    private OrthographicCamera camera;

    private TiledMapTileLayer collisionLayer;

    private static float HITBOX_W = 14f;
    private static float HITBOX_H = 14f;

    /**
     * Constructor method to instantiate a player which takes the orthographic camera object and sets
     * the camera parameter to the camera attribute to later calculate degree offset (can be moved to
     * another input class soon if necessary).
     *
     * @param orthographicCamera Passes through the main camera object
     * @param map passes the map objectto the player
     */
    public Player(OrthographicCamera orthographicCamera, TiledMap map) {
        super();
        camera = orthographicCamera;
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        loadAnimations();
        initialiseSprite();
    }

    /**
     * Load animations and populate animation map
     */
    private void loadAnimations() {
        for (PlayerAction action : PlayerAction.values()) {
            Map<SpriteDirection, Animation<TextureRegion>> directionMap = new HashMap<>();

            for (SpriteDirection direction : SpriteDirection.values()) {
                Animation<TextureRegion> animation =
                    new Animation<TextureRegion>(
                        0.25f,
                        textureAtlas.findRegions("player/" + action.label + "/" + direction.label),
                        PlayMode.LOOP);

                directionMap.put(direction, animation);
            }

            animations.put(action, directionMap);
        }
    }

    /**
     * Method to update the movement based on key polling with a vector normalised and scaled by delta
     * time to maintain frame independence and sets the moving bool to help handle animations.
     */
    public void updateMovement() {
        // Initialises a zeroed Vector2 to track movement magnitude vector
        Vector2 playerMovementMagnitude = new Vector2(0, 0);

        // Selection statements using key polling to check if keys have been pressed
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            // Applies a positive magnitude on the y-axis to move the character up
            playerMovementMagnitude.y += 1;
        }
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            // Applies a negative magnitude on the x-axis to move the character left
            playerMovementMagnitude.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            // Applies a negative magnitude on the y-axis to move the character down
            playerMovementMagnitude.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            // Applies a positive magnitude on the x-axis to move the character right
            playerMovementMagnitude.x += 1;
        }

        if (!playerMovementMagnitude.isZero()) {
            // Calculates the position plus the magnitude scaled with speed multiplied by delta time to
            // make the movement frame independent
            playerPosition.mulAdd(playerMovementMagnitude.nor(), speed * Gdx.graphics.getDeltaTime());
            getSprite().setPosition(playerPosition.x, playerPosition.y);
            action = PlayerAction.WALK;
        } else {
            action = PlayerAction.IDLE;
        }
    }

    /**
     * Calculates the degree offset based on player position and mouse position and converts from
     * degrees.
     *
     * @return The cardinal or ordinal direction names of the sprite frames corresponding to an atlas
     * region name if combined with the alias concatenated by this and the alias again
     */
    public SpriteDirection directionFacing() {
        // Stores the mouse position in a vector 3
        Vector3 mousePosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    /* Takes the mouse position on both axes and minuses them from player position then takes the arctangent for radians and converts
    the angle into degrees */
        degreeOffset =
            (float) Math.atan2(mousePosition.y - playerPosition.y, mousePosition.x - playerPosition.x)
                * MathUtils.radiansToDegrees;

        // Checks the degree offset to see if the mouse on the left or the right of the player
        if (degreeOffset >= -22.5f && degreeOffset <= 22.5f
            || degreeOffset >= 157.5f && degreeOffset <= 180f
            || degreeOffset >= -180f && degreeOffset <= -157.5f) {
            return SpriteDirection.SIDE;
        }
        // Checks the degree offset to see if the mouse on the back incline of the player
        else if (degreeOffset >= 22.5f && degreeOffset <= 67.5f
            || degreeOffset >= 112.5f && degreeOffset <= 157.5f) {
            return SpriteDirection.BACKWARD_INCLINE;
        }
        // Checks the degree offset to see if the mouse on the front incline of the player
        else if (degreeOffset >= -157.5f && degreeOffset <= -112.5f
            || degreeOffset >= -67.5 && degreeOffset <= -22.5) {
            return SpriteDirection.FORWARD_INCLINE;
        }
        // Checks the degree offset to see if the mouse is above the player
        else if (degreeOffset >= 67.5f && degreeOffset <= 112.5f) {
            return SpriteDirection.BACKWARD;
        }
        // Defaultly assumes the mouse is below the player if all other selection returns false
        else {
            return SpriteDirection.FORWARD;
        }
    }

    /**
     * Getter function to return the player's position as a Vector2.
     *
     * @return The player's position
     */
    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    @Override
    protected Animation<TextureRegion> getCurrentAnimation() {
        return animations.get(action).get(directionFacing());
    }

    @Override
    protected boolean shouldFlipX() {
        return !(degreeOffset >= -90 && degreeOffset <= 90);
    }

    public void resetPos(){
        playerPosition.set(0f,0f);
        getSprite().setPosition(playerPosition.x-16f,playerPosition.y - 16f);
        action = PlayerAction.IDLE;
    }
    /**
     * checks if a proposed movement is allowed
     * @param x the x position of the possible move.
     * @param y the y position of the possible move.
     * @return true if the move is valid
     */
    private boolean canMoveTo(float x, float y){
        int tileW = collisionLayer.getTileWidth();
        int tileH = collisionLayer.getTileHeight();
        int cols = collisionLayer.getWidth();
        int rows = collisionLayer.getHeight();
        float[] xBorders = {x-HITBOX_W/2,x+HITBOX_H/2};
        float[] yBorders = {y-HITBOX_H/2,y+HITBOX_H/2};

        for (float xCandidate:xBorders){
            for (float yCandidate:yBorders){
                int col = (int) Math.floor(xCandidate/tileW);
                int row = rows -1 - (int) Math.floor(yCandidate/tileH);
                // out of border check
                if(col<0||col>=cols||row<0||row>=rows){
                    return false;
                }

                TiledMapTileLayer.Cell cell = collisionLayer.getCell(col,row);
                // collision test
                if(cell != null && cell.getTile() != null){
                    return false;
                }
            }
        }
        return true;
    }
}
