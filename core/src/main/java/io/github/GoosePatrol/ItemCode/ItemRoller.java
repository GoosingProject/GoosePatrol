package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

/** rolls random items on pickup */
public class ItemRoller {
    private Texture scareTexture;
    private Texture breadTexture;
    private Random random = new Random();

    public ItemRoller(Texture scareTexture, Texture breadTexture){
        this.scareTexture = scareTexture;
        this.breadTexture = breadTexture;
    }

    public Item rollItem(){
        if (random.nextBoolean()){
            return new ScareItem(new TextureRegion(scareTexture));
        }else{
            return new FoodItem(new TextureRegion(breadTexture));
        }
    }
}
