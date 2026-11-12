package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.GoosePatrol.Player;
import io.github.GoosePatrol.WorldContext;

/**
 * class describing a scare item that scares geese away from its position.
 * ScareItem
 */
public class ScareItem extends Item {
    private static float LIFETIME = 5f;
    private static float RADIUS = 150f;
    private static float INTERVAL = 0.5f;

    private float pulseTimer = 0f;

    /**
     * constructer for the ScareItem class.
     * @param sprite the scare item sprite
     */
    public ScareItem(TextureRegion sprite) {
        super("Scarecrow", LIFETIME, sprite);
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.scale = 16f / texture.getRegionWidth();
    }

    @Override
    public void use(Player player, Vector2 itemPostion, WorldContext world) {
        spawn(itemPostion);
        world.addActiveItem(this);
        world.scareGeese(itemPostion, RADIUS);
    }

    @Override
    protected void updateActive(WorldContext world, float deltaTime) {
        pulseTimer += deltaTime;
        while (pulseTimer >= INTERVAL) {
            pulseTimer -= INTERVAL;
            world.scareGeese(position, RADIUS);
        }
    }

}
