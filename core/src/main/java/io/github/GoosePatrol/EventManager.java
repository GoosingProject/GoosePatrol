package io.github.GoosePatrol;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class EventManager {
    private float eventChance = 0.002f;

    public void update(float deltaTime, List<Goose> geese, Vector2 playerPosition) {
        if (Math.random() < eventChance * deltaTime) {
            // TODO trigger event
        }
    }
}
