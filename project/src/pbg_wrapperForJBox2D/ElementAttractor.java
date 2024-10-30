package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;


public class ElementAttractor extends BasicParticle implements InteractableElement  
{	
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private final int GRAPPLE_RADIUS_SCREEN; 
	private float pullRange = 1f;
	
	//Attractor settings
	private float pullForce = 0f;
	
	private boolean isGrappleOn = false;

	public ElementAttractor(Vec2 spawnPosition, float radius, float pullRange, float pullForce, BasicPhysicsEngineUsingBox2D game) {
		super(spawnPosition, radius, Constants.ATTRACTOR_COLOR, 1f, 0f);
		
		this.pullRange = pullRange;
		this.pullForce = pullForce;
		this.isGrappleOn = false;
		
		this.GRAPPLE_RADIUS_SCREEN=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(pullRange), 1);
		
		this.body.setType(BodyType.KINEMATIC);
		
		this.body.getFixtureList().setSensor(true);
		
		game.elements.add(this);
		game.particles.add(this);
	}
	
	@Override
	public void processInput() 
	{
		BallParticle ball = BasicPhysicsEngineUsingBox2D.getBall();
		
		isGrappleOn = InputListener.isSpaceKeyPressed();
		
		if(isGrappleOn && isBallInRange(ball))
		{
			//System.out.println("Applying pull force : " + this.pullForce);
			ball.ApplyPullForce(this.body.getPosition(), pullForce);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		super.draw(g);
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Constants.ATTRACTOR_COLOR);
		g.drawArc(x - GRAPPLE_RADIUS_SCREEN, y - GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 0, 360);
		
		if(isGrappleOn)
		{
			int rangeX = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
			int rangeY = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
			
			g.setColor(Utilities.getColorWithAlpha(Constants.ATTRACTOR_COLOR, 0.4f));
			g.fillArc(rangeX - GRAPPLE_RADIUS_SCREEN, rangeY - GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 0, 360);
		}
	}

	@Override
	public void debugDraw(Graphics2D g)
	{	
		
	}

	@Override
	public void activeDraw(Graphics2D g) 
	{
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Color.white);
		g.drawArc(x - GRAPPLE_RADIUS_SCREEN, y - GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 2 * GRAPPLE_RADIUS_SCREEN, 0, 360);
	}

	@Override
	public float getInteractionRange() 
	{
		return this.pullRange;
	}

	@Override
	public void resetElement()
	{
		this.isGrappleOn = false;
	}
	
	@Override
	public Body getBody() 
	{
		return this.body;
	}
	
	private boolean isBallInRange(BallParticle ball)
	{
		return ball.body.getPosition().sub(this.body.getPosition()).length() <= this.pullRange;
	}

	@Override
	public boolean isLocked() 
	{
		return false;
	}
}
