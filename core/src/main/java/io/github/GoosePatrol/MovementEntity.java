package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Abstract class to handle rendering entities with movement animations
 */
public abstract class MovementEntity {
    private Sprite sprite;
    private float stateTime = 0f;

    /**
     * Initialise the sprite, should be called when entity is created
     */
    protected void initialiseSprite() {
        sprite = new Sprite(getCurrentAnimation().getKeyFrame(stateTime));
    }

    /**
     * Update the sprite and render to the screen
     * @param batch SpriteBatch used to draw sprite to the screen
     */
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        Animation<TextureRegion> animation = getCurrentAnimation();
        TextureRegion textureRegion = animation.getKeyFrame(stateTime, true);

        if (textureRegion.isFlipX() != shouldFlipX()) {
            textureRegion.flip(true, false);
        }

        sprite.setRegion(textureRegion);
        sprite.draw(batch);
    }

    protected Sprite getSprite() {
        return sprite;
    }

    /**
     * Return the current animation for the entity
     *
     * @return Current animation for entity
     */
    protected abstract Animation<TextureRegion> getCurrentAnimation();

    /**
     * Return whether the sprite should flip to make it face left
     *
     * @return True if sprite should face left, false otherwise
     */
    protected abstract boolean shouldFlipX();
}
