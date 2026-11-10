package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * a bin placed in the world that allows the player to discard items
 * Bin
 */
public class Bin {
    private static float RADIUS = 100f;
    private static float SIZE = 32f;
    private Vector2 position = new Vector2();
    private TextureRegion sprite;
    public Bin(Vector2 position, TextureRegion sprite){
        this.position = position;
        this.sprite = sprite;
    }
    public Vector2 getPosition(){
        return position;
    }
}
