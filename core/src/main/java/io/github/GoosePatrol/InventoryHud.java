package io.github.GoosePatrol;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
        for ( int i = 0; i< Inventory.SLOT_COUNT; i++){
            Label label = new Label("", skin);
            slotLabels[i] = label;
            slotTable.add(label).width(80).height(50).pad(2);
        }
        root.bottom().add(slotTable).padBottom(10);
    }
    public void refresh(){
        for(int i = 0; i < Inventory.SLOT_COUNT;i++){
            Item item = inventory.get(i);
            int keyNum = (i + 1);
            if (item == null){
                slotLabels[i].setText(keyNum);
            } else {
                slotLabels[i].setText(keyNum+": "+item.getName());
            }
        }
    }
    @Override 
    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
