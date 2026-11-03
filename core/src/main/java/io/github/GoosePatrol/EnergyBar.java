package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class EnergyBar extends ApplicationAdapter {
        private Skin skin;
        private Stage stage;
        private ProgressBar energyBar;
        private float maxPlayerEnergy = 100f;
        private float currentPlayerEnergy = 100f;

    @Override 
    public void create() {
        skin = new Skin(Gdx.files.internal("metal-ui.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //creates a table root to allow button placement to be more gridlike, easier centering etc.

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        //Start of layout

        energyBar = new ProgressBar(0f, maxPlayerEnergy, 25, false, skin);
        energyBar.setValue(currentPlayerEnergy);
        energyBar.setAnimateDuration(0.25f);
        root.add(energyBar).expand().top().left().width(150).height(20);

        
    }
    @Override
    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
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

    public float getPlayerEnergy() {
        return currentPlayerEnergy;
    }

    public void setEnergyBar(float energy) {
        this.currentPlayerEnergy = MathUtils.clamp(energy, 0f, maxPlayerEnergy); // keeps energy value clamped from 0 to 100
        energyBar.setValue(currentPlayerEnergy);
    }
}

    
