package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;
/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    private String itemName;
    private float itemDuration;
    private float remainingLife;
    protected Vector2 position = new Vector2();
    protected Item(String name, float duration){
        this.itemName=name;
        this.itemDuration=duration;
    }

    public void triggerAbility(){
        
    }

    public void changeDuration(float deltaTime){
        remainingLife -= deltaTime;
    }

    public boolean pickUpItem(){
        return true;
    }

    public void spawn(Vector2 location){
        position.set(location);
        remainingLife = itemDuration;
    }
}
