package io.github.GoosePatrol;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Class representing a single goose
 */
public class Goose extends MovementEntity {
    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");
    private GooseAction action = GooseAction.PATROL;
    private Map<GooseAction, Map<SpriteDirection, Animation<TextureRegion>>> animations = new HashMap<>();
    private Vector2 position;
    private Vector2 targetPosition;
    private float degreeOffset;

    public Goose(float x, float y) {
        super();
        position = new Vector2(x, y);
        targetPosition = position.cpy();
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
        degreeOffset =
        (float) Math.atan2(targetPosition.y - position.y, targetPosition.x - position.x)
            * MathUtils.radiansToDegrees;

    // Checks the degree offset to see if the mouse on the left or the right of the player
    if (degreeOffset >= -22.5f && degreeOffset <= 22.5f
        || degreeOffset >= 157.5f && degreeOffset <= 180f
        || degreeOffset >= -180f && degreeOffset <= -157.5f) {
      return SpriteDirection.SIDE;
    }
    // Checks the degree offset to see if the mouse on the back incline of the player
    else if (degreeOffset >= 22.5f && degreeOffset <= 67.5f
        || degreeOffset >= 112.5f && degreeOffset <= 157.5f) {
      return SpriteDirection.BACKWARD_INCLINE;
    }
    // Checks the degree offset to see if the mouse on the front incline of the player
    else if (degreeOffset >= -157.5f && degreeOffset <= -112.5f
        || degreeOffset >= -67.5 && degreeOffset <= -22.5) {
      return SpriteDirection.FORWARD_INCLINE;
    }
    // Checks the degree offset to see if the mouse is above the player
    else if (degreeOffset >= 67.5f && degreeOffset <= 112.5f) {
      return SpriteDirection.BACKWARD;
    }
    // Defaultly assumes the mouse is below the player if all other selection returns false
    else {
      return SpriteDirection.FORWARD;
    }
    }

    @Override
    protected Animation<TextureRegion> getCurrentAnimation() {
        return animations.get(action).get(directionFacing());
    }

    @Override
    protected boolean shouldFlipX() {
        return !(degreeOffset >= -90 && degreeOffset <= 90);
    }
}
