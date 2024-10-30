package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.jbox2d.common.Vec2;

public class BasicView extends JComponent {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Modified by: Moganaselvan Ramamoorthy
	 */
	
	
	// background colour
	public static final Color BG_COLOR = Color.BLACK;
	
	//References
	private BasicPhysicsEngineUsingBox2D game;
	private UiHandler uiHandler;
	
	//Trackers
	private Vec2 followTargetPosition = null;
	private float lookAheadDistance = Constants.CAMERA_LOOK_AHEAD_DISTANCE;
	private float cameraSpeed = Constants.CAMERA_FOLLOW_SPEED;
	private float cameraScreenOffsetXNormalised = Constants.CAMERA_FOLLOW_OFFSET_X_NORMALIZED;
	
	//Other tracking variables
	float currentX = 0f;

	public BasicView(BasicPhysicsEngineUsingBox2D game) {
		this.game = game;
		
		initializeBasicView();
	}
	
	public void initializeBasicView()
	{
		System.out.println("Initializing view!");
		
		this.uiHandler = new UiHandler(game);
		
		game.getActiveLevel().loadGraphicsResources();
		
		//Setting follow target position
		this.followTargetPosition = BasicPhysicsEngineUsingBox2D.getBall().body.getPosition();
	}


	@Override
	public void paintComponent(Graphics g0) {
		BasicPhysicsEngineUsingBox2D game;
		synchronized(this) {
			game=this.game;
		}
		Graphics2D g = (Graphics2D) g0;

		BallParticle ball = game.getBall();
		AffineTransform at = g.getTransform();
		
		double uiPanelScreenCoordX = at.getTranslateX();
		double uiCameraScale = 1f;
		
		//Calculates look ahead position and follow the target
		if(ball != null)
		{
			followTargetPosition = Utilities.lerpVector(followTargetPosition.clone(),
					ball.body.getPosition().add(ball.getBallFacingDirection().mul(lookAheadDistance)), 
					cameraSpeed);
					
						
			int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(followTargetPosition.x);
			int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(followTargetPosition.y);
			
			float targetX = -x + (getWidth() * cameraScreenOffsetXNormalised);
			
			double targetTranslationX = Utilities.lerp(at.getTranslateX(), targetX, 1f);			
			uiPanelScreenCoordX = targetTranslationX;
			
			at.translate(targetTranslationX, getHeight() * 0.003f);		
			g.setTransform(at);			
		}
				
		// paint the background
		g.setColor(BG_COLOR);		
		int multSize = 10;
		g.fillRect(-getWidth() * multSize, -getHeight() * multSize, getWidth() * multSize * 4, getHeight() * multSize * 4);
		
		//Draw call on each level, used to draw unique backgrounds for each level
		game.getActiveLevel().draw(g);
		
		for (BasicParticle p : game.particles)
		{			
			p.draw(g);
		}
		for (BasicPolygon p : game.polygons)
		{
			p.draw(g);		
		}
		for (AnchoredBarrier b : game.barriers)
		{
			b.draw(g);
		}
		for	(ICompoundElement c : game.compoundElements)
		{
			c.draw(g);
		}
		
		//Debug draw call
		if(BasicPhysicsEngineUsingBox2D.IsDebugDrawEnabled)
		{			
			for (InteractableElement e : game.elements)
			{
				e.debugDraw(g);
			}
		}
		
		//Draw call on active element
		if(game.ActiveElement != null)
		{
			game.ActiveElement.activeDraw(g);
		}
		
		//Draws UI Panel
		if(uiHandler != null)
		{
			uiHandler.draw(g, Math.round((float)Math.floor(uiPanelScreenCoordX)), 0, uiCameraScale);
		}
		
		
		//Draws follow target position
		if(BasicPhysicsEngineUsingBox2D.IsDebugDrawEnabled)
		{
			int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(followTargetPosition.x);
			int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(followTargetPosition.y);
			
			g.setColor(Color.magenta);
			g.drawOval(x - 5, y - 5, 10, 10);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return BasicPhysicsEngineUsingBox2D.FRAME_SIZE;
	}
	
	public synchronized void updateGame(BasicPhysicsEngineUsingBox2D game) {
		this.game=game;
	}
}