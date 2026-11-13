package io.github.GoosePatrol;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EventManager {
    private final float POPUP_TIME = 2f;

    private float eventChance = 0.002f;
    private Skin skin;
    private Stage stage;
    private Label label;
    private float popupTimer = 0;

    public EventManager() {
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        label = new Label("",skin);
        label.setFontScale(2f);
        root.top().pad(10).add(label);
    }

    public void update(float deltaTime, List<Goose> geese, Vector2 playerPosition) {
        if (Math.random() < eventChance * deltaTime) {
            int event = (int) Math.floor(Math.random() * 2);
            switch (event) {
                case 0:
                    longBoiEvent(geese, playerPosition);
                    break;
                case 1:
                    swarmEvent(geese);
                    break;
            }
        }

        if (popupTimer > 0) {
            popupTimer -= deltaTime;
            if (popupTimer <= 0) {
                label.setText("");
            }
        }
    }

    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Positive 'long boi' event to scare all geese
     * @param geese List of all geese
     * @param playerPosition Player's position
     */
    private void longBoiEvent(List<Goose> geese, Vector2 playerPosition) {
        label.setText("Hidden event!\n   Long boi");
        popupTimer = POPUP_TIME;
        for (Goose goose : geese) {
            goose.scare(playerPosition, Float.MAX_VALUE);
        }
    }

    /**
     * Negative swarm event to make all geese chase player
     * @param geese List of all geese
     */
    private void swarmEvent(List<Goose> geese) {
        label.setText("Hidden event!\n   Swarm");
        popupTimer = POPUP_TIME;
        for (Goose goose : geese) {
            goose.swarm();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
