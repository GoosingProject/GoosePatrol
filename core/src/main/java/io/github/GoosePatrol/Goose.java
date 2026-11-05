package io.github.GoosePatrol;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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
        for (GooseAction action : GooseAction.values()) {
            Map<SpriteDirection, Animation<TextureRegion>> directionMap = new HashMap<>();

            for (SpriteDirection direction : SpriteDirection.values()) {
                Animation<TextureRegion> animation =
            new Animation<TextureRegion>(
                0.25f,
                textureAtlas.findRegions("goose/" + action.label + "/" + direction.label),
                PlayMode.LOOP);

                directionMap.put(direction, animation);
            }

            animations.put(action, directionMap);
        }
    }

    /**
     * Calculate the direction the goose is facing
     * @return SpriteDirection of which direction the goose is facing
     */
    private SpriteDirection directionFacing() {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'directionFacing'");
    }

    @Override
    protected Animation<TextureRegion> getCurrentAnimation() {
        return animations.get(action).get(directionFacing());
    }

    @Override
    protected boolean shouldFlipX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shouldFlipX'");
    }
}
