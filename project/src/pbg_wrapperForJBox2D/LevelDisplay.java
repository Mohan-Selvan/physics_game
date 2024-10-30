package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import org.jbox2d.common.Vec2;

public class LevelDisplay implements ILevel {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	public BasicPhysicsEngineUsingBox2D game;
	
	private Image worldBgSprite = null;
	
	
	@Override
	public int getNumberOfCharges() {
		return 5;
	}

	public void loadGraphicsResources()
	{
		//System.out.println("Loading graphic resources");
		//worldBgSprite = Resources.loadImageFromFile(Resources.PATH_STAR_FIELD_BG);
	}
	
	public void draw(Graphics2D g)
	{
		if(worldBgSprite != null)
		{
			int currentX = -worldBgSprite.getWidth(null);
			for(int i = 0; i < 5; i++)
			{
				g.drawImage(worldBgSprite, currentX, 0, null);
				g.drawImage(worldBgSprite, currentX, 0 + worldBgSprite.getHeight(null), null);
				currentX += worldBgSprite.getWidth(null);
			}
		}
	}
	
	
	public void buildLevel(BasicPhysicsEngineUsingBox2D game)
	{
		this.game = game;
		
		float rectangleWidth = game.WORLD_WIDTH * 10f;
		
		float skinWidth = 10f;
		Vec2 playAreaSize = new Vec2(game.WORLD_WIDTH * 10f, 10);
		Vec2 rectPosition = new Vec2((playAreaSize.x /2f) - 0.008f, (playAreaSize.y/2));
		
		float topY = 8;
		float bottomY = 2;
		
		float posX = 2f;
		
		Vec2 ballPosition = new Vec2(posX, topY);
		float ballRadius = 0.2f;
		game.createBall(ballPosition, ballRadius);

		posX += 0;
		float cannonSize = 0.1f;
		float cannonInteractableRange = 1.2f;
		float cannonShootForce = 10f;
		ElementCannon cannon = new ElementCannon(
				new Vec2(posX, topY),
				cannonSize,
				cannonInteractableRange,
				cannonShootForce,
				Color.white, 
				game); 
		
		posX += 3.2f;
		float grappleSize = 0.1f;
		float grappleInteractableRange = 1.2f;
		ElementGrapple grapple = new ElementGrapple(
				new Vec2(posX, topY),
				grappleSize,
				grappleInteractableRange,
				BasicPhysicsEngineUsingBox2D.world, 
				game
				);
		
		posX += 3.2f;
		//Attractor
		float attractorRadius = 0.1f;
		float attractorRange = 1.2f;
		float attractorPullForce = 0.3f;
		ElementAttractor attractor = new ElementAttractor(
				new Vec2(posX, topY),
				attractorRadius,
				attractorRange,
				attractorPullForce,
				game); 

		posX = 2f;
		new PortalSet(
				new Vec2(posX, bottomY),			//portal 1 position
				90f,																	//portal 1 angle in degrees
				new Vec2(posX + 1.5f, bottomY),	//portal 2 position
				90f,																	//portal 2 angle in degrees
				0.3f,																	//portal radius
				BasicPhysicsEngineUsingBox2D.world,										//world object
				game																	//game reference
				);
		
		posX += 4f;
		
		new KeyDoorSet(
				new Vec2(posX, bottomY),				//door position
				0.15f, 												//key radius
				new Vec2(posX + 1f, bottomY),									//key position
				new Vec2(0.2f, 2f),						//door size
				Utilities.getColorWithAlpha(Constants.getNextColor(), 0.6f),	//pair color
				game);	
		
		posX += 3f;
		WinPlatformPolygon winPlatformPolygon = new WinPlatformPolygon(
				new Vec2(posX, bottomY),
				new Vec2(0.2f, 2f),
				Color.green,
				game);
		
		
		posX += 2;
		new SpikeRectangle(new Vec2(posX, bottomY), new Vec2(0.2f, 2f), game);
		
		new ConcentricRectanglePolygon(
				rectPosition,				//position
				playAreaSize,				//size
				skinWidth,					//skin width
				Color.darkGray,				//colors
				true,						//draw borders
				game						
				);
		
		//Elements
//		float grappleRadius = 0.2f;
//		float grapplePullRange = 1.5f;
//		ElementGrapple grapple = new ElementGrapple(
//				new Vec2(WORLD_WIDTH * 0.15f, WORLD_HEIGHT * 0.3f), 
//				grappleRadius,
//				grapplePullRange,
//				Color.white,
//				world, 
//				elements,
//				particles); 

		//Attractor
//		float attractorRadius = 0.2f;
//		float attractorRange = 3f;
//		float attractorPullForce = 0.3f;
//		ElementAttractor attractor = new ElementAttractor(
//				new Vec2(WORLD_WIDTH * 0.4f, WORLD_HEIGHT * 0.3f),
//				attractorRadius,
//				attractorRange,
//				attractorPullForce,
//				Color.pink,
//				elements,
//				particles); 
	}
}
