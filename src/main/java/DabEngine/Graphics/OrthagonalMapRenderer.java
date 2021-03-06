package DabEngine.Graphics;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.Batch.Polygon;
import DabEngine.Graphics.Batch.PolygonBatch;
import DabEngine.Graphics.Batch.SpriteBatch;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Utils.Primitives.Primitives;

public class OrthagonalMapRenderer {

    private TileMap map;

    public OrthagonalMapRenderer(TileMap map) {
        this.map = map;
    }

    public void draw(Graphics g){
        for(MapLayer layer : map.layers){
            switch (layer.type){
                case "tilelayer":
                    SpriteBatch s = g.getBatch(SpriteBatch.class);
                    s.begin();
                        for(int y = 0; y < map.info.height; y++){
                            for(int x = 0; x < map.info.width; x++){
                                Texture t = map.getTile(layer, x, y);
                                if(t != null)
                                    s.draw(t, x * map.info.tileWidth, y * map.info.tileHeight, map.getFinalTileWidth(layer, x, y), map.getFinalTileHeight(layer, x, y), 0, 0, 0, 1, 1, 1, 1);
                            }
                        }
                    s.end();
                    break;
                
                case "objectgroup":
                    PolygonBatch poly = g.getBatch(PolygonBatch.class);
                    poly.begin();
                        for(MapObject o : layer.mOBjs){
                            if(o != null){
                                if(o.draw){
                                    if(o instanceof RectangleMapObject){
                                        RectangleMapObject r = (RectangleMapObject)o;
                                        poly.setPrimitiveType(GL11.GL_TRIANGLES);
                                        Polygon polygon = new Polygon(
                                            new int[] {
                                                0,1,2,
                                                2,3,0
                                            },
                                            new Vector2f[] {
                                                new Vector2f(0, 0),
                                                new Vector2f(0, 1),
                                                new Vector2f(1, 1),
                                                new Vector2f(1, 0)
                                            }
                                        );
                                        poly.draw(polygon, r.x, r.y, r.width, r.height, 0, 0, 0);
                                    }
                                    else if(o instanceof PointMapObject){
                                        PointMapObject p = (PointMapObject)o;
                                        poly.setPrimitiveType(GL11.GL_POINTS);
                                        poly.draw(Primitives.POINT.generate(), p.x, p.y, 1, 1, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    poly.end();
                    break;
            }
        }
    }
}