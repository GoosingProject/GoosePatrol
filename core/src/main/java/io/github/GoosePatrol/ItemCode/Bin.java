package io.github.GoosePatrol.ItemCode;

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
    private TextureRegion ring;
    /**
     * constructor for the bin class.
     * @param position  the vector position for the bin.
     * @param sprite    the sprite for the bin.
     * @param ring      the sprite for the bin aoe.
     */
    public Bin(Vector2 position, TextureRegion sprite, TextureRegion ring){
        this.position = position;
        this.sprite = sprite;
        this.ring = ring;
    }
    /**
     * @return the position vector of the bin.
     */
    public Vector2 getPosition(){
        return position;
    }
    /**
     * method to check if player is within effective range of bin.
     * @param pos   the player position.
     * @return  true if the player is within range.
     */
    public boolean contains(Vector2 pos) {
        return position.dst2(pos) <= RADIUS * RADIUS;
    }
    /**
     * render the bin and bin aoe in the world.
     * @param batch the SpriteBatch to draw it.
     */
    public void render(SpriteBatch batch){
        float diameter = RADIUS * 2f;
        batch.draw(ring, position.x - RADIUS, position.y - RADIUS, diameter, diameter);
        batch.draw(sprite, position.x - SIZE / 2f, position.y - SIZE / 2f, SIZE, SIZE);
    }
}
