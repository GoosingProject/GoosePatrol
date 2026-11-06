package io.github.GoosePatrol;
import com.badlogic.gdx.Gdx;

import java.security.PublicKey;

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

public class InventoryHud extends ApplicationAdapter{
    private Inventory inventory;
    private Label[] slotLabels = new Label[Inventory.SLOT_COUNT];
    private Skin skin;
    private Stage stage;
    public InventoryHud(Inventory inventory){
        this.inventory = inventory;
    }
    @Override 
    public void create(){
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table slotTable = new Table();
        for ( int i = 0; i< inventory.SLOT_COUNT; i++){
            Label label = new Label("", skin);
            slotLabels[i] = label;
            slotTable.add(label).width(80).height(50).pad(2);
        }
        root.bottom().add(slotTable).padBottom(10);
    }
}
