package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import org.jbox2d.common.Vec2;

public class Level_1 implements ILevel {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	public BasicPhysicsEngineUsingBox2D game;
	
	private Image worldBgSprite = null;
	
	
	@Override
	public int getNumberOfCharges() {
		return 1;
	}

	public void loadGraphicsResources()
	{
		System.out.println("Loading graphic resources");
		worldBgSprite = Resources.loadImageFromFile(Resources.PATH_STAR_FIELD_BG);
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
		
		Vec2 ballPosition = new Vec2(2, 6);
		float ballRadius = 0.2f;
		game.createBall(ballPosition, ballRadius);
		
		float cannonSize = 0.1f;
		float cannonInteractableRange = 1.2f;
		float cannonShootForce = 10f;
		ElementCannon cannon = new ElementCannon(
				new Vec2(2, 2),
				cannonSize,
				cannonInteractableRange,
				cannonShootForce,
				Color.white, 
				game); 
		
		float grappleSize = 0.1f;
		float grappleInteractableRange = 1.2f;
		new ElementGrapple(
				new Vec2(9, 5),
				grappleSize,
				grappleInteractableRange,
				BasicPhysicsEngineUsingBox2D.world, 
				game
				);

		new BasicRectangle(
				new Vec2(13, 4),	//position
				new Vec2(1, 8),	//size
				Color.darkGray,		//color
				game);	//polygonsList
		
		//Top spike
		new SpikeRectangle(
				new Vec2(10f, playAreaSize.y), 		//position
				new Vec2(9f, 0.4f),		//size
				0f,						//angle in degrees
				game);
		
		
		//Bottom spike
		new SpikeRectangle(
				new Vec2(8f, 0f), 		//position
				new Vec2(9f, 0.4f),		//size
				0f,						//angle in degrees
				game);
		
		new WinPlatformPolygon(
				new Vec2(15, playAreaSize.y / 2f),
				new Vec2(1, playAreaSize.y),
				Color.green,
				game);
		
		new ConcentricRectanglePolygon(
				rectPosition,				//position
				playAreaSize,				//size
				skinWidth,					//skin width
				Color.darkGray,				//colors
				true,						//draw borders
				game						
				);
	}
}
