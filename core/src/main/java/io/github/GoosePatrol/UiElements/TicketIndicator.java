package io.github.GoosePatrol.UiElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;


public class TicketIndicator {
    private static float LABEL_DURATION = 4f;
    private BitmapFont font;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private float labelTimer = 0f;
    public TicketIndicator(){
        font = new BitmapFont();
        font.getData().setScale(2f);
    }

    public void showSpawnLabel(){
        labelTimer = LABEL_DURATION;
    }

    public void update(float deltaTime){
        if (labelTimer>0f){
            labelTimer = Math.max(0f,labelTimer-deltaTime);
        }
    }

    public void render(SpriteBatch batch){
        if (labelTimer <= 0f){
            return; 
        }
        String text = "Ticket available";
        glyphLayout.setText(font, text);
        float x = (Gdx.graphics.getWidth() - glyphLayout.width)/2f;
        float y = Gdx.graphics.getHeight()-80f;
        // have the label fade out over the final second
        float alpha = MathUtils.clamp(labelTimer,0f,1f);

        font.setColor(0f,0f,0f,alpha);
        font.draw(batch,glyphLayout,x+2f,y-2f); // drop shadow
        font.setColor(1f,1f,0f,alpha);
        font.draw(batch,glyphLayout,x,y); // actual text
        font.setColor(Color.WHITE);
    }
}
