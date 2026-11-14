package io.github.GoosePatrol.UiElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
/**
 * a 5 minite timer that counts down until the game ends;
 * PlayTimer
 */
public class PlayTimer {
    private static float TOTAL_TIME = 300f;
    private float timeRemaining = TOTAL_TIME;
    private boolean gameEnd = false;
    private Skin skin;
    private Stage stage;
    private Label timeLabel;
    /** create the countdown timer as a ui element */
    public void create(){
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new FitViewport(1200f,700f));
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        timeLabel = new Label("",skin);
        timeLabel.setFontScale(2f);
        root.top().right().add(timeLabel).pad(10);
        updateLabel();
    }
    /**
     * update the value of timeRemaining
     * @param deltaTime time since last frame
     */
    public void update(float deltaTime){
        if(gameEnd){
            return;
        }
        timeRemaining-= deltaTime;
        if(timeRemaining<=0f){
            timeRemaining = 0f;
            gameEnd = true;
        }
        updateLabel();
    }
    /**
     * update the value shown on the timer
     */
    private void updateLabel(){
        int totalSeconds = MathUtils.ceil(timeRemaining);
        int minutes = totalSeconds/60;
        int seconds = totalSeconds%60;
        timeLabel.setText(String.format("%02d:%02d", minutes,seconds));
    }
    /**
     * reset time remaining to 5 mins
     */
    public void reset(){
        timeRemaining = TOTAL_TIME;
        gameEnd = false;
        updateLabel();
    }
    /**
     * @return if the game has ended because time ran out
     */
    public boolean isGameEnd(){
        return gameEnd;
    }
    /**
     * draw the timer on the screen
     */
    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    /**
     * resize the viewport to specified scale
     * @param width
     * @param height
     */
    public void resize(int width,int height){
        stage.getViewport().update(width,height,true);
    }
    /**
     * dispose assets to avoid memory leaks.
     */
    public void dispose(){
        skin.dispose();
        stage.dispose();
    }
    /**
     * @param timeElapsed the time that should pass for this check to return true
     * @return true once the time that has passed is longer than timeElapsed
     */
    public boolean hasElapsed(float timeElapsed){
        return (TOTAL_TIME-timeRemaining)>=timeElapsed;
    }
}
