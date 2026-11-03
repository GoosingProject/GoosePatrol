package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    private String itemName;
    private float itemDuration;
    private float remainingLife;
    private boolean active;
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
        this.active = false;
    }

    /**
     * updates the remaining time of the item
     * @param deltaTime time since last update
     */
    public void changeDuration(float deltaTime){
        if(active){
            remainingLife -= deltaTime;
            if(remainingLife <= 0f){
                active = false;
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
        active = true;
    }

    public boolean isActive(){
        return active;
    }

    public String getName(){
        return itemName;
    }

    public float getRemaining(){
        return remainingLife;
    }
}
