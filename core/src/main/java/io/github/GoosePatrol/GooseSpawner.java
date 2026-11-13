package io.github.GoosePatrol;

import java.util.ArrayList;
import java.util.List;

import org.xguzm.pathfinding.gdxbridge.NavigationTiledMapLayer;
import org.xguzm.pathfinding.grid.GridCell;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class GooseSpawner {
    private static float SPAWN_INTERVAL = 10f;

    private List<Goose> geese = new ArrayList<>();
    private float spawnTimer = 1f;
    private int worldTileWidth;
    private int worldTileHeight;
    private TiledMapTileLayer layer;

    public GooseSpawner(
        TiledMapTileLayer layer,
        int worldTileWidth,
        int worldTileHeight,
        int startingGeese)
    {
        this.worldTileWidth = worldTileWidth;
        this.worldTileHeight = worldTileHeight;
        this.layer = layer;

        for (int i = 0; i < startingGeese; i++) {
            spawnNewGoose();
        }
    }
    /**
     * pass update calls to all geese in world
     * @param deltaTime time since last frame
     * @param playerPosition position vector of the player
     * @return true if any goose attacked this frame
     */
    public boolean update(float deltaTime, Vector2 playerPosition) {
        spawnTimer -= deltaTime;
        if (spawnTimer <= 0f) {
            spawnTimer = SPAWN_INTERVAL;
            spawnNewGoose();
        }
        boolean attacked = false;
        for (Goose goose : geese) {
            if(goose.update(playerPosition, deltaTime)){
                attacked = true;
            }
        }
        return attacked;
    }

    public void render(SpriteBatch batch) {
        for (Goose goose : geese) {
            goose.draw(batch);
        }
    }

    private void spawnNewGoose() {
        int x;
        int y;

        NavigationTiledMapLayer navLayer = getNavLayer(layer);

        do {
            x = (int) Math.floor(Math.random() * worldTileWidth);
            y = (int) Math.floor(Math.random() * worldTileHeight);
        } while (!navLayer.getCell(x, y).isWalkable());

        geese.add(new Goose(x * 16, y * 16, navLayer, worldTileWidth, worldTileHeight));
    }

    private NavigationTiledMapLayer getNavLayer(TiledMapTileLayer layer) {
    int width = layer.getWidth();
    int height = layer.getHeight();
    GridCell[][] cells = new GridCell[width][height];

    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            cells[x][y] = new GridCell(x, y);
            Cell cell = layer.getCell(x, y);
            if (cell != null) {
                cells[x][y].setWalkable(false);
            }
        }
    }

    return new NavigationTiledMapLayer(cells);
  }

  public List<Goose> getGeese() {
    return geese;
  }
}
