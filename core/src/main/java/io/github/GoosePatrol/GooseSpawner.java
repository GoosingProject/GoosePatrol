package io.github.GoosePatrol;

import java.util.ArrayList;
import java.util.List;

import org.xguzm.pathfinding.gdxbridge.NavigationTiledMapLayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GooseSpawner {
    private static float SPAWN_INTERVAL = 10f;

    private List<Goose> geese = new ArrayList<>();
    private float spawnTimer = 1f;
    private NavigationTiledMapLayer navLayer;
    private int worldTileWidth;
    private int worldTileHeight;

    public GooseSpawner(NavigationTiledMapLayer navLayer, int worldTileWidth, int worldTileHeight) {
        this.navLayer = navLayer;
        this.worldTileWidth = worldTileWidth;
        this.worldTileHeight = worldTileHeight;
    }

    public void update(float deltaTime, Vector2 playerPosition) {
        spawnTimer -= deltaTime;
        if (spawnTimer <= 0f) {
            spawnTimer = SPAWN_INTERVAL;
            spawnNewGoose();
        }

        for (Goose goose : geese) {
            goose.update(playerPosition, deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        for (Goose goose : geese) {
            goose.draw(batch);
        }
    }

    private void spawnNewGoose() {
        int x;
        int y;

        do {
            x = (int) Math.floor(Math.random() * worldTileWidth);
            y = (int) Math.floor(Math.random() * worldTileHeight);
        } while (!navLayer.getCell(x, y).isWalkable());

        geese.add(new Goose(x, y, navLayer, worldTileWidth, worldTileHeight));
    }
}
