package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FoodItem extends Item{
    private static float LIFETIME = 5f;
    private static float RADIUS = 150f;

    private float pulseTimer = 0f;

    public FoodItem(TextureRegion sprite){
        super("Bread", LIFETIME, sprite);
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
    }
    @Override 
    public void use(Player player, Vector2 itemPostion, WorldContext world){
        spawn(itemPostion);
        world.addActiveItem(this);
        world.attractGeese(position, RADIUS);
    }
    @Override 
    protected void updateActive(WorldContext world, float deltaTime){
        world.attractGeese(position, RADIUS);
    }

}
