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
    private Vector3 usePos = new Vector3();
    public ItemDeployHandler(OrthographicCamera camera, Inventory inventory, GameWorld world, InventoryHud hud, Player player){
        this.camera = camera;
        this.inventory = inventory;
        this.world = world;
        this.hud = hud;
        this.player = player;
    }
    public void update(){
        int slot = slotFromInput(); 
        if (slot < 0){
            return;
        }
        Item item = inventory.remove(slot);
        if (item == null){
            return;
        }
        usePos.set(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(usePos);
        item.use(player, new Vector2(usePos.x, usePos.y), world);
        hud.refresh();
    }
    private  int slotFromInput(){
        if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) return 0;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) return 1;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) return 2;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_4)) return 3;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_5)) return 4;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_6)) return 5;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_7)) return 6;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_8)) return 7;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_9)) return 8;
        if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) return 9;
        return -1;
    }
}
