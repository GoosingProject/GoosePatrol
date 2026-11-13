package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bus {
    private Vector2 position = new Vector2();
    private Vector2 offscreenRight = new Vector2();
    private Vector2 waitPosition = new Vector2();
    private Vector2 spawnPosition = new Vector2();
    private static float WIDTH = 100f;
    private static float HEIGHT = 40f;
    private Texture busTexture;
    // how far the bus has to travel to be off screen
    private static float OFFSCREEN_OFFSET = 250f;
    private boolean boarded = false;

    public enum BusState {INACTIVE,ARRIVING,WAITING,DEPARTING,FINISHED}
    private BusState state = BusState.INACTIVE;
    private boolean win = false;
    /**
     * constructor fo the Bus class
     * @param spawnPosition the position the bus will spawn
     * @param waitPosition the position the bus will wait for the player
     * @param busTexture the bus texture
     * @param mapWidth the width of the map
     */
    public Bus(Vector2 spawnPosition, Vector2 waitPosition, Texture busTexture,float mapWidth){
        this.spawnPosition = spawnPosition;
        this.waitPosition = waitPosition;
        this.position = spawnPosition;
        this.busTexture = busTexture;
        this.offscreenRight = new Vector2(mapWidth + OFFSCREEN_OFFSET, waitPosition.y);
    }
    /** call once ticket has been collected */
    public void spawn(){
        if (state!=BusState.INACTIVE){
            return;
        }
        position.set(new Vector2(OFFSCREEN_OFFSET,waitPosition.y));
        state = BusState.ARRIVING;
    }
    /**
     * call every frame
     * @param playerPosition the player position vector
     * @param deltaTime time since last frame
     */
    public void update(Vector2 playerPosition, float deltaTime){
        float step = 300f *deltaTime;
        if (state == BusState.ARRIVING){
            // bus has reached wait position
            if(moveToward(waitPosition, step)){
                state = BusState.WAITING;
            }
        }else if(state == BusState.WAITING){
            if (collisionCheck(playerPosition)){
                boarded = true;
                state = BusState.DEPARTING;
            }

        }else if(state == BusState.DEPARTING){
            if (moveToward(offscreenRight, step)){
                state = BusState.FINISHED;
                win = true;
            }
        }
    }
    /**
     * move the bus towards a target
     * @param target the target location
     * @param step the maximum number of units per second
     * @return true if bus has reached target
     */
    private boolean moveToward(Vector2 target,float step){
        Vector2 diff = target.cpy().sub(position);
        float dist = diff.len();
        if ( dist <= step){
            position.set(target);
            return true;
        }
        position.add(diff.nor().scl(step));
        return false;
    }
    /**
     * check if player is colliding with bus
     * @param pos player position vector
     * @return true if player is touching bus
     */
    public boolean collisionCheck(Vector2 pos){
        return Math.abs(pos.x - position.x) <= WIDTH /2f && Math.abs(pos.y-position.y) <= HEIGHT + 5f;
    }
    /**
     * render bus in the world
     * @param batch the spriteBatch 
     */
    public void render(SpriteBatch batch){
        if (state == BusState.INACTIVE){
            return;
        }
        batch.draw(busTexture,position.x-WIDTH/2f,position.y-HEIGHT/2f,WIDTH,HEIGHT);
    }
    // getters
    public BusState getState(){
        return state;
    }
    public boolean isPlayerBoarded(){
        return boarded;
    }
    public boolean isWin(){
        return win;
    }
    public boolean isActive(){
        return state != BusState.INACTIVE && state != BusState.FINISHED;
    }
    public Vector2 getPosition(){
        return position; 
    }
    /**
     * reset position and state
     */
    public void reset(){
        state = BusState.INACTIVE;
        boarded = false;
        win = false;
        position = spawnPosition;
    }
    /** dispose bus texture */
    public void dispose(){
        busTexture.dispose();
    }
}
