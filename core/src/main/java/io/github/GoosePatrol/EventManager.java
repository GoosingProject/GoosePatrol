package io.github.GoosePatrol;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class EventManager {
    private float eventChance = 0.002f;

    public void update(float deltaTime, List<Goose> geese, Vector2 playerPosition) {
        if (Math.random() < eventChance * deltaTime) {
            int event = (int) Math.floor(Math.random() * 2);
            switch (event) {
                case 0:
                    longBoiEvent(geese, playerPosition);
                    break;
                case 1:
                    // TODO swarm
                    break;
            }
        }
    }

    /**
     * Positive 'long boi' event to scare all geese
     * @param geese List of all geese
     * @param playerPosition Player's position
     */
    private void longBoiEvent(List<Goose> geese, Vector2 playerPosition) {
        System.out.println("long boi");
        for (Goose goose : geese) {
            goose.scare(playerPosition, Float.MAX_VALUE);
        }
    }
}
