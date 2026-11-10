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

    private TextureAtlas textureAtlas = new TextureAtlas("sprites.atlas");
    private GooseAction action = GooseAction.PATROL;
    private Map<GooseAction, Map<SpriteDirection, Animation<TextureRegion>>> animations = new HashMap<>();
    private Vector2 position;
    private Vector2 targetPosition;
    private NavigationTiledMapLayer navLayer;
    private AStarGridFinder<GridCell> finder = new AStarGridFinder<GridCell>(GridCell.class);
    private List<GridCell> patrolPath = new ArrayList<>();
    private int patrolPathIndex = 0;
    private float degreeOffset;

    // Width of world in tiles
    private int worldTileWidth;
    // Height of world in tiles
    private int worldTileHeight;

    private boolean fleeing = false;
    private Vector2 fleeSource;
    private  float fleeRadius;

    private boolean attracted = false;
    private Vector2 attractTarget = null;


    public Goose(float x, float y, NavigationTiledMapLayer navLayer, int worldTileWidth, int worldTileHeight) {
        super();
        position = new Vector2(x, y);
        targetPosition = position.cpy();
        this.navLayer = navLayer;
        this.worldTileWidth = worldTileWidth;
        this.worldTileHeight = worldTileHeight;
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
     * Update goose's state and position every frame
     * @param playerPosition Vector2 of player's position
     */
    public void update(Vector2 playerPosition, float deltaTime) {
        // stop fleeing once scarecrow is gone or escaped scarecrow radius
        if(fleeing&&(fleeSource==null||position.dst2(fleeSource)>fleeRadius*fleeRadius)){
            fleeing = false;
            fleeSource =null;
        }

        if(fleeing){
            action = GooseAction.FLEE;
        }
        else if (attracted){
            action = GooseAction.ATTRACRED;
        }
        else{
            if (action == GooseAction.FLEE || action == GooseAction.ATTRACRED)
            {
                // leave flee state > return to patrol state
                action = GooseAction.PATROL;
            }
            if (action == GooseAction.PATROL && playerPosition.cpy().sub(position).len() <= PLAYER_DETECT_RADIUS) {
                action = GooseAction.CHASE;
            }
        }
        if(action == GooseAction.FLEE){

        }else if(action == GooseAction.ATTRACRED){

        }
        else if (action == GooseAction.PATROL) {
            targetPosition = calculatePatrolTargetPosition();
        } else if (action == GooseAction.CHASE) {
            targetPosition = calculateChaseTargetPosition(playerPosition);
        }

        Vector2 direction = targetPosition.cpy().sub(position);
        float distance = direction.len();
        if (distance >= 0.5) {
            Vector2 velocity = direction.nor().scl(SPEED * deltaTime);
            position.add(velocity);
            getSprite().setPosition(position.x, position.y);
        }
    }
    /**
     * runs 5 tiles away from the scare source
     * @return the target vector
     */
    private Vector2 calculateFleeTargetPosition(){
        Vector2 currentTile = coordinateToTile(position);
        Vector2 fleeTile = coordinateToTile(fleeSource);
        Vector2 away = currentTile.cpy().sub(fleeTile);
        if(away.isZero()){
            away.set(1,0);
        }
        away.nor();
        Vector2 targetTile = currentTile.cpy().add(away.scl(5));
        targetTile.x = MathUtils.clamp(targetTile.x, 0, worldTileWidth  - 1);
        targetTile.y = MathUtils.clamp(targetTile.y, 0, worldTileHeight - 1);
        List<GridCell> path = finder.findPath((int) currentTile.x, (int) currentTile.y, (int) targetTile.x,(int)targetTile.y, navLayer);
        if(path == null || path.isEmpty()){
            return position.cpy();
        }
        return new Vector2(path.get(0).x*16,path.get(0).y*16);
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

        if (path == null || path.size() == 0) {
            return targetPosition.cpy();
        }

        return new Vector2(path.get(0).x * 16, path.get(0).y * 16);
    }

    /**
     * Calculate next target position for goose when in patrol state
     * @return Next target position
     */
    private Vector2 calculatePatrolTargetPosition() {
        Vector2 currentTile = coordinateToTile(position);

        if (patrolPathIndex >= patrolPath.size() - 1) {
            do {
                double randomAngle = Math.random() * 2 * Math.PI;
                Vector2 randomTarget = new Vector2(
                    Math.round(currentTile.x + 15 * Math.cos(randomAngle)),
                    Math.round(currentTile.y + 15 * Math.sin(randomAngle))
                );

                if (randomTarget.x < 0 || randomTarget.x >= worldTileWidth ||
                    randomTarget.y < 0 || randomTarget.y >= worldTileHeight)
                {
                    continue;
                }

                patrolPath = finder.findPath(
                    (int) currentTile.x,
                    (int) currentTile.y,
                    (int) randomTarget.x,
                    (int) randomTarget.y, navLayer);

            } while (patrolPath == null || patrolPath.size() == 0);

            patrolPathIndex = 0;
        }

        if (currentTile.x == patrolPath.get(patrolPathIndex).x && currentTile.y == patrolPath.get(patrolPathIndex).y) {
            patrolPathIndex += 1;
        }

        Vector2 target = new Vector2(
            patrolPath.get(patrolPathIndex).x * 16,
            patrolPath.get(patrolPathIndex).y * 16);

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
        return new Vector2(Math.round(coordinate.x / 16), Math.round(coordinate.y / 16));
    }
    /**
     * make the goose run away from the scare device
     * @param source the centre of the scare radius
     * @param radius the radius of effect
     */
    public void startFleeing(Vector2 source, float radius){
        if (position.dst2(source)>radius*radius){
            return;
        }
        fleeSource = source.cpy();
        fleeRadius = radius;
        fleeing = true;
        attracted = false; // scare state overrides flee
    }
    /**
     * makes the goose attracted to the food item
     * @param target the positioin of the food item
     * @param radius the radius of effect
     */
    public void startAttracting(Vector2 target,float radius){
        if(fleeing){
            return;
        }
        if(position.dst2(target)>radius*radius){
            return;
        }
        attractTarget = target.cpy();
        attracted = true;
    }
 /**
  * stop being attracted to target
  * @param target the target vector to stop being attaracted to
  */
    public void stopAttracting(Vector2 target){
        if(attractTarget != null && attractTarget.epsilonEquals(target,1f)){
            attracted = false;
            attractTarget = null;
        }
    }
}
