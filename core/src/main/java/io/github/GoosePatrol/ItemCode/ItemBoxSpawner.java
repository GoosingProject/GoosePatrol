package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.HelperClasses.WalkableTiles;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * class for spawning item boxes and handling updates to them
 * ItemBoxSpawner
 */
public class ItemBoxSpawner {
    private TextureRegion texture;
    private float mapWidth;
    private float mapHeight;
    private float spawnTimer = 1f;
    private static float SPAWN_INTERVAL = 5f;
    private List<ItemBox> boxes = new ArrayList<>();
    private TiledMap map;
    private List<Vector2> spawnPositions = new ArrayList<>();
    private Random random = new Random();
    private WalkableTiles walkable;

    /**
     * constructer for the ItemBoxSpawner class
     *
     * @param texture ItemBoxTexture
     * @param map     the Tiled map object
     * @param width   map width
     * @param height  map height
     */
    public ItemBoxSpawner(TextureRegion texture, TiledMap map, float width, float height) {
        this.texture = texture;
        this.map = map;
        this.mapWidth = width;
        this.mapHeight = height;
        this.spawnPositions = walkable.findWalkableTiles(map);
    }

    /**
     * checks pickups and spawns every frame.
     *
     * @return true if the inventory changed
     */
    public boolean update(float deltaTime, Vector2 playerPos, Inventory inventory, ItemRoller roller) {
        boolean changed = false;
        spawnTimer -= deltaTime;
        // checks for item box spawns
        if (spawnTimer <= 0f) {
            spawnTimer = SPAWN_INTERVAL;
            // rolls random x and y co-ordinates
            float x = MathUtils.random(40f, mapWidth - 40f);
            float y = MathUtils.random(40f, mapHeight - 40f);
            boxes.add(new ItemBox(pickSpawnPositon(), texture));
        }
        for (ItemBox box : boxes) {
            box.update(deltaTime);
            if (box.canCollect(playerPos)) {
                Item item = roller.rollItem();
                inventory.add(item);
                box.collect();
                changed = true;
            }

        }
        boxes.removeIf(ItemBox::isExpired);

        return changed;
    }

    /**
     * chose random viable position from list of valid positions
     * @return the vector position
     */
    private Vector2 pickSpawnPositon(){
        return new Vector2(spawnPositions.get(random.nextInt(spawnPositions.size())));
    }

    /**
     * pass render calls to all boxes in world
     */
    public void render(SpriteBatch batch) {
        for (ItemBox box : boxes) {
            box.render(batch);
        }
    }

    /**
     * clear all boxes from world
     */
    public void clear() {
        boxes.clear();
    }
    /** remove all boxes and restart spawn timer */
    public void reset(){
        boxes.clear();
        spawnTimer = SPAWN_INTERVAL;
    }

}
