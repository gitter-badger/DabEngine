package Entities;

import org.joml.Vector4f;

import Entities.Components.CInteract;
import Entities.Components.CSprite;
import Entities.Components.CTransform;
import Graphics.Models.Texture;

public class NPC extends Entity {
	
	@SuppressWarnings("unused")
	private String name;

	public NPC(String name, Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor) {
		this.name = name;
		
		addComponent(new CSprite());
		addComponent(new CInteract());
		addComponent(new CTransform());
		
		CSprite render = this.getComponent(CSprite.class);
		render.texture = texture;
		render.center_anchor = center_anchor;
		render.color = color;
		
		CTransform transform = this.getComponent(CTransform.class);
		transform.pos.set(x, y);
		transform.size.set(width, height);
	}
}
