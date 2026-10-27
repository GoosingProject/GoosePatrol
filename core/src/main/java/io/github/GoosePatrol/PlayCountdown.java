package io.github.GoosePatrol;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayCountdown {
    private Animation<TextureRegion> countdownAnimation;
    private boolean visible;
    public PlayCountdown(){
        this.visible = false;
    }
    public void startAnimation(){
        visible = !visible;
    }

}
