package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import org.xguzm.pathfinding.gdxbridge.NavigationTiledMapLayer;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GooseSpawner {
    private static int GOOSE_COUNT = 5;
    private static float MIN_PLAYER_DIST = 150f;
    private static float MIN_SPACING = 150f;
    private static int   MAX_ATTEMPTS = 500;
    private List<Goose> geese = new ArrayList<>();
    private Random random = new Random();
    public GooseSpawner(TiledMap map, GameWorld world, Vector2 playerStart) {
        NavigationTiledMapLayer navLayer = buildNavLayer(map);

        MapProperties prop = map.getProperties();
        int worldTileWidth  = prop.get("width",  Integer.class);
        int worldTileHeight = prop.get("height", Integer.class);

        List<Vector2> spawnTiles = WalkableTiles.findWalkableTiles(map);

        int attempts = 0;
        while (geese.size() < GOOSE_COUNT && attempts < MAX_ATTEMPTS) {
            attempts++;
            Vector2 candidate = spawnTiles.get(random.nextInt(spawnTiles.size()));

            // Keep away from the player's spawn
            if (candidate.dst2(playerStart) < MIN_PLAYER_DIST * MIN_PLAYER_DIST) {
                continue;
            }

            // Keep away from other geese
            boolean tooClose = false;
            for (Goose g : geese) {
                if (g.getPosition().dst2(candidate) < MIN_SPACING * MIN_SPACING) {
                    tooClose = true;
                    break;
                }
            }
            if (tooClose)
            { 
                continue;
            }

            Goose goose = new Goose(candidate.x, candidate.y, navLayer,worldTileWidth, worldTileHeight);
            geese.add(goose);
            world.addGeese(goose);
        }
    }
    private static NavigationTiledMapLayer buildNavLayer(TiledMap map) {
        TiledMapTileLayer collision = (TiledMapTileLayer) map.getLayers().get("Collision");
        int cols = collision.getWidth();
        int rows = collision.getHeight();

        GridCell[][] cells = new GridCell[cols][rows];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                TiledMapTileLayer.Cell tiledCell = collision.getCell(x, y);
                boolean blocked = tiledCell != null && tiledCell.getTile() != null;

                GridCell cell = new GridCell(x, y);
                cell.setWalkable(!blocked);
                cells[x][y] = cell;
            }
        }

        return new NavigationTiledMapLayer(cells);
    }
}
