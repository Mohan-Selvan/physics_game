package pbg_wrapperForJBox2D;

import java.awt.Graphics2D;


import org.jbox2d.dynamics.Body;

public interface InteractableElement {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */

	public Body getBody();
	
	public void processInput();

	public void debugDraw(Graphics2D g);
	
	public void activeDraw(Graphics2D g);
	
	public float getInteractionRange();
	
	public void resetElement();
	
	public boolean isLocked();
}
