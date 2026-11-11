package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bus {
    private Vector2 position = new Vector2();
    private static float WIDTH = 50f;
    private static float HEIGHT = 20f;
    private Texture busTexture;
    public Bus(Vector2 position, Texture busTexture){
        this.position = position;
        this.busTexture = busTexture;
    }

}
