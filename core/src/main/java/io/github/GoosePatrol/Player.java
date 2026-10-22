package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

/**
 * Class with methods to instantiate, render and control a player in the game
 * @version 1.0
 */
public class Player {
    private SpriteBatch playerSprite;
    private Texture playerTexture;

    private Vector2 playerPosition = new Vector2(0,0);
    private float speed = 100f;

    /**
     * Constructor method to instantiate a player and set its texture to the specified, hardcoded path (currently placeholder)
     */
    public Player() {
        playerSprite = new SpriteBatch();
        playerTexture = new Texture("libgdx.png");
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
            // Applies a negative magnitude on the x-axis to move the character up
            playerMovementMagnitude.x -= 1;
        }
        // Applies a negative magnitude on the y-axis to move the character up
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            playerMovementMagnitude.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            // Applies a positive magnitude on the x-axis to move the character up
            playerMovementMagnitude.x += 1;
        }

        if (!playerMovementMagnitude.isZero()) {
            // Calculates the position plus the magnitude scaled with speed multiplied by delta time to make the movement frame independent
            playerPosition.mulAdd(playerMovementMagnitude.nor(), speed * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Method to draw the player, intended to be called in Main class
     */
    public void drawPlayer() {
        playerSprite.draw(playerTexture, playerPosition.x, playerPosition.y);
    }

    /**
     * Getter function to return a players sprite batch
     *
     * @return the player sprite
     */
    public SpriteBatch getPlayerSprite() {
        return playerSprite;
    }
}
