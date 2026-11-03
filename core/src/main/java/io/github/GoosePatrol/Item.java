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
    protected Item(String name, float duration){
        this.itemName=name;
        this.itemDuration=duration;
        this.active = false;
    }

    public void triggerAbility(){
        
    }

    public void changeDuration(float deltaTime){
        if(active){
            remainingLife -= deltaTime;
            if(remainingLife <= 0f){
                active = false;
            }
        }
    }

    public boolean pickUpItem(){
        return true;
    }

    public void spawn(Vector2 location){
        position.set(location);
        remainingLife = itemDuration;
        active = true;
    }
}
