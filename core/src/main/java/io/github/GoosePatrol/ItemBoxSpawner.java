package io.github.GoosePatrol;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import java.util.ArrayList;
public class ItemBoxSpawner {
    private TextureRegion texture;
    private float mapWidth;
    private float mapHeight;
    private float spawnTimer = 1f;
    private static float SPAWN_INTERVAL = 5f;
    private List<ItemBox> boxes = new ArrayList<>();

    public ItemBoxSpawner(TextureRegion texture, float width, float height){
        this.texture = texture;
        this.mapWidth = width;
        this.mapHeight = height;
    }
    /**
     * checks pickups and spawns every frame.
     * @return true if the inventory changed 
     */
    public boolean update(float deltaTime, Vector2 playerPos, Inventory inventory, ItemRoller roller){
        boolean changed = false;
        spawnTimer -= deltaTime;
        // checks for item box spawns
        if ( spawnTimer<=0f){
            spawnTimer = SPAWN_INTERVAL;
            // rolls random x and y co-ordinates
            float x = MathUtils.random(40f,mapWidth-40f);
            float y = MathUtils.random(40f,mapHeight-40f);
            boxes.add(new ItemBox(new Vector2(x,y), texture));
        }
        for (ItemBox box : boxes){
            box.update(deltaTime);
            if (box.canCollect(playerPos)){
                Item item = roller.rollItem();
                inventory.add(item);
                box.collect();
                changed = true;
            }
        }

        return changed;
    }
}
