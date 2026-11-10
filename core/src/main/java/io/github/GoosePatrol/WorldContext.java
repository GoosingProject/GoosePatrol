package io.github.GoosePatrol;

import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.ItemCode.Item;

public interface WorldContext {
    /*scare geese in radius, scare device */
    void scareGeese(Vector2 Centre, float radius);

    /*attract geese in radius, food item */
    void attractGeese(Vector2 Centre, float radius);

    void releaseAttract(Vector2 Centre,float radius);

    void addActiveItem(Item item);

    void removeActiveitem(Item item);
    /* increase the energy by specified amount */
    void addPlayerEnergy(float amount);
}
