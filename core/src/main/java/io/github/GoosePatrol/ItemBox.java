package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** a box that grants random item when player touches it */
public class ItemBox {
    private static float LIFETIME = 60f;


    private Vector2 positon = new Vector2();
    private TextureRegion texture;
    private boolean collected = false;
    private float lifeRemaining = LIFETIME;
    private boolean expired = false;
    public ItemBox(Vector2 position, TextureRegion texture){
        this.positon.set(positon);
        this.texture = texture;
    }
    public void update(float deltaTime){
        if (!expired){
            lifeRemaining -= deltaTime;
            if (lifeRemaining <=0f ){
                expired = true;
            }
        }
    }
}
