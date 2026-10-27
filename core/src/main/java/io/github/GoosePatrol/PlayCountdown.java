package io.github.GoosePatrol;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

/**
 * creates an object that plays a countdown animation.
 * PlayCountdown
 */
public class PlayCountdown {
    private TextureAtlas countdownAtlas;
    private Animation<TextureRegion> countdownAnimation;
    private float playTime = 0f;
    private boolean visible;
    /**
     * Constructer for the PlayCountdown class
     */
    public PlayCountdown(){
        this.countdownAtlas =  new TextureAtlas("countdown.atlas");
        this.countdownAnimation = new Animation<TextureRegion>(1f,countdownAtlas.findRegions("countdown"),PlayMode.NORMAL);
        this.visible = false;
    }
    /**
     * re sets the animation to the beggining
     */
    public void startAnimation(){
        playTime = 0f;
        visible = true;
    }
    /**
     * updates the animation to the correct frame
     * @param deltaTime time since last frame
     */
    public void updateAnimation(float deltaTime){
        if (!visible){
            // return if any attempt to update animation when object is hidden
            return;
        }
        playTime += deltaTime;
        if (countdownAnimation.isAnimationFinished(playTime)){
            // hide if animation is finished
            visible = false;
        }
    }
    /**
     * ensure the animation is playing in the centre of the screen
     * @param batch the camera SpriteBatch
     * @param centerX the x co-ordinate of the centre of the screen
     * @param centerY the y co-ordinate of the centre of the screen
     * @param scale scale the animation by this factor
     */
    public void draw(SpriteBatch batch, float centerX, float centerY, float scale){
        if (!visible){
            // return if any attempt to update animation when object is hidden
            return;
        }
        TextureRegion frame = countdownAnimation.getKeyFrame(playTime);
        float width = frame.getRegionWidth()*scale;
        float height = frame.getRegionHeight()*scale;
        batch.draw(frame, centerX-width/2f, centerY-height/2f,width,height);
    }
}
