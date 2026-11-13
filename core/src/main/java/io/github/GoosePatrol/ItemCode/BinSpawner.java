package io.github.GoosePatrol.ItemCode;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * places bins on random spawnable tiles
 * BinSpawner
 */
public class BinSpawner {
    private static int BIN_COUNT = 4;
    private static float MIN_SPACING = 200f;
    private static int MAX_PICK_ATTEMPTS = 200;
    private List<Bin> bins = new ArrayList<>();
    private Random random = new Random();
    private Texture binTexture;
    private Texture aoeTexture;
    /**
     * constructer for the BinSpawner class.
     * @param walkableTiles a list of tiles not on the collision layer of the map.
     * @param binTexture the bin texture.
     */
    public BinSpawner(List<Vector2> walkableTiles,Texture binTexture){
        this.binTexture = binTexture;
        this.aoeTexture = createAoeTexture();
        pickSpots(walkableTiles, binTexture, aoeTexture);
    }
    /**
     * creates a circle texture to show the bins aoe.
     * @return the bin texture.
     */
    private static Texture createAoeTexture() {
        int size = 128;
        int thickness = 4;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();
        pm.setColor(new Color(0.95f, 0.90f, 0.35f, 0.45f));
        pm.fillCircle(size / 2, size / 2, size / 2 - 1);
        pm.setBlending(Pixmap.Blending.None);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fillCircle(size / 2, size / 2, size / 2 - 1 - thickness);
        pm.setBlending(Pixmap.Blending.SourceOver);
        Texture t = new Texture(pm);
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        pm.dispose();
        return t;
    }
    /**
     * picks out valid random spots from the list of walkable tiles
     * @param walkable list of walkable tiles
     * @param binTexture the texture of the bin
     * @param ringTexture the texture of the bins AOE
     */
    private void pickSpots(List<Vector2> walkable, Texture binTexture, Texture ringTexture){
        List<Vector2> chosen = new ArrayList<>();
        int attempts = 0;
        while (bins.size()<=BIN_COUNT && attempts < MAX_PICK_ATTEMPTS){
            Vector2 candidate = walkable.get(random.nextInt(walkable.size()));
            boolean valid = true;

            for (Vector2 c:chosen){
                if (c.dst2(candidate)<MIN_SPACING*MIN_SPACING){
                    valid =false;
                    break;
                }
            }
            if (valid){
                chosen.add(candidate);
                bins.add(new Bin(candidate,new TextureRegion(binTexture),new TextureRegion(ringTexture)));
            }
            attempts++;
        }
    }
    /** @return true if the position is inside any bin's discard circle. */
    public boolean isInAnyBin(Vector2 position) {
        for (Bin bin : bins) {
            if (bin.contains(position)) {
                return true;
            }
        }
        return false;
    }
    /**
     * pass render calls to all bins in the world
     * @param batch the SpriteBatch to render the bins
     */
    public void render(SpriteBatch batch) {
        for (Bin bin : bins) {
            bin.render(batch);
        }
    }

    public void dispose() {
        aoeTexture.dispose();
        binTexture.dispose();
    }
}
