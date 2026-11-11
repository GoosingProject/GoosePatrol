package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.ItemCode.Item;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements WorldContext {
    private List<Item> activeItems = new ArrayList<>();
    private final List<Item> toRemove = new ArrayList<>();
    private GooseSpawner gooseSpawner;

    public GameWorld(GooseSpawner gooseSpawner) {
        this.gooseSpawner = gooseSpawner;
    }

    /* call after energy bar is created in main */
    private EnergyBar energyBar;
        public void setEnergyBar(EnergyBar energyBar) {
        this.energyBar = energyBar;
    }
    @Override
    public void scareGeese(Vector2 centre, float radius) {
        // TODO - have geese run away from item
    }

    @Override
    public void attractGeese(Vector2 centre, float radius) {
        for (Goose goose : gooseSpawner.getGeese()) {
            goose.attract(centre, radius);
        }
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
    @Override
    public void addPlayerEnergy(float amount){
        energyBar.setEnergyBar(energyBar.getPlayerEnergy()+amount);
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
    /** remove all deployed items. */
    public void clear(){
        activeItems.clear();
        toRemove.clear();
    }
}
