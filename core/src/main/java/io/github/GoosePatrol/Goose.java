package io.github.GoosePatrol;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Class representing a single goose
 */
public class Goose extends MovementEntity {
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");
    private GooseAction action = GooseAction.PATROL;
    private Map<GooseAction, Map<SpriteDirection, Animation<TextureRegion>>> animations = new HashMap<>();
    private Vector2 position;

    public Goose(float x, float y) {
        super();
        position = new Vector2(x, y);
        loadAnimations();
        initialiseSprite();
    }

    /** Load animations and populate animation map */
    private void loadAnimations() {
        // TODO
    }

    @Override
    protected Animation<TextureRegion> getCurrentAnimation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentAnimation'");
    }

    @Override
    protected boolean shouldFlipX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shouldFlipX'");
    }
}
