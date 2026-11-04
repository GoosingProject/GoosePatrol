package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;

public class ScareItem extends Item{
    private static  float LIFETIME = 5f;
    public ScareItem(){
        super("Scarecrow", LIFETIME);
    }

    public void use(Player player, Vector2 itemPostion, WorldContext world){
        spawn(itemPostion);
        world.addActiveItem(this);
    }

}
