package DabEngine.Graphics;

import org.joml.Vector4f;

import DabEngine.Graphics.Batch.SpriteBatch;
import DabEngine.Graphics.Models.Texture;

/* Class Backround 
 * struct representing a background
 */
public class Background {
	
	public Texture texture;
	public float x, y, width, height;
	public Vector4f color = new Vector4f();
	public float z;
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, x, y, width, height, 0, 0, 0, color.x, color.y, color.z,color.w);
	}
}