package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ScareItem extends Item{
    private static float LIFETIME = 5f;
    private static float RADIUS = 150f;
    private static float INTERVAL = 0.5f;

    private float pulseTimer = 0f;

    public ScareItem(TextureRegion sprite){
        super("Scarecrow", LIFETIME, sprite);
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
    }
    @Override 
    public void use(Player player, Vector2 itemPostion, WorldContext world){
        spawn(itemPostion);
        world.addActiveItem(this);
    }
    @Override 
    protected void updateActive(WorldContext world, float deltaTime){
        pulseTimer += deltaTime;
        while(pulseTimer >= INTERVAL){
            pulseTimer -= INTERVAL;
            world.scareGeese(position, RADIUS);
        }
    }

}
