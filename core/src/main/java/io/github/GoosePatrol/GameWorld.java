package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.ItemCode.Item;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements WorldContext {
    private List<Item> activeItems = new ArrayList<>();
    private final List<Item> toRemove = new ArrayList<>();

    @Override
    public void scareGeese(Vector2 centre, float radius) {
        // TODO - have geese run away from item
    }

    @Override
    public void attractGeese(Vector2 centre, float radius) {
        // TODO - have geese pathfind towards deployed postion
    }

    @Override
    public void addActiveItem(Item item) {
        activeItems.add(item);
    }

    @Override
    public void removeActiveitem(Item item) {
        if (!toRemove.contains(item)) {
            toRemove.add(item);
        }
    }

    public void update(float deltaTime) {
        for (Item item : activeItems) {
            item.update(deltaTime, this);
        }
        for (Item item : activeItems) {
            if (!item.isActive()) {
                toRemove.add(item);
            }
        }
        if (!toRemove.isEmpty()) {
            activeItems.removeAll(toRemove);
            toRemove.clear();
        }
    }

    public void render(SpriteBatch batch) {
        for (Item item : activeItems) {
            item.render(batch);
        }
    }
    public void clear(){
        activeItems.clear();
        toRemove.clear();
    }
}
