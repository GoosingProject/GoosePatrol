package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.HelperClasses.WorldContext;
import io.github.GoosePatrol.PlayerCode.Player;
/**
 * class describing a food item that restores energy and attracts geese.
 * FoodItem
 */
public class FoodItem extends Item {
    private static float LIFETIME = 5f;
    private static float RADIUS = 100f;
    private static float ENERGY_RESTORE = 20f;
    /**
     * constructer for the FoodItem class
     * @param sprite sprite for the food item
     */
    public FoodItem(TextureRegion sprite) {
        super("Bread", LIFETIME, sprite);
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.scale = 16f / texture.getRegionWidth();
    }

    @Override
    public void use(Player player, Vector2 itemPostion, WorldContext world) {
        spawn(itemPostion);
        world.addActiveItem(this);
        world.attractGeese(position, RADIUS);
        world.addPlayerEnergy(ENERGY_RESTORE);
    }

    @Override
    protected void updateActive(WorldContext world, float deltaTime) {
        world.attractGeese(position, RADIUS);
    }
}
