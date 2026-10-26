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

/**
 * Class with methods to instantiate, control, animate and render a player in the game
 *
 * @version 1.0
 */
public class Player {
    // Loads the texture atlas from assets/sprites.atlas
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");

    // Loads all the animations preliminarily in their forward state to avoid any errors for methods that require the last result
    private Animation<TextureRegion> idleAnimation = new Animation<TextureRegion>(0.25f,textureAtlas.findRegions("idle/forward-idle"), PlayMode.LOOP);
    private Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/forward-walk"), PlayMode.LOOP);
    private Animation<TextureRegion> interactAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/forward-interact"), PlayMode.LOOP);
    private Animation<TextureRegion> feedAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/forward-feed"), PlayMode.LOOP);
    // Initialises state time to be used for playing animations
    private float stateTime = 0f;
    // Bool to be set to true when the player is moving
    private boolean moving = false;

    // Stores the player position in a Vector2 to make manipulating it with maths easier
    private Vector2 playerPosition = new Vector2(0,0);
    // Initialises a degree offset to be used to determine which way the player is facing
    private float degreeOffset = 0f;
    // Controls how fast the player can move around the canvas (might be good for later debuffs)
    private float speed = 100f;

    // Initialises a rectangular area of texture to be applied to a sprite to style it
    private TextureRegion textureRegion = idleAnimation.getKeyFrame(stateTime, true);
    // Initialises an idle player sprite
    private Sprite playerSprite = new Sprite(idleAnimation.getKeyFrame(stateTime, true));
    private OrthographicCamera camera;

    /**
     * Constructor method to instantiate a player which takes the orthographic camera object and sets the camera parameter
     * to the camera attribute to later calculate degree offset (can be moved to another input class soon if necessary)
     *
     * @param orthographicCamera Passes through the main camera object
     */
    public Player(OrthographicCamera orthographicCamera) {
        camera = orthographicCamera;
    }

    /**
     * Method to update the movement based on key polling with a vector normalised and scaled by delta time to maintain
     * frame independence and sets the moving bool to help handle animations
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
            playerSprite.setPosition(playerPosition.x - 16, playerPosition.y - 16);
            // Bool signifies player is in motion
            moving = true;
        }
        else {
            // Bool signifies player is idle
            moving = false;
        }
    }

    /**
     * Method to play the player animation, appropriately flip if character is facing left (from player perspective) and to then draw the
     * sprite onto the sprite batch intended to be called in the Main class
     *
     * @param batch Passes SpriteBatch in such that the player sprite can render directly on the sprite batch with all other sprites
     */
    public void drawPlayer(SpriteBatch batch) {
        // Adds delta time to the state time to properly time the animations
        stateTime += Gdx.graphics.getDeltaTime();

        /* Figures out which way the player is facing by using the directionFacing method which returns a string corresponding to animation
        file name. Then uses selection to determine whether a walking or idle animation should play */
        if (moving) {
            walkAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("walk/" + directionFacing() + "-walk"), PlayMode.LOOP);
            textureRegion = walkAnimation.getKeyFrame(stateTime, true);
        }
        else {
            idleAnimation = new Animation<TextureRegion>(0.25f, textureAtlas.findRegions("idle/" + directionFacing() + "-idle"), PlayMode.LOOP);
            textureRegion = idleAnimation.getKeyFrame(stateTime, true);
        }

        /* Checks the degree offset to determine whether to flip the texture region (globally flips so can't instantiate player) and uses
        the result to determine which way it should be (i.e. flipped or true) */
        boolean flipPlayer = !(degreeOffset >= -90 && degreeOffset <= 90);
        if (textureRegion.isFlipX() != flipPlayer){
            textureRegion.flip(true, false);
        }

        // Sets the texture to the player sprite and draws it onto the sprite batch in main
        playerSprite.setRegion(textureRegion);
        playerSprite.draw(batch);
    }

    /**
     * Calculates the degree offset based on player position and mouse position and converts from degrees
     *
     * @return The cardinal or ordinal direction names of the sprite frames corresponding to an atlas region name if combined with the
     * alias concatenated by this and the alias again
     */
    public String directionFacing() {
        // Stores the mouse position in a vector 3
        Vector3 mousePosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        /* Takes the mouse position on both axes and minuses them from player position then takes the arctangent for radians and converts
        the angle into degrees */
        degreeOffset = (float)Math.atan2(mousePosition.y - playerPosition.y, mousePosition.x - playerPosition.x) * MathUtils.radiansToDegrees;

        // Checks the degree offset to see if the mouse on the left or the right of the player
        if (degreeOffset >= -22.5f && degreeOffset <= 22.5f ||
            degreeOffset >= 157.5f && degreeOffset <= 180f ||
            degreeOffset >= -180f && degreeOffset <= -157.5f) {
            return "side";
        }
        // Checks the degree offset to see if the mouse on the back incline of the player
        else if (degreeOffset >= 22.5f && degreeOffset <= 67.5f ||
                degreeOffset >= 112.5f && degreeOffset <= 157.5f) {
            return "backward-incline";
        }
        // Checks the degree offset to see if the mouse on the front incline of the player
        else if (degreeOffset >= -157.5f && degreeOffset <= -112.5f ||
                degreeOffset >= -67.5 && degreeOffset <= -22.5) {
            return "forward-incline";
        }
        // Checks the degree offset to see if the mouse is above the player
        else if (degreeOffset >= 67.5f && degreeOffset <= 112.5f) {
            return "backward";
        }
        // Defaultly assumes the mouse is below the player if all other selection returns false
        else {
            return "forward";
        }
    }

    /**
     * Getter function to return a players sprite batch
     *
     * @return The player sprite
     */
    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    /**
     * Getter function to return the players position as a Vector2
     *
     * @return The players position
     */
    public Vector2 getPlayerPosition() {
        return playerPosition;
    }
}
