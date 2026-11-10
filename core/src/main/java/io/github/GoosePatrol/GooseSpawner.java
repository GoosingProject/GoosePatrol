package io.github.GoosePatrol;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GooseSpawner {
    private List<Goose> geese = new ArrayList<>();

    public GooseSpawner() {

    }

    public void update(float deltaTime, Vector2 playerPosition) {
        for (Goose goose : geese) {
            goose.update(playerPosition, deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        for (Goose goose : geese) {
            goose.draw(batch);
        }
    }
}
