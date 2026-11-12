package io.github.GoosePatrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xguzm.pathfinding.gdxbridge.NavigationTiledMapLayer;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

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
    private static final float SPEED = 50;
    private static final float PLAYER_DETECT_RADIUS = 50;
    private static final float ITEM_EFFECT_TIME = 5f;

    public static final float ATTACK_DAMAGE = 10f;
    private static final float ATTACK_RADIUS = 32f;
    private static final float ATTACK_COOLDOWN = 1.5f;

    private float attackCooldownTimer = 0f;

    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");
    private GooseAction action = GooseAction.PATROL;
    private Map<GooseAction, Map<SpriteDirection, Animation<TextureRegion>>> animations = new HashMap<>();
    private Vector2 position;
    private Vector2 targetPosition;
    private NavigationTiledMapLayer navLayer;
    private AStarGridFinder<GridCell> finder = new AStarGridFinder<GridCell>(GridCell.class);
    private List<GridCell> path;
    private int pathIndex = 0;
    private float degreeOffset;
    private float itemEffectTimer = ITEM_EFFECT_TIME;

    // Width of world in tiles
    private int worldTileWidth;
    // Height of world in tiles
    private int worldTileHeight;

    public Goose(float x, float y, NavigationTiledMapLayer navLayer, int worldTileWidth, int worldTileHeight) {
        super();
        position = new Vector2(x, y);
        targetPosition = position.cpy();
        this.navLayer = navLayer;
        this.worldTileWidth = worldTileWidth;
        this.worldTileHeight = worldTileHeight;
        loadAnimations();
        initialiseSprite();
        getSprite().setPosition(x, y);
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
     * Update goose's state and position every frame
     * @param playerPosition Vector2 of player's position
     * @param deltaTime time since last frame
     * @return true if goose attacked this frame
     */
    public boolean update(Vector2 playerPosition, float deltaTime) {
        if (action == GooseAction.PATROL &&
            playerPosition.cpy().sub(position).len() <= PLAYER_DETECT_RADIUS)
        {
            action = GooseAction.CHASE;
        }

        if (action == GooseAction.ATTRACT || action == GooseAction.SCARE) {
            itemEffectTimer -= deltaTime;
            if (itemEffectTimer <= 0f) {
                itemEffectTimer = ITEM_EFFECT_TIME;
                action = GooseAction.PATROL;
            }
        }

        switch (action) {
            case PATROL:
                targetPosition = calculatePatrolTargetPosition();
                break;
            case CHASE:
                targetPosition = calculateChaseTargetPosition(playerPosition);
                break;
            case ATTRACT:
                targetPosition = calculatePathTargetPosition();
                break;
            case SCARE:
                targetPosition = calculatePathTargetPosition();
                break;
        }

        Vector2 direction = targetPosition.cpy().sub(position);
        float distance = direction.len();
        if (distance >= 0.5) {
            Vector2 velocity = direction.nor().scl(SPEED * deltaTime);
            position.add(velocity);
            getSprite().setPosition(position.x, position.y);
        }
        if (attackCooldownTimer > 0f){
            attackCooldownTimer -= deltaTime;
        }
        boolean attacked = false;

        if(action == GooseAction.CHASE && position.dst(playerPosition)<=ATTACK_RADIUS&&attackCooldownTimer<=0f){
            attackCooldownTimer = ATTACK_COOLDOWN;
            attacked = true;
        }

        return attacked;
    }

    /**
     * Set goose to scare state if wihtin radius
     */
    public void scare(Vector2 centre, float radius) {
        if (position.dst(centre) > radius) {
            return;
        }

        action = GooseAction.SCARE;
        Vector2 currentTile = coordinateToTile(position);
        Vector2 direction = position.cpy().sub(centre).nor();
        for (int distance = 15; distance > 0; distance--) {
            Vector2 target = position.cpy().add(direction.cpy().scl(distance * 16));
            Vector2 targetTile = coordinateToTile(target);
            GridCell cell = navLayer.getCell((int) targetTile.x, (int) targetTile.y);
            if (cell != null && cell.isWalkable()) {
                List<GridCell> newPath = finder.findPath(
                    (int) currentTile.x,
                    (int) currentTile.y,
                    (int) targetTile.x,
                    (int) targetTile.y, navLayer);

                if (newPath != null && newPath.size() > 0) {
                    path = new ArrayList<>(newPath);
                    pathIndex = 0;
                }

                return;
            }
        }
    }

    /**
     * Set goose to attract state if wihtin radius
     */
    public void attract(Vector2 centre, float radius) {
        if (position.dst(centre) > radius) {
            return;
        }

        action = GooseAction.ATTRACT;
        Vector2 currentTile = coordinateToTile(position);
        Vector2 targetTile = coordinateToTile(centre);

        List<GridCell> newPath = finder.findPath(
            (int) currentTile.x,
            (int) currentTile.y,
            (int) targetTile.x,
            (int) targetTile.y, navLayer);

        if (newPath != null && newPath.size() > 0) {
            path = new ArrayList<>(newPath);
            pathIndex = 0;
        }
    }

    /**
     * Calculate next target position for goose when following path
     * @return Next target position
     */
    private Vector2 calculatePathTargetPosition() {
        if (path == null) {
            return targetPosition;
        }

        if (pathIndex >= path.size()) {
            return targetPosition;
        }

        Vector2 currentTile = coordinateToTile(position);
        Vector2 currentTileTarget = new Vector2(
            path.get(pathIndex).x,
            path.get(pathIndex).y
        );

        if (currentTile.dst(currentTileTarget) < 2) {
            pathIndex += 1;
            if (pathIndex == path.size()) {
                return targetPosition;
            }
        }

        Vector2 target = new Vector2(
            path.get(pathIndex).x * 16 + 8,
            path.get(pathIndex).y * 16 + 8);

        return target;
    }

    /**
     * Calculate next target position for goose when in chase state
     * @return Next target position
     */
    private Vector2 calculateChaseTargetPosition(Vector2 playerPosition) {
        Vector2 currentTile = coordinateToTile(position);
        Vector2 playerTile = coordinateToTile(playerPosition);
        List<GridCell> path = finder.findPath(
            (int) currentTile.x,
            (int) currentTile.y,
            (int) playerTile.x,
            (int) playerTile.y, navLayer);

        if (path == null || path.size() <= 1) {
            return targetPosition.cpy();
        }

        return new Vector2(path.get(0).x * 16 + 8, path.get(0).y * 16 + 8);
    }

    /**
     * Calculate next target position for goose when in patrol state
     * @return Next target position
     */
    private Vector2 calculatePatrolTargetPosition() {
        Vector2 currentTile = coordinateToTile(position);

        if (path == null || pathIndex >= path.size()) {
            int attempts = 0;
            do {
                attempts += 1;
                if (attempts > 20) {
                    return targetPosition;
                }

                double randomAngle = MathUtils.random() * 2 * Math.PI;
                double randomDistance = MathUtils.random() * 10;
                Vector2 randomTarget = new Vector2(
                    Math.round(currentTile.x + randomDistance * Math.cos(randomAngle)),
                    Math.round(currentTile.y + randomDistance * Math.sin(randomAngle))
                );

                if (randomTarget.x < 0 || randomTarget.x >= worldTileWidth ||
                    randomTarget.y < 0 || randomTarget.y >= worldTileHeight)
                {
                    path = null;
                    continue;
                }

                List<GridCell> newPath = finder.findPath(
                    (int) currentTile.x,
                    (int) currentTile.y,
                    (int) randomTarget.x,
                    (int) randomTarget.y,
                    navLayer
                );

                if (newPath == null || newPath.size() == 0) {
                    path = null;
                    continue;
                }

                path = new ArrayList<>(newPath);

            } while (path == null || path.size() == 0);

            pathIndex = 0;
        }

        Vector2 currentTileTarget = new Vector2(
            path.get(pathIndex).x,
            path.get(pathIndex).y
        );

        if (currentTile.dst(currentTileTarget) < 2) {
            pathIndex += 1;
            if (pathIndex == path.size()) {
                return targetPosition;
            }
        }

        Vector2 target = new Vector2(
            path.get(pathIndex).x * 16 + 8,
            path.get(pathIndex).y * 16 + 8);

        return target;
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

    /**
     * Convert world coordinate to tile x, y position
     * @param coordinate World coordinate
     * @return Tile position
     */
    private Vector2 coordinateToTile(Vector2 coordinate) {
        return new Vector2((int) Math.floor(coordinate.x / 16), (int) Math.floor(coordinate.y / 16));
    }
}
