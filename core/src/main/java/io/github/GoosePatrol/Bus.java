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


}
