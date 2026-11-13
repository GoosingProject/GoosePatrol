package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

/**
 * rolls random items on pickup
 */
public class ItemRoller {
    private Texture scareTexture;
    private Texture breadTexture;
    private Random random = new Random();
    /**
     * constructer for the item roller class
     * @param scareTexture the scare item sprite
     * @param breadTexture the food item sprite
     */
    public ItemRoller(Texture scareTexture, Texture breadTexture) {
        this.scareTexture = scareTexture;
        this.breadTexture = breadTexture;
    }
    /**
     * 
     * @return a random item object
     */
    public Item rollItem() {
        if (random.nextBoolean()) {
            return new ScareItem(new TextureRegion(scareTexture));
        } else {
            return new FoodItem(new TextureRegion(breadTexture));
        }
    }
}
