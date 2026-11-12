package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.math.Vector2;
import io.github.GoosePatrol.Player;
import io.github.GoosePatrol.WorldContext;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    public enum State {INACTIVE, ACTIVE, EXPIRED}

    private State state;
    private String itemName;
    private float itemDuration;
    private float remainingLife;
    protected Vector2 position = new Vector2();
    protected TextureRegion texture;
    protected float width = 32f;
    protected float height = 32f;
    protected float scale = 1f;

    /**
     * constructer for the item abstract class class
     *
     * @param name
     * @param duration
     */
    protected Item(String name, float duration, TextureRegion sprite) {
        this.itemName = name;
        this.itemDuration = duration;
        this.state = State.INACTIVE;
        this.texture = sprite;
    }

    /**
     * updates the remaining time of the item
     *
     * @param deltaTime time since last update
     */
    public void update(float deltaTime, WorldContext world) {
        if (state == State.ACTIVE) {
            remainingLife -= deltaTime;
            if (remainingLife <= 0f) {
                state = State.EXPIRED;
                onExpire(world);
            } else {
                updateActive(world, deltaTime);
            }
        }
    }

    /**
     * spawn the item in the world
     *
     * @param location the worldspace co-ordinates of the item to be spawned
     */
    public void spawn(Vector2 location) {
        position.set(location);
        remainingLife = itemDuration;
        state = State.ACTIVE;
    }

    /**
     * draws the item in the world
     *
     * @param batch the batch in which to draw the item
     */
    public void render(SpriteBatch batch) {
        if (texture != null) {
            float w = width * scale;
            float h = height * scale;
            batch.draw(texture,
                position.x - w / 2f,
                position.y - h / 2f,
                w, h);
        }
    }

    /**
     * to be called upon player using item.
     * @param player    the player object using the item.
     * @param itemPosition  the position vector of the item.
     * @param world     the worldcontex object that tracks this item.
    */
    public abstract void use(Player player, Vector2 itemPosition, WorldContext world);
    /**
     * @return true if this item is active
     */
    public boolean isActive() {
        return state == State.ACTIVE;
    }
    /**
     * @return the name of the item
     */
    public String getName() {
        return itemName;
    }

    /**
     * cleanup when item lifetime runs out
     */
    protected void onExpire(WorldContext world) {
    }

    /**
     * per frame hook for deployed items
     */
    protected void updateActive(WorldContext world, float deltaTime) {
    }
    /**
     * @return the remaining time the item will exist.
     */
    public float getRemaining() {
        return remainingLife;
    }
}
