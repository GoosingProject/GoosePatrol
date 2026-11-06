package io.github.GoosePatrol;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ItemBoxSpawner {
    private TextureRegion texture;
    private float mapWidth;
    private float mapHeight;
    public ItemBoxSpawner(TextureRegion texture, float width, float height){
        this.texture = texture;
        this.mapWidth = width;
        this.mapHeight = height;
    }
}
