package io.github.GoosePatrol;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
public class InventoryHud extends ApplicationAdapter{
    private static float WIDTH = 1200f;
    private static float HEIGHT = 700f;
    private Inventory inventory;
    private Label[] slotLabels = new Label[Inventory.SLOT_COUNT];
    private Skin skin;
    private Stage stage;
    private Texture backgroundTexture;
    public InventoryHud(Inventory inventory){
        this.inventory = inventory;
    }
    @Override 
    public void create(){
        skin = new Skin(Gdx.files.internal("metal-ui.json"));
        stage = new Stage(new FitViewport(WIDTH,HEIGHT));
        Pixmap pm = new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pm.setColor(new Color(0.15f,0.15f,0.15f,0.65f));
        pm.fill();
        backgroundTexture = new Texture(pm);
        pm.dispose();
        TextureRegionDrawable bg = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table slotTable = new Table();
        slotTable.setBackground(bg);
        slotTable.pad(10);
        for ( int i = 0; i< Inventory.SLOT_COUNT; i++){
            Label label = new Label("", skin);
            slotLabels[i] = label;
            slotTable.add(label).width(80).height(50).pad(2);
        }
        root.bottom().add(slotTable).padBottom(10);
        refresh();
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
