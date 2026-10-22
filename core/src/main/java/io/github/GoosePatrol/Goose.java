package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for a single enemy Goose
 */
public class Goose {
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private State state = State.PATROL;
    private Texture texture;

    /**
     * Constructor for Goose object
     * @param x Starting x position
     * @param y Starting y position
     * @param texture Texture to be rendered
     */
    public Goose(float x, float y, Texture texture) {
        this.position.x = x;
        this.position.y = y;
        this.texture = texture;
    }

    /**
     * Update goose's state and position
     * @param deltaTime
     * @param playerPosition
     */
    public void update(float deltaTime, Vector2 playerPosition) {

    }

    /**
     * Render goose texture to screen
     * @param batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, this.position.x, this.position.y);
    }

}

/**
 * States a goose can be in:
 * Patrol - Default idle state
 * Chase - Chase after player
 */
enum State {
    PATROL,
    CHASE,
}
