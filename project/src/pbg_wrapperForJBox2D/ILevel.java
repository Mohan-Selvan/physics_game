package pbg_wrapperForJBox2D;

import java.awt.Graphics2D;

/*
 * Author: Moganaselvan Ramamoorthy
 */


public interface ILevel {
	
	// For loading sprites and initialization
	void loadGraphicsResources();
	
	// Draw call
	void draw(Graphics2D g);
	
	// Constructs the level
	void buildLevel(BasicPhysicsEngineUsingBox2D game);
	
	// Ball parameters
	int getNumberOfCharges();
}
