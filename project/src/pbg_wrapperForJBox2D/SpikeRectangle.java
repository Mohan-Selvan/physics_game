package pbg_wrapperForJBox2D;

import java.awt.Color;

import org.jbox2d.common.Vec2;

public class SpikeRectangle extends BasicRectangle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public SpikeRectangle(Vec2 position, Vec2 size, BasicPhysicsEngineUsingBox2D game) {
		this(position, size, 0f, Constants.SPIKE_COLOR, true, Constants.BORDER_COLOR, game);
	}
	
	public SpikeRectangle(Vec2 position, Vec2 size, float angleInDegrees, BasicPhysicsEngineUsingBox2D game) {
		this(position, size, angleInDegrees, Constants.SPIKE_COLOR, true, Constants.BORDER_COLOR, game);
	}
	
	
	public SpikeRectangle(Vec2 position, Vec2 size, float angleInDegrees, Color color, boolean drawBorders, Color borderColor, BasicPhysicsEngineUsingBox2D game) {
		super(position, size, angleInDegrees, color, drawBorders, borderColor, game);
		
		this.body.getFixtureList().setUserData(Constants.getSpikeFixtureUserData());
	}
}
