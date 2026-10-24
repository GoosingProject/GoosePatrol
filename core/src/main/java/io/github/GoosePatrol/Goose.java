package io.github.GoosePatrol;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for a single enemy Goose
 */
public class Goose {
    static final float SEPARATION_RADIUS = 30;
    static final float BOUNDARY_RADIUS = 30;
    static final float PATROL_WEIGHT = 1;
    static final float SEPARATION_WEIGHT = 3;
    static final float MAX_SPEED = 5;
    static final float MAX_FORCE = 0.2f;

    private int worldWidth;
    private int worldHeight;

    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private State state = State.PATROL;
    private Texture texture;
    private Vector2 patrolDirection = new Vector2();

    /**
     * Constructor for Goose object
     * @param x Starting x position
     * @param y Starting y position
     * @param texture Texture to be rendered
     * @param worldWidth Width of the world
     * @param worldHeight Height of the world
     */
    public Goose(float x, float y, Texture texture, int worldWidth, int worldHeight) {
        this.position.x = x;
        this.position.y = y;
        this.texture = texture;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    /**
     * Update goose's velocity
     * @param geese Collection of all geese
     */
    public void updateVelocity(ArrayList<Goose> geese) {
        if (this.state == State.PATROL) {
            this.updateVelocityPatrol(geese);
        }
    }

    /**
     * Update goose's state and position
     * @param deltaTime
     * @param playerPosition
     * @param geese Collection of all geese
     */
    public void update(float deltaTime, Vector2 playerPosition, ArrayList<Goose> geese) {
        this.position.mulAdd(this.velocity, deltaTime);
    }

    /**
     * Update the goose's velocity for patrolling
     * @param geese Collection of all geese
     */
    private void updateVelocityPatrol(ArrayList<Goose> geese) {
        // Sometimes pick a new random direction
        if ((int)(Math.random() * 10001) < 5) {
            this.patrolDirection.setToRandomDirection();
        }

        // Make goose avoid other geese
        Vector2 separation = new Vector2();
        for (Goose g : geese) {
            if (g == this) continue;

            Vector2 offset = this.position.cpy().sub(g.getPosition());
            float gooseDistance = offset.len();
            if (gooseDistance > 0 && gooseDistance < SEPARATION_RADIUS) {
                offset.nor();
                offset.scl(1 - gooseDistance / SEPARATION_RADIUS);
                separation.add(offset);
            }
        }

        // Make goose avoid world boundaries
        if (this.position.x < BOUNDARY_RADIUS) {
            this.patrolDirection.x = Math.abs(this.patrolDirection.x);
        } else if (this.position.x > this.worldWidth - BOUNDARY_RADIUS) {
            this.patrolDirection.x = -Math.abs(this.patrolDirection.x);
        }

        if (this.position.y < BOUNDARY_RADIUS) {
            this.patrolDirection.y = Math.abs(this.patrolDirection.y);
        } else if (this.position.y > this.worldHeight - BOUNDARY_RADIUS) {
            this.patrolDirection.y = -Math.abs(this.patrolDirection.y);
        }

        // Apply forces to velocity
        Vector2 desiredVelocity = new Vector2();
        desiredVelocity.mulAdd(this.patrolDirection, PATROL_WEIGHT);
        desiredVelocity.mulAdd(separation, SEPARATION_WEIGHT);
        desiredVelocity.nor().scl(MAX_SPEED);
        Vector2 steering = desiredVelocity.cpy().sub(this.velocity);
        steering.limit(MAX_FORCE);
        this.velocity.add(steering);
        this.velocity.limit(MAX_SPEED);
    }

    /**
     * Render goose texture to screen
     * @param batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.texture, this.position.x, this.position.y);
    }

    /**
     * Get goose's position
     * @return Copy of position vector
     */
    public Vector2 getPosition() {
        return this.position.cpy();
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
