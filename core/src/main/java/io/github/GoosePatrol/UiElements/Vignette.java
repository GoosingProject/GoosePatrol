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
    private static float LOW_ENERGY_THRESHOLD = 40f;
    private static float MAX_ALPHA = 0.85f;
    private static int TEXTURE_SIZE = 256;
    private Texture texture;
    private TextureRegion region;
        public Vignette() {
        this.texture = createVignetteTexture();
        this.region = new TextureRegion(texture);
    }

    /**
     * Builds a square texture whose centre is transparent and whose edges are
     * opaque black, with a smooth falloff between them.
     */
    private Texture createVignetteTexture() {
        Pixmap pm = new Pixmap(TEXTURE_SIZE, TEXTURE_SIZE, Pixmap.Format.RGBA8888);
        pm.setBlending(Pixmap.Blending.None);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();

        float centre = TEXTURE_SIZE / 2f;
        float maxRadius = TEXTURE_SIZE / 2f;
        // Fraction of the radius that stays clear permanantly
        float innerFraction = 0.35f;

        for (int y = 0; y < TEXTURE_SIZE; y++) {
            for (int x = 0; x < TEXTURE_SIZE; x++) {
                float dx = (x - centre) / maxRadius;
                float dy = (y - centre) / maxRadius;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                float alpha = (dist - innerFraction) / (1f - innerFraction);
                alpha = Math.max(0f, Math.min(1f, alpha));
                // Quadratic falloff — keeps the centre clear and ramps up near the edges.
                alpha = alpha * alpha;

                pm.setColor(0f, 0f, 0f, alpha);
                pm.drawPixel(x, y);
            }
        }

        Texture t = new Texture(pm);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pm.dispose();
        return t;
    }
    /**
     * Draws the vignette streched across the current viewport.
     * @param batch the SpriteBatch to draw with.
     * @param energy the players current energy
     */
    public void render(SpriteBatch batch,float energy){
        if(energy >= LOW_ENERGY_THRESHOLD){
            return;
        }
        // increases towards 0 energy
        float intensity = 1f - (energy/LOW_ENERGY_THRESHOLD);
        intensity = Math.max(0f, Math.min(1f,intensity));

        batch.setColor(1f,1f,1f,intensity*MAX_ALPHA);
        batch.draw(region, 0f, 0f,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.setColor(1f,1f,1f,1f);
    }
    public void dispose(){
        texture.dispose();
    }
}
