package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * class describing the ticket objects.
 * Ticket
 */
public class Ticket {
    public Texture texture;
    private Vector2 position = new Vector2();

    private boolean spawned = false;
    private  boolean collected = false;

    private static float SIZE = 32f;
    /**
     * constructor for the ticket class.
     * @param texture the ticket sprite texture.
     */
    public Ticket(Texture texture){
        this.texture = texture;
        this.position = new Vector2(888f,424f);
    }
    /**
     * spawns the ticket in the world so its visible.
     */
    public void spawn(){
        if (!collected){
            spawned = true;
        }
    }
    /**
     * check for collision with player.
     * @param playerPosition the position vector of the player.
     */
    public void update(Vector2 playerPosition){
        if ( spawned && !collected){
            if (playerPosition.dst2(position)<20f*20f){
                collected = true;
                spawned = false;
            }
        }
    }
    /**
     * used for rendering uncollected ticket in the world.
     * @param batch the sprite batch.
     */
    public void renderWorld(SpriteBatch batch){
        if (spawned&&!collected){
            batch.draw(texture,position.x - SIZE, position.y -SIZE,SIZE,SIZE);
        }
    }
    /**
     * used for rendering the collected ticket in a HUD element.
     * @param batch
     */
    public void renderHud(SpriteBatch batch) {
        if (collected) {
            batch.draw(texture, 20f, 20f , SIZE*5, SIZE*5);
        }
    }
    /**
     * reset the parameters of the ticket.
     */
    public void reset(){
        spawned = false;
        collected = false;
    }
    /**
     * @return the value of the spawned boolean.
     */
    public boolean isSpawned(){
        return spawned;
    }
    /**
     * @return the value of the collected boolean.
     */
    public boolean isCollected(){
        return collected;
    }
    /**
     * dispose the ticket sprite
     */
    public void dispose(){
        texture.dispose();
    }
    /**
     * 
     * @return the position vector of the ticket
     */
    public Vector2 getPosition(){
        return position;
    }
}
