package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Ticket {
    public Texture texture;
    private Vector2 position = new Vector2();

    private boolean spawned = false;
    private  boolean collected = false;

    public Ticket(Texture texture){
        this.texture = texture;
        this.position = new Vector2(888f,424f);
    }

    public void spawn(){
        if (!collected){
            spawned = true;
        }
    }
    public void update(Vector2 playerPosition){
        if ( spawned && !collected){
            if (playerPosition.dst2(position)<20f*20f){
                collected = true;
                spawned = false;
            }
        }
    }
    /**
     * used for rendering uncollected ticket in the world
     * @param batch the sprite batch
     */
    public void renderWorld(SpriteBatch batch){
        if (spawned&&!collected){
            batch.draw(texture,position.x- texture.getWidth() / 2f, 
                position.y - texture.getHeight() / 2f);
        }
    }
    /**
     * used for rendering the collected ticket in a HUD element
     * @param batch
     */
    public void renderHud(SpriteBatch batch) {
        if (collected) {
            batch.draw(texture, 20f, 20f);
        }
    }

    public void reset(){
        spawned = false;
        collected = false;
    }

    public boolean isSpawned(){
        return spawned;
    }
    public boolean isCollected(){
        return collected;
    }
    public void dispose(){
        texture.dispose();
    }
}
