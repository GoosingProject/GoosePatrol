package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** a box that grants random item when player touches it */
public class ItemBox {
    private Vector2 positon = new Vector2();
    private TextureRegion texture;
    private boolean collected = false;
    public ItemBox(Vector2 position, TextureRegion texture){
        this.positon.set(positon);
        this.texture = texture;
    }

}
