package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;
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
    protected TextureRegion Texture;

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
    public void changeDuration(float deltaTime){
        if(state == State.active){
            remainingLife -= deltaTime;
            if(remainingLife <= 0f){
                state = State.inactive;
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

    public boolean isActive(){
        return state == State.active;
    }

    public String getName(){
        return itemName;
    }

    public float getRemaining(){
        return remainingLife;
    }
}
