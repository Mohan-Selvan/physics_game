package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.util.List;

import org.jbox2d.common.Vec2;

public class WinPlatformPolygon extends BasicRectangle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public WinPlatformPolygon(Vec2 position, Vec2 size, Color color, BasicPhysicsEngineUsingBox2D game) {
		super(position, size, color, game);
		
		this.body.getFixtureList().setUserData(Constants.getWinPlatformFixtureUserData());
	}
}
