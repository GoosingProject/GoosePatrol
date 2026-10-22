package io.github.GoosePatrol;
/**
 * holds a worldspace postion for the camera to look at
 */
public class CameraTarget {
    private float x;
    private float y;
    /**
     * @param x the starting x position
     * @param y the starting y position
     */
    public CameraTarget(float x, float y){
        this.x = x;
        this.y = y;
    }
    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }

}
