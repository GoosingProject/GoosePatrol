package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Screen;


/**
 * Manages the Scene2D layout for the ingame energy bar HUD for the player.
 */
public class EnergyBar implements Screen {
        private Skin skin;
        private Stage stage;
        private ProgressBar energyBar;
        private float maxPlayerEnergy = 100f;
        private float currentPlayerEnergy = 10f;

    /** constructer for the EnergyBar class */
    public EnergyBar() {
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        stage = new Stage(new ScreenViewport());

        

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        //Start of layout

        energyBar = new ProgressBar(0f, maxPlayerEnergy, 10, false, skin);
        energyBar.setValue(currentPlayerEnergy);
        energyBar.setAnimateDuration(0.25f);
        root.add(energyBar).expand().top().left().width(150).height(20);

        
    }
    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose () {
        skin.dispose();
        stage.dispose();
    }

       @Override 
    public void show() {

    }

    @Override 
    public void pause() {

    }

    @Override 
    public void resume() {

    }

    @Override 
    public void hide() {

    }

    /**
     * Getter that returns the player's current energy level
     * @return The player's energy level as a float between 0 - 100
     */
    public float getPlayerEnergy() {
        return currentPlayerEnergy;
    }


    /**
     * Updates player's energy and updates the energy bar accordingly
     * Caps the value between 0-100
     * @param energy The new energy value
     */
    public void setEnergyBar(float energy) {
        this.currentPlayerEnergy = MathUtils.clamp(energy, 0f, maxPlayerEnergy); // keeps energy value clamped from 0 to 100
        energyBar.setValue(currentPlayerEnergy);
    }
}

    
