package io.github.GoosePatrol.ItemCode;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class WalkableTiles {
    private WalkableTiles(){

    }
    public static List<Vector2> findWalkableTiles(TiledMap map) {
        List<Vector2> walkable = new ArrayList<>();
        TiledMapTileLayer collision = (TiledMapTileLayer) map.getLayers().get("Collision");
        int tileW = collision.getTileWidth();
        int tileH = collision.getTileHeight();
        int cols = collision.getWidth();
        int rows = collision.getHeight();
        for(int col = 0; col < cols; col++){
            for(int row = 0; row<rows;row++){
                TiledMapTileLayer.Cell cell = collision.getCell(col,row);
                boolean blocked = (cell !=null )&&(cell.getTile() !=null);
                if(!blocked){
                    float worldX = col*tileW+tileW /2f;
                    float worldY = (rows - 1 - row) * tileH + tileH / 2f;
                    walkable.add(new Vector2(worldX,worldY));
                }
            }
        }
        return walkable;
    }


}
