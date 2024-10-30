package pbg_wrapperForJBox2D;

import java.awt.Color;

import org.jbox2d.common.Vec2;

public class Door extends BasicRectangle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */

	private BasicPhysicsEngineUsingBox2D game;

	public Door(Vec2 position, Vec2 size, Color color, BasicPhysicsEngineUsingBox2D game) {
		super(position, size, color, game);
		
		this.game = game;
	}
	
	public void disableDoor()
	{
		this.body.setActive(false);
		game.polygons.remove(this);
	}
}
