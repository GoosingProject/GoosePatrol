package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    public enum State {inactive, active, consumed, expired}
    private State state;
    private String itemName;
    private float itemDuration;
    private float remainingLife;
    protected Vector2 position = new Vector2();
    protected TextureRegion texture;
    protected float width = 32f;
    protected float height = 32f;

    /**
     * constructer for the item abstract class class
     * @param name
     * @param duration
     */
    protected Item(String name, float duration){
        this.itemName=name;
        this.itemDuration=duration;
        this.state = State.inactive;
    }

    /**
     * updates the remaining time of the item
     * @param deltaTime time since last update
     */
    public void update(float deltaTime, WorldContext world){
        if(state == State.active){
            remainingLife -= deltaTime;
            if(remainingLife <= 0f){
                state = State.expired;
                onExpire(world);
            }
            else{
                updateActive(world);
            }
        }
    }

    /**
     * spawn the item in the world
     * @param location the worldspace co-ordinates of the item to be spawned
     */
    public void spawn(Vector2 location){
        position.set(location);
        remainingLife = itemDuration;
        state = State.active;
    }
    /**
     * marks item as used
     */
    protected void markConsumed(){
        state = State.consumed;
    }
    /**
     * draws the item in the world
     * @param batch the batch in which to draw the item
     */
    public void render(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture,
                position.x - width / 2f,
                position.y - height / 2f,
                width, height);
        }
    }
    /**to be called by the inventory class when an item is selected */
    public abstract void use(Player player, Vector2 itemPosition, WorldContext world);

    public boolean isActive(){
        return state == State.active;
    }

    public String getName(){
        return itemName;
    }
    /** cleanup when item lifetime runs out */
    protected void onExpire(WorldContext world){}
    /** per frame hook for deployed items */
    protected void updateActive(WorldContext world){}

    public float getRemaining(){
        return remainingLife;
    }
}
