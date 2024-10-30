package pbg_wrapperForJBox2D;

import java.awt.Color;

import org.jbox2d.common.Vec2;

public class JumpPadRectangle extends BasicRectangle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public JumpPadRectangle(Vec2 position, Vec2 size, float angleInDegrees, float restitution, Color color, BasicPhysicsEngineUsingBox2D game) {
		this(position, size, angleInDegrees, restitution, color, true, Constants.BORDER_COLOR, game);
	}
	
	public JumpPadRectangle(Vec2 position, Vec2 size, float angleInDegrees, float restitution, Color color, boolean drawBorders, Color borderColor, BasicPhysicsEngineUsingBox2D game) {
		super(position, size, angleInDegrees, color, drawBorders, borderColor, game);
		
		
		JumpPadUserData jumpPadUserData = new JumpPadUserData();
		jumpPadUserData.restitution = restitution;
		
		this.body.getFixtureList().setUserData(jumpPadUserData);
	}
}
