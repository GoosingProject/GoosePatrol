package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bus {
    private Vector2 position = new Vector2();
    private Vector2 offscreenRight = new Vector2();
    private Vector2 offscreenLeft = new Vector2();
    private static float WIDTH = 50f;
    private static float HEIGHT = 20f;
    private Texture busTexture;
    // how far the bus has to travel to be off screen
    private static float OFFSCREEN_OFFSET = 250f;

    public enum BusState {INACTIVE,ARRIVING,WAITING,DEPARTING,FINISHED}
    private BusState state = BusState.INACTIVE;
    public Bus(Vector2 position, Texture busTexture){
        this.position = position;
        this.busTexture = busTexture;
        this.offscreenRight = new Vector2(position.x + OFFSCREEN_OFFSET, position.y);
        this.offscreenLeft = new Vector2(position.x - OFFSCREEN_OFFSET, position.y);
    }

    public void spawn(){
        if (state!=BusState.INACTIVE){
            return;
        }
        position.set(offscreenLeft);
        state = BusState.ARRIVING;
    }

    public void update(Vector2 playerPosition, float deltaTime){
        float step = 300f *deltaTime;
        if (state == BusState.ARRIVING){

        }else if(state == BusState.WAITING){

        }else if(state == BusState.DEPARTING){

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
}
