package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** a box that grants random item when player touches it */
public class ItemBox {
    private static float LIFETIME = 60f;


    private Vector2 position = new Vector2();
    private TextureRegion texture;
    private boolean collected = false;
    private float lifeRemaining = LIFETIME;
    private boolean expired = false;
    private static float WORLD_SCALE = 16f;

    private static float PICKUP_RADIUS = 20f;
    public ItemBox(Vector2 position, TextureRegion texture){
        this.position.set(position);
        this.texture = texture;
    }
    public void update(float deltaTime){
        if (!expired){
            lifeRemaining -= deltaTime;
            if (lifeRemaining <=0f ){
                expired = true;
            }
        }
    }
    public boolean canCollect(Vector2 playerPos){
        return !collected && position.dst(playerPos) <= PICKUP_RADIUS;
    }
    public void collect(){
        collected = true;
    }
    public boolean isExpired(){
        return expired||collected;
    }
    public Vector2 getPositon(){
        return position;
    }
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - WORLD_SCALE/2f, position.y - WORLD_SCALE/2f,WORLD_SCALE,WORLD_SCALE);
    }
}
