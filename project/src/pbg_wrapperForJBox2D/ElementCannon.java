package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

public class ElementCannon extends BasicParticle implements InteractableElement  {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private float pullRange = 0f;
	private final int PULL_RADIUS_SCREEN; 
	
	// Trackers
	private boolean isCannonActive = false;
	
	//Shoot parameters
	private float shootAngle;
	private float shootForce;
	
	//Interactable parameters
	private float rotateStepAngle = 45;
	
	//Graphic resources
	Image cannonSprite = null;
	
	public ElementCannon(Vec2 spawnPosition, float cannonSize, float range, float shootForce, Color color, BasicPhysicsEngineUsingBox2D game) {
		super(spawnPosition, cannonSize, color, 0f, 0f);
		
		this.pullRange = range;
		
		this.PULL_RADIUS_SCREEN=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(pullRange), 1);
		this.body.setType(BodyType.KINEMATIC);
		
		this.shootAngle = 0f;
		this.shootForce = shootForce;
		this.isCannonActive = false;
		
		game.elements.add(this);
		game.particles.add(this);
		
		this.body.getFixtureList().setSensor(true);
		
		cannonSprite = Resources.loadImageFromFile(Resources.PATH_CANNON_IMAGE);
		
		if(cannonSprite != null)
		{
			cannonSprite = cannonSprite.getScaledInstance(32, 32, 1);
		}
	}

	@Override
	public void processInput()
	{
		BallParticle ball = BasicPhysicsEngineUsingBox2D.getBall();
		
		if(!isCannonActive)
		{
			if(InputListener.isSpaceKeyDown() && isBallInRange(ball))
			{
				LoadBall(ball);
			}
		}
		else 
		{
			if(InputListener.isLeftKeyDown())
			{
				RotateCannon(rotateStepAngle);
			}
			else if(InputListener.isRightKeyDown())
			{
				RotateCannon(-rotateStepAngle);
			}
			
			if(InputListener.isSpaceKeyDown())
			{
				ShootBall(ball);
			}
		}
	}
	
	private void RotateCannon(float angle)
	{
		shootAngle += angle;
		shootAngle = (shootAngle % 360);
	}
	
	private boolean isBallInRange(BallParticle ball)
	{
		return ball.body.getPosition().sub(this.body.getPosition()).length() <= this.pullRange;
	}
	
	private void LoadBall(BallParticle ball)
	{
		//System.out.println("Ball is in range!");
		ball.body.setType(BodyType.STATIC);
		ball.body.setTransform(this.body.getPosition(), 0f);
		ball.EnableBallSprite(false);
		
		isCannonActive = true;
	}
	
	private void ShootBall(BallParticle ball)
	{
		Vec2 shootVelocity = getShootDirection().mul(shootForce);
		
		System.out.println("Shooting ball : " + shootVelocity);
		ball.body.setType(BodyType.DYNAMIC);
		ball.body.setLinearVelocity(shootVelocity);
		ball.EnableBallSprite(true);
		
		isCannonActive = false;
	}

	@Override
	public void draw(Graphics2D g) {
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Constants.CANNON_COLOR);
		g.drawArc(x - PULL_RADIUS_SCREEN, y - PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 0, 360);
		
		if(isCannonActive)
		{
			Vec2 direction = getShootDirection().clone();
			direction.normalize();
			
			Vec2 lineEnd = direction.add(body.getPosition().clone());
			Vec2 lineStart = this.body.getPosition().clone().add(direction.clone().mul(0.2f));
			
			int lineStartScreenPosX = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(lineStart.x);
			int lineStartScreenPosY = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(lineStart.y);
			
			int lineEndScreenPosX = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(lineEnd.x);
			int lineEndScreenPosY = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(lineEnd.y);
			
			g.setColor(Constants.ACTIVE_COLOR);
			g.drawLine(
					lineStartScreenPosX,
					lineStartScreenPosY,
					lineEndScreenPosX,
					lineEndScreenPosY
					);
		}
		
		if(Constants.DRAW_SPRITES && cannonSprite != null)
		{
			AffineTransform at = new AffineTransform();
			int posX = x;
			int posY = y;
			
			at.translate(posX - (cannonSprite.getWidth(null) /2), posY - cannonSprite.getHeight(null)/2);
			at.rotate(Math.toRadians(-shootAngle), cannonSprite.getWidth(null)/2, cannonSprite.getHeight(null)/2);
			//at.scale(SCALE, -SCALE);
			g.drawImage(cannonSprite, at, null);
		}
		else
		{
			super.draw(g);
		}
	}
	
	
	public void activeDraw(Graphics2D g)
	{
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		g.setColor(Color.white);
		g.drawArc(x - PULL_RADIUS_SCREEN, y - PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 2 * PULL_RADIUS_SCREEN, 0, 360);
	}
	
	private Vec2 getShootDirection()
	{
		Vec2 direction = Utilities.polarToCartesian(shootAngle, 1f);
		direction.normalize();
		return direction;
	}
	
	public void debugDraw(Graphics2D g)
	{
		//Draw range
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public boolean isLocked()
	{
		return isCannonActive;
	}
	
	@Override
	public float getInteractionRange() {
		return pullRange;
	}

	@Override
	public void resetElement() {
		
	}
}
