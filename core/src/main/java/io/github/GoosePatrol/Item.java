package io.github.GoosePatrol;
/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    private String itemName;
    private float itemDuration;
    protected Item(String name, float duration){
        this.itemName=name;
        this.itemDuration=duration;
    }

    public void triggerAbility(){
        
    }

    public void changeDuration(float deltaTime){
        itemDuration -= deltaTime;
    }

    public boolean pickUpItem(){
        return true;
    }

    public void spawn(){

    }
}
