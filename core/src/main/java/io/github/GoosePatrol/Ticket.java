package io.github.GoosePatrol;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Ticket {
    public Texture texture;
    private Vector2 position = new Vector2();

    public Ticket(Texture texture){
        this.texture = texture;
        this.position = new Vector2(888f,424f);
    }
}
