package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
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

        ProgressBar energyBar = new ProgressBar(0f, maxPlayerEnergy, 25, false, skin);
        energyBar.setSize(150, 20);
        energyBar.setValue(currentPlayerEnergy);
        root.add(energyBar).expand().top().left();
        
    }
    @Override
    public void render () {
        stage.act();
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
}

    
