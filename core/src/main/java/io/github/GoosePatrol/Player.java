package io.github.GoosePatrol;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Animation;

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

    private static float INVINCIBILITY_DURATION = 1.5f;
    private static float FLASH_INTERVAL = 0.1f;
    private float invincibilityTimer = 0f;

    /**
     * Constructor method to instantiate a player which takes the orthographic camera object and sets
     * the camera parameter to the camera attribute to later calculate degree offset (can be moved to
     * another input class soon if necessary).
     *
     * @param orthographicCamera Passes through the main camera object
     */
    public Player(OrthographicCamera orthographicCamera) {
        super();
        camera = orthographicCamera;
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
        // Prevents movement when player is in a sleep state
        if (this.action == PlayerAction.SLEEP || this.action == PlayerAction.FALLING_ASLEEP) {
            return;
        }

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

    private float sleepAnimTimer = 0f;

    @Override
    protected Animation<TextureRegion> getCurrentAnimation() {
        if (action == PlayerAction.FALLING_ASLEEP) {
            sleepAnimTimer += Gdx.graphics.getDeltaTime();
            Animation<TextureRegion> fallAsleepAnim = animations.get(PlayerAction.FALLING_ASLEEP).get(SpriteDirection.FORWARD);

            if (fallAsleepAnim.isAnimationFinished(sleepAnimTimer)) {
                action = PlayerAction.SLEEP;
                return animations.get(PlayerAction.SLEEP).get(SpriteDirection.FORWARD);
            }
            return fallAsleepAnim;
        }
        if (action == PlayerAction.SLEEP) {
            return animations.get(PlayerAction.SLEEP).get(SpriteDirection.FORWARD);
        }
        return animations.get(action).get(directionFacing());
    }

    @Override
    protected boolean shouldFlipX() {
        if (action == PlayerAction.SLEEP || action == PlayerAction.FALLING_ASLEEP) {
            return false; // prevents the sprite from flipping horizontally when playing the sleep animation
        }
        return !(degreeOffset >= -90 && degreeOffset <= 90);
    }

    public void resetPos(){
        playerPosition.set(0f,0f);
        getSprite().setPosition(playerPosition.x-16f,playerPosition.y - 16f);
        action = PlayerAction.IDLE;
        sleepAnimTimer = 0f;
    }
    /**
     * A setter to set the player's current action/animation state
     * @param newAction The PlayerAction to be assigned to the player.
     */
    public void setAction(PlayerAction newAction){
        this.action = newAction;
    }
    /**
     * @return true if the player is currently invincible
     */
    public boolean isInvincible(){
        return invincibilityTimer > 0f;
    }
    /**
     * attempt to damage player
     * @return true if player gets damage
     */
    public boolean takeDamage(){
        if(isInvincible()){
            return false;
        }
        invincibilityTimer = INVINCIBILITY_DURATION;
        return true;
    }
    /** lower the invinciblity timer, call every frame */
    public void updateInvincibility(float deltaTime){
        if(invincibilityTimer>0f){
            invincibilityTimer-=deltaTime;
            if(invincibilityTimer<0f){
                invincibilityTimer = 0f;
            }
        }
    }
}
