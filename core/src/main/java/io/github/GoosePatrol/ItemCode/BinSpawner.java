package io.github.GoosePatrol.ItemCode;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.GoosePatrol.Bin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * places bins on random spawnable tiles
 * BinSpawner
 */
public class BinSpawner {
    private static int BIN_COUNT = 4;
    private static float MIN_SPACING = 200f;
    private List<Bin> bins = new ArrayList<>();
    private Random random = new Random();
    private Texture binTexture;
    private Texture aoeTexture;
    public BinSpawner(Texture binTexture){
        this.binTexture = binTexture;
    }
}
