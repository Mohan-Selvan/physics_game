package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

public class ElementGrapple extends BasicParticle implements InteractableElement  {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	private float pullRange = 0f;
	
	private final int PULL_RADIUS_SCREEN; 
	
	// Trackers
	private boolean isGrappleActive = false;
	private Joint distanceJoint = null;
	
	//Grapple settings
	private float swingForce = 1f;
	
	//References
	private World world;
	
	public ElementGrapple(Vec2 spawnPosition, float radius, float range, World world, BasicPhysicsEngineUsingBox2D game) {
		super(spawnPosition, radius, Constants.GRAPPLE_COLOR, 0f, 0f);
		
		this.pullRange = range;
		
		this.PULL_RADIUS_SCREEN=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(pullRange), 1);
		this.body.setType(BodyType.STATIC);

		this.world = world;
		
		isGrappleActive = false;
		
		this.body.getFixtureList().setSensor(true);
		
		game.elements.add(this);
		game.particles.add(this);
	}

	@Override
	public void processInput()
	{		
		BallParticle ball = BasicPhysicsEngineUsingBox2D.getBall();
		
		if(!isGrappleActive)
		{
			if(InputListener.isSpaceKeyDown() && isBallInRange(ball))
			{
				AttachBall(ball);
			}
		}
		else 
		{
			//Applying swinging force
			if(InputListener.isLeftKeyDown())
			{
				// Applying swinging force (clockwise)
				Vec2 tangentVector = Utilities.getDirection(ball.body.getPosition().clone(), this.body.getPosition().clone()); 
				Vec2 direction = new Vec2(-tangentVector.y, tangentVector.x); //Rotates the vector by 90 degrees counter clockwise
				Vec2 targetVelocity = ball.body.getLinearVelocity().clone().add(direction).mul(swingForce);
				ball.setLinearVelocity(targetVelocity);
			}
			else if(InputListener.isRightKeyDown())
			{
				// Applying swinging force (counter clockwise)
				Vec2 tangentVector = Utilities.getDirection(ball.body.getPosition().clone(), this.body.getPosition().clone()); 
				Vec2 direction = new Vec2(tangentVector.y, -tangentVector.x); //Rotates the vector by 90 degrees clockwise
				Vec2 targetVelocity = ball.body.getLinearVelocity().clone().add(direction).mul(swingForce);
				ball.setLinearVelocity(targetVelocity);
			}
			
			if(InputListener.isSpaceKeyDown())
			{
				DetachBall(ball);
			}
		}
	}
	
	private boolean isBallInRange(BallParticle ball)
	{
		return ball.body.getPosition().sub(this.body.getPosition()).length() <= this.pullRange;
	}

	@Override
	public void draw(Graphics2D g) {
		
		super.draw(g);
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Constants.GRAPPLE_COLOR);
		g.drawArc(x - PULL_RADIUS_SCREEN, y - PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 0, 360);
	}
	
	private void AttachBall(BallParticle ball)
	{
		System.out.println("Attaching ball!");
		
		DistanceJointDef distanceJointDef = new DistanceJointDef();
		
		distanceJointDef.initialize(this.body, ball.body, this.body.getPosition(), ball.body.getPosition());

		distanceJointDef.collideConnected = true;
		distanceJointDef.frequencyHz = 10f;
		distanceJointDef.dampingRatio = 0f;
		
		distanceJointDef.length = this.pullRange;
				
		distanceJoint = this.world.createJoint(distanceJointDef);

		isGrappleActive = true;
	}
	
	private void DetachBall(BallParticle ball)
	{
		System.out.println("Detaching ball!");
		
		if(distanceJoint != null)
		{
			this.world.destroyJoint(distanceJoint);
			distanceJoint = null;
		}
		
		isGrappleActive = false;
	}
	
	public void activeDraw(Graphics2D g)
	{
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Color.white);
		g.drawArc(x - PULL_RADIUS_SCREEN, y - PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 0, 360);
				
		if(isGrappleActive)
		{
			BallParticle ball = BasicPhysicsEngineUsingBox2D.getBall();		
			Vec2 endPosition = ball.body.getPosition();
			
			int screenPosX = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(endPosition.x);
			int screenPosY = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(endPosition.y);
			
			g.drawLine(x, y,
					screenPosX,
					screenPosY
					);
		}
	}
	
	public void debugDraw(Graphics2D g)
	{
		//Draw debug elements here
	}
	
	@Override
	public boolean isLocked()
	{
		return isGrappleActive;
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public float getInteractionRange() {
		return pullRange;
	}

	@Override
	public void resetElement() {
		
	}
}
