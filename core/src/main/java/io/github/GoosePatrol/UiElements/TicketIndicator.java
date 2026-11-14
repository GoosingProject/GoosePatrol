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
}
