package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * draws a black vignette over the screen, intensity scale with player energy
 * Vignette
 */
public class Vignette {
    private static float LOW_ENERGY_THRESHOLD = 20f;
    private static float MAX_ALPHA = 0.85f;
    private static int TEXTURE_SIZE = 256;
    private Texture texture;
    private TextureRegion region;
    
}
