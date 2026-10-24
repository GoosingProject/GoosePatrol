package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Class with methods to instantiate, render and control a player in the game
 * @version 1.0
 */

public class Player {
    private Sprite playerSprite;
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> interactAnimation;
    private Animation<TextureRegion> feedAnimation;
    private float stateTime;

    private Vector2 playerPosition = new Vector2(0,0);
    private float degreeOffset;
    private float speed = 100f;

    private boolean moving;

    private OrthographicCamera camera;

    /**
     * Constructor method to instantiate a player and set its texture to the specified, hardcoded path (currently placeholder)
     */
    public Player(OrthographicCamera orthographicCamera) {
        camera = orthographicCamera;

        textureAtlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));

        idleAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/forward-idle"), PlayMode.LOOP);
        walkAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("walk/forward-walk"), PlayMode.LOOP);

        playerSprite = new Sprite(idleAnimation.getKeyFrame(stateTime, true));
        textureRegion = idleAnimation.getKeyFrame(stateTime, true);
    }

    /**
     * Method to update the movement based on key polling with a vector normalised and scaled by delta time to maintain frame independence
     */
    public void updateMovement() {
        // Initialises a zeroed Vector2 to track movement magnitude vector
        Vector2 playerMovementMagnitude = new Vector2(0,0);

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
            // Calculates the position plus the magnitude scaled with speed multiplied by delta time to make the movement frame independent
            playerPosition.mulAdd(playerMovementMagnitude.nor(), speed * Gdx.graphics.getDeltaTime());
            playerSprite.setPosition(playerPosition.x - 8, playerPosition.y - 16);
            moving = true;
        }
        else {
            moving = false;
        }
    }

    /**
     * Method to draw the player, intended to be called in Main class
     *
     * @param batch Passes SpriteBatch in such that the player sprite can render directly on the sprite batch with all other sprites
     */
    public void drawPlayer(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (moving) {
            walkAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("walk/" + directionFacing(textureRegion) + "-walk"), PlayMode.LOOP);
            textureRegion = walkAnimation.getKeyFrame(stateTime, true);
        }
        else {
            idleAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/" + directionFacing(textureRegion) + "-idle"), PlayMode.LOOP);
            textureRegion = idleAnimation.getKeyFrame(stateTime, true);
        }

        boolean flipPlayer = !(degreeOffset >= -90 && degreeOffset <= 90);
        if (textureRegion.isFlipX() != flipPlayer){
            textureRegion.flip(true, false);
        }

        playerSprite.setRegion(textureRegion);
        playerSprite.draw(batch);
    }

    public String directionFacing(TextureRegion textureRegion) {
        // Skeleton code to work out rotation
        Vector3 mousePosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        degreeOffset = (float)Math.atan2(mousePosition.y - playerPosition.y, mousePosition.x - playerPosition.x) * MathUtils.radiansToDegrees;

        // Facing Side
        if (degreeOffset >= -22.5f && degreeOffset <= 22.5f ||
            degreeOffset >= 157.5f && degreeOffset <= 180f ||
            degreeOffset >= -180f && degreeOffset <= -157.5f) {
            return "side";
        }
        // Facing Back Incline
        else if (degreeOffset >= 22.5f && degreeOffset <= 67.5f ||
                degreeOffset >= 112.5f && degreeOffset <= 157.5f) {
            return "backward-incline";
        }
        // Facing Front Incline
        else if (degreeOffset >= -157.5f && degreeOffset <= -112.5f ||
                degreeOffset >= -67.5 && degreeOffset <= -22.5) {
            return "forward-incline";
        }
        // Facing Back
        else if (degreeOffset >= 67.5f && degreeOffset <= 112.5f) {
            return "backward";
        }
        // Facing Front
        else {
            return "forward";
        }
    }

    /**
     * Getter function to return a players sprite batch
     *
     * @return the player sprite
     */
    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }
}
