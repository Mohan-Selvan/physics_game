package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class BallParticle extends BasicParticle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	//References
	BasicPhysicsEngineUsingBox2D game;
		
	//Charge settings
	private int maxNumberOfCharges = 0;
	private int chargesLeft = 0;
	private float chargeMagnitude = 8f;
	
	//Flags
	private boolean drawBall = true;
	
	//Visuals
	private Color ballColor1 = Constants.BALL_COLOR_1;
	private Color ballColor2 = Constants.BALL_COLOR_2;
	
	//Ball trail
	List<Vec2> ballTrailPositionsList = null;
	int ballTrailStartIndex = 0;
	int ballTrailLength = Constants.BALL_TRAIL_LENGTH;
	float trailUpdateDelay = Constants.BALL_TRAIL_UPDATE_DELAY;
	float lastTrailUpdateTime = 0f;
	
	//Trackers - Direction
	private Vec2 chargeDirection;
	private Vec2 positionLastFrame;
	private Vec2 ballDirection;
	
	//Trackers - Position
	float ballRadius = 1f;
	boolean moveNextFrame = false;
	Vec2 nextFrameWorldPosition = new Vec2(0, 0);
	Vec2 nextFrameLinearVelocity = new Vec2(0, 0);
	
	private int INNER_RADIUS;

	public BallParticle(Vec2 spawnPosition, float radius, int maxNumberOfCharges, BasicPhysicsEngineUsingBox2D game) {
		super(spawnPosition.x, spawnPosition.y, 0, 0, radius, Constants.BALL_COLOR_1, 1f, 0f);

		this.INNER_RADIUS=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(radius),1);
		
		this.game = game;
		this.ballRadius = radius;
		this.drawBall = true;
		
		this.chargeDirection = new Vec2(0f, 0f);
		this.maxNumberOfCharges = maxNumberOfCharges;
		this.chargesLeft = maxNumberOfCharges;
		
		this.positionLastFrame = new Vec2(0f, 0f);
		this.ballDirection = new Vec2(0f, 0f);
		
		//Enable friction on the ball only when it is enabled in Constants class 
		if(Constants.ENABLE_BALL_FRICTION)
		{
			this.body.getFixtureList().setFriction(1000f);
		}
		
		//Setting ball's body to bullet 
		//in order to avoid pass through issues - just in case
		this.body.setBullet(true);
	
		//Trail list
		ballTrailPositionsList = new ArrayList<Vec2>();
		for(int i = 0; i < ballTrailLength; i++)
		{
			ballTrailPositionsList.add(spawnPosition);
		}
		
		ballTrailStartIndex = 0;
	}
	
	public void Tick(float dt)
	{		
		this.ballDirection = Utilities.getDirection(positionLastFrame, this.body.getPosition().clone()); 
		
		this.chargeDirection = InputListener.getInputDirection();
		
		if(moveNextFrame) //Check if next frame position state has changed.
		{
			moveNextFrame = false;
			this.body.setTransform(nextFrameWorldPosition, 0f);
			this.body.setLinearVelocity(nextFrameLinearVelocity);
			
			return;
		}
		
		if(InputListener.isXKeyReleased() && !game.isBallLocked())
		{			
			System.out.println("Charge key event received! : Charge left : " + chargesLeft + " Direction : " + chargeDirection);
			
			if(canCharge())
			{
				chargesLeft--;
				System.out.println("Charging.. : Charge left : " + chargesLeft);
				this.setLinearVelocity(chargeDirection.mul(chargeMagnitude));
			}
		}
		
		if(this.body.getLinearVelocity().lengthSquared() > Constants.getBallVelocityMaxThresholdSquared())
		{
			//System.out.println("Clamping ball velocity to " + Constants.BALL_VELOCITY_MAX_THRESHOLD);
			Vec2 clampedVelocity = Utilities.clampVector(this.body.getLinearVelocity().clone(), Constants.BALL_VELOCITY_MAX_THRESHOLD);
			this.body.setLinearVelocity(clampedVelocity);
		}
		
		//Updating tracking variables
		this.positionLastFrame = this.body.getPosition().clone();
		
		
		
		
		if(Constants.DRAW_BALL_TRAIL_ONLY_WHEN_TIME_SLOW)
		{
			//Reset trail positions if time is slowed down this frame.
			if(game.getIsTimeSlowedDown() && !game.getWasTimeSlowedDownLastFrame())
			{
				for(int i = 0; i < ballTrailPositionsList.size(); i++)
				{
					ballTrailPositionsList.set(i, this.body.getPosition().clone());
				}
			}			
		}
		
		//Updating position for trail
		if((game.gameTimer - lastTrailUpdateTime) > trailUpdateDelay)
		{		
			//Update current position at start index pointer
			this.ballTrailStartIndex = (ballTrailStartIndex + 1) % ballTrailLength;
			this.ballTrailPositionsList.set(ballTrailStartIndex, this.body.getPosition().clone());			
			lastTrailUpdateTime = game.gameTimer;
		}
	}

	@Override
	public void draw(Graphics2D g)
	{	
		//super.draw(g);
		
		if(!drawBall) {return;}
		
		Color ballColor = ballColor1;
		if(getMaxOfCharges() > 0)
		{
			float t = (float)getNumberOfChargesLeft() / (float)getMaxOfCharges();
			ballColor = Utilities.lerpColor(ballColor2, ballColor1, t);
		}	
		
		//Draw ball trail only if time is slowed and trail is enabled in constants class
		if(Constants.DRAW_BALL_TRAIL && (game.getIsTimeSlowedDown() || (!Constants.DRAW_BALL_TRAIL_ONLY_WHEN_TIME_SLOW)))
		{
			//Draw ball trail on the screen
			int index = ballTrailStartIndex;
			
			Vec2 ballTrailAlphaRangeNormalized = new Vec2(0.4f, 1f);
			Vec2 ballTrailSizeRangeNormalized = new Vec2(0.2f, 0.9f);
			
			for(int i = 0; i < ballTrailLength; i++)
			{				
				Vec2 position = ballTrailPositionsList.get(index);
								
				int tx = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(position.x);
				int ty = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(position.y);
				
				float t = ((float)(i+1)) / ((float)ballTrailLength);
				
				int r = (int)Math.max(
						BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(
								Utilities.lerp(
										this.ballRadius * ballTrailSizeRangeNormalized.x,
										this.ballRadius * ballTrailSizeRangeNormalized.y,
										1f - t))
						, 1);
				
				float alpha = Utilities.lerp(
						ballTrailAlphaRangeNormalized.x,
						ballTrailAlphaRangeNormalized.y,
						1f - t);
				
				g.setColor(Utilities.getColorWithAlpha(Color.DARK_GRAY, alpha));
				g.fillOval(tx - r, ty - r, 2 * r, 2 * r);
								
				
				//FOR SOME REASON MOD OPERATOR DOES NOT WORK THE WAY IT SHOULD!
				//index = (index - 1) % ballTrailLength;
				
				index -= 1;
				
				if(index < 0)
				{
					index = ballTrailLength - 1;
				}
			}
		}
		
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		if(InputListener.isXKeyPressed() && canCharge() && !game.isBallLocked())
		{
			g.setStroke(Constants.getBorderStroke());
			
			Vec2 currentWorldPosition = body.getPosition().clone();
			Vec2 lineEndWorldPosition = currentWorldPosition.add(chargeDirection);
			
			int lineEndX = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(lineEndWorldPosition.x);
			int lineEndY = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(lineEndWorldPosition.y);
			
			g.setColor(Color.white);
			g.drawLine(x, y, lineEndX, lineEndY);
		}
	
		g.setColor(ballColor);
		g.fillOval(x - INNER_RADIUS, y - INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);
		
		if(Constants.DRAW_BORDERS)
		{
			g.setColor(Color.white);
			g.drawOval(x - INNER_RADIUS, y - INNER_RADIUS, 2 * INNER_RADIUS, 2 * INNER_RADIUS);
		}
	}
	
	@Override
	public void notificationOfNewTimestep() {
		super.notificationOfNewTimestep();
	}
	
	public int getNumberOfChargesLeft()
	{
		return chargesLeft;
	}
	
	public int getMaxOfCharges()
	{
		return maxNumberOfCharges;
	}
	
	public Vec2 getBallFacingDirection()
	{
		return this.ballDirection;
	}
	
	public boolean canCharge()
	{
		return chargesLeft != 0;
	}
	
	public void MoveToPositionNextFrame(Vec2 targetWorldPosition, Vec2 linearVelocity)
	{
		this.nextFrameWorldPosition = targetWorldPosition;
		this.nextFrameLinearVelocity = linearVelocity;
		this.moveNextFrame = true;
	}
	
	public void ApplyPullForce(Vec2 targetPosition, float magnitude)
	{
		Vec2 targetVelocity = this.body.getLinearVelocity().clone();
		Vec2 direction = Utilities.getDirection(this.body.getPosition(), targetPosition);
		targetVelocity = targetVelocity.add(direction.mul(magnitude));
		
		this.body.setLinearVelocity(targetVelocity);
	}
	
	public void addLinearVelocity(Vec2 velocityToAdd)
	{
		this.setLinearVelocity(this.body.getLinearVelocity().add(velocityToAdd));
	}
	
	public void setLinearVelocity(Vec2 targetVelocity)
	{
		this.body.setLinearVelocity(new Vec2(0, 0));
		this.body.setLinearVelocity(targetVelocity);
	}
	
	public void SetFixtureUserData(BallUserData userData)
	{
		this.body.getFixtureList().setUserData(userData);
	}
	
	public void killBall()
	{
		//System.out.println("Killing ball!");
		game.handleBallDied();
	}
	
	public void EnableBallSprite(boolean value)
	{
		this.drawBall = value;
	}
	
	public void HandleBallTouchedWinPlatform()
	{
		//System.out.println("Ball touched win platform!");
		game.handleBallTouchedWinPlatform();	
	}
}

