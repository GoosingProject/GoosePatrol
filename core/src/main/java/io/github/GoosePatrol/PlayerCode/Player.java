package io.github.GoosePatrol.PlayerCode;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.GoosePatrol.HelperClasses.MovementEntity;
import io.github.GoosePatrol.HelperClasses.SpriteDirection;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    private Vector2 playerPosition = new Vector2(1080, 1300);
    // Initialises a degree offset to be used to determine which way the player is facing
    private float degreeOffset = 0f;
    // Controls how fast the player can move around the canvas (might be good for later debuffs)
    private float speed = 100f;

    private OrthographicCamera camera;

    private static float INVINCIBILITY_DURATION = 1.5f;
    private static float FLASH_INTERVAL = 0.1f;
    private float invincibilityTimer = 0f;
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
        getSprite().setPosition(playerPosition.x - getSprite().getWidth() / 2f, playerPosition.y - getSprite().getHeight() / 2f);
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
            // creates the movement vector for the frame without applying it so it can be tested first.
            Vector2 delta = playerMovementMagnitude.nor().scl(speed*Gdx.graphics.getDeltaTime());
            if(canMoveTo(playerPosition.x+delta.x, playerPosition.y+delta.y)){
                playerPosition.add(delta);
            }
            else{
                // against a wall try single axis movement.
                if (canMoveTo(playerPosition.x + delta.x, playerPosition.y)){
                    playerPosition.x+=delta.x;
                } 
                else if (canMoveTo(playerPosition.x,playerPosition.y+delta.y)){
                    playerPosition.y+= delta.y;
                }
            }
            getSprite().setPosition(playerPosition.x - getSprite().getWidth() / 2f, playerPosition.y - getSprite().getHeight() / 2f);
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
        playerPosition.set(1080,1300);
        getSprite().setPosition(playerPosition.x-16f,playerPosition.y - 16f);
        action = PlayerAction.IDLE;
        sleepAnimTimer = 0f;
        invincibilityTimer = 0f;
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
    @Override public void draw(SpriteBatch batch){
        boolean fading = isInvincible() && isFlashOff();
        if(fading){
            getSprite().setAlpha(0.3f);
        }
        super.draw(batch);
        if(fading){
            getSprite().setAlpha(1f);
        }
    }
    private boolean isFlashOff(){
        return ((int)(invincibilityTimer/FLASH_INTERVAL))%2==0;
    }
    /**  checks if a proposed movement is allowed
     * @param x the x position of the possible move.
     * @param y the y position of the possible move.
     * @return true if the move is valid
     */
    private boolean canMoveTo(float x, float y){
        int tileW = collisionLayer.getTileWidth();
        int tileH = collisionLayer.getTileHeight();
        int cols = collisionLayer.getWidth();
        int rows = collisionLayer.getHeight();
        float[] xBorders = {x-HITBOX_W/2,x+HITBOX_W/2};
        float[] yBorders = {y-10-HITBOX_H/2,y-10+HITBOX_H/2};

        for (float xCandidate:xBorders){
            for (float yCandidate:yBorders){
                int col = (int) Math.floor(xCandidate/tileW);
                int row = (int) Math.floor(yCandidate/tileH);
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
    /**
     * @return the duration of the falling asleep animation
     */
    public float getFallingAsleepDuration(){
        return animations.get(PlayerAction.FALLING_ASLEEP).get(SpriteDirection.FORWARD).getAnimationDuration();
    }
}
