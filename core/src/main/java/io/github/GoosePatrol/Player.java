package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class Player {
    private Texture playerTexture;

    public Player() {
        // manually sets texture to character sprite
        playerTexture = new Texture("libgdx.png");
    }

    public void drawSprite(SpriteBatch playerSprite) {
        // draws the sprite itself
        playerSprite.draw(playerTexture, 0, 0);
    }
}
