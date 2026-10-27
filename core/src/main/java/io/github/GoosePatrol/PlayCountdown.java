package io.github.GoosePatrol;
import com.badlogic.gdx.graphics.g2d.Animation;
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
        this.countdownAnimation = new Animation<TextureRegion>(1f,countdownAtlas.findRegions("1"),PlayMode.NORMAL);
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
            return;
        }
        playTime += deltaTime;
        if (countdownAnimation.isAnimationFinished(playTime)){
            visible = false;
        }
    }

}
