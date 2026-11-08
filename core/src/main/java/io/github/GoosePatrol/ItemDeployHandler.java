package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
/**
 * reads numberKey presses and deploys item to mouse position
 * ItemDeployHandler
 */

import com.badlogic.gdx.graphics.OrthographicCamera;

public class ItemDeployHandler {
    private OrthographicCamera camera;
    private Inventory inventory;
    private GameWorld world;
    private InventoryHud hud;
    private Player player;
    public ItemDeployHandler(OrthographicCamera camera, Inventory inventory, GameWorld world, InventoryHud hud, Player player){
        this.camera = camera;
        this.inventory = inventory;
        this.world = world;
        this.hud = hud;
        this.player = player;
    }
}
