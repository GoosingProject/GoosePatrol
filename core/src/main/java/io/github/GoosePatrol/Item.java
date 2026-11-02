package io.github.GoosePatrol;
/**
 * abstact class describing a general item.
 * Item
 */
public abstract class Item {
    private String itemName;
    private Float itemDuration;
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
