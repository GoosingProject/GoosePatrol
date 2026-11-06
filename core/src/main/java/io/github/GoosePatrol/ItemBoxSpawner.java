package io.github.GoosePatrol;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ItemBoxSpawner {
    private TextureRegion texture;
    private float mapWidth;
    private float mapHeight;
    private float spawnTimer = 1f;
    private static float SPAWN_INTERVAL = 5f;
    public ItemBoxSpawner(TextureRegion texture, float width, float height){
        this.texture = texture;
        this.mapWidth = width;
        this.mapHeight = height;
    }

    public boolean update(float deltaTime, Vector2 playerPos, Inventory inventory, ItemRoller roller){
        boolean changed = false;
        spawnTimer -= deltaTime;
        if ( spawnTimer<=0f){
            spawnTimer = SPAWN_INTERVAL;
            float x = MathUtils.random(40f,mapWidth-40f);
            float y = MathUtils.random(40f,mapHeight-40f);
        }
    }
}
