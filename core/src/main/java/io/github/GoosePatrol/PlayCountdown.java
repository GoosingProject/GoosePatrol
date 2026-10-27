package io.github.GoosePatrol;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayCountdown {
    private TextureAtlas countdownAtlas = new TextureAtlas("countdown.atlas");
    private Animation<TextureRegion> countdownAnimation = new Animation<TextureRegion>(1f,countdownAtlas.findRegions("3"));
    private boolean visible;
    public PlayCountdown(){
        this.visible = false;
    }
    public void startAnimation(){
        visible = !visible;
    }

}
