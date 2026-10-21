package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

/** Class to instantiate a player into the game
 *
 * @version 1.0
 */
public class Player {
    private Texture playerTexture;

    private Vector2 playerPosition = new Vector2(0,0);
    private float speed = 100f;

    /** Constructor method
     *
     */
    public Player() {
        // manually sets texture to character sprite
        playerTexture = new Texture("libgdx.png");
    }

    /**
     *
     */
    private void updateMovement() {
        // movement magnitude vector
        Vector2 playerMovementMagnitude = new Vector2(0,0);

        // move up
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            playerMovementMagnitude.y += 1;
        }
        // move left
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerMovementMagnitude.x -= 1;
        }
        // move down
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            playerMovementMagnitude.y -= 1;
        }
        // move right
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerMovementMagnitude.x += 1;
        }

        // checks if magnitude isn't zero
        if (!playerMovementMagnitude.isZero()) {
            // if so then calculate the position plus the magnitude scaled with speed multiplied by delta time to make the game frame independent
            playerPosition.mulAdd(playerMovementMagnitude.nor(), speed * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     *
     * @param playerSprite
     */
    public void updatePlayer(SpriteBatch playerSprite) {
        // draws the sprite itself
        playerSprite.draw(playerTexture, playerPosition.x, playerPosition.y);
        // updates movement
        updateMovement();
    }
}
