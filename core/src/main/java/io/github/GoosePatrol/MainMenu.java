package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.Screen;

/**
 * Main menu class to create, render and control the menu.
 */
public class MainMenu implements Screen {
    private Skin skin;
    private Stage stage;
    private Window settingsWindow;
    private Label masterVolumeSliderLabel;
    private Slider masterVolumeSlider;
    private Audio audio;

    /**
     * Initialises the play state to false.
     */
    public boolean playGameTriggered = false;

    public MainMenu(Audio aud) {
        this.audio = aud;
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());

        // creates a table root to allow button placement to be more gridlike, easier centering etc.
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Start of layout
        TextButton settingsButton = new TextButton("Settings", skin);
        root.add(settingsButton).space(10);

        settingsButton.addListener(
            new ChangeListener() { // use changelistener to detect button presses to show settings
                // window, as well as trigger play and exit functions
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    settingsWindow.setVisible(true);
                }
            });

        TextButton playButton = new TextButton("Play", skin);
        root.add(playButton).space(10);
        playButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    playGameTriggered = true;
                }
            });

        TextButton exitButton = new TextButton("Exit", skin);
        root.add(exitButton).space(10);
        exitButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.exit();
                }
            });

        // implemented settings using a scene2D window

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        stage.addActor(settingsTable);

        settingsWindow = new Window("Settings", skin);
        settingsWindow.setPosition(stage.getWidth() / 2, settingsWindow.getHeight() / 2);
        settingsWindow.setMovable(false);
        settingsWindow.setModal(false);
        settingsWindow.setVisible(false);
        settingsTable.add(settingsWindow).width(600).height(450).center();

        // add a button to close settings menu in the titletable of the window
        TextButton settingsCloseButton = new TextButton("X", skin);
        settingsCloseButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    settingsWindow.setVisible(false);
                }
            });
        settingsWindow.getTitleTable().add(settingsCloseButton).size(30, 15).padRight(5);

        // create volume sliders
        Label masterVolumeSliderLabel = new Label("Master Volume", skin);
        Slider masterVolumeSlider = new Slider(0, 1, 0.05f, false, skin);
        masterVolumeSlider.setValue(1f);

        Label musicSliderLabel = new Label("Music Volume", skin);
        Slider musicVolumeSlider = new Slider(0, 1, 0.05f, false, skin);
        musicVolumeSlider.setValue(1f);

        Label sfxSliderLabel = new Label("SFX Volume", skin);
        Slider sfxVolumeSlider = new Slider(0, 1, 0.05f, false, skin);
        sfxVolumeSlider.setValue(1f);

        settingsWindow.add(masterVolumeSliderLabel).padBottom(15);
        settingsWindow.row();
        settingsWindow.add(masterVolumeSlider).width(150);

        settingsWindow.row();

        settingsWindow.add(sfxSliderLabel).padBottom(5);
        settingsWindow.row();
        settingsWindow.add(sfxVolumeSlider).width(150);

        settingsWindow.row();

        settingsWindow.add(musicSliderLabel).padBottom(5);
        settingsWindow.row();
        settingsWindow.add(musicVolumeSlider).width(150);

        masterVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audio.setMasterVolume(masterVolumeSlider.getValue());
            }
        });

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audio.setMusicVolume(musicVolumeSlider.getValue());
            }
        });

        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audio.setSfxVolume(sfxVolumeSlider.getValue());
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    /**
     * Getter function to return the current stage the game is on
     *
     * @return The stage the game is on
     */
    public Stage getStage() {
        return this.stage;
    }
}
