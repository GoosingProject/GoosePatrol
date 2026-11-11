package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PlayTimer {
    private float timeRemaining = 300f;
    private boolean gameEnd = false;
    private Skin skin;
    private Stage stage;
    private Label timeLabel;
    public void create(){
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        timeLabel = new Label("",skin);
        timeLabel.setFontScale(2f);
        root.top().right().add(timeLabel).pad(10);
        updateLabel();
    }
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
    private void updateLabel(){
        int totalSeconds = MathUtils.ceil(timeRemaining);
        int minutes = totalSeconds/60;
        int seconds = totalSeconds%60;
        timeLabel.setText(String.format("%02d:%02d", minutes,seconds));
    }
    public void reset(){
        timeRemaining = 300f;
        gameEnd = false;
        updateLabel();
    }
    public boolean isGameEnd(){
        return gameEnd;
    }
    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    public void resize(int width,int height){
        stage.getViewport().update(width,height,true);
    }
    public void dispose(){
        skin.dispose();
        stage.dispose();
    }
}
