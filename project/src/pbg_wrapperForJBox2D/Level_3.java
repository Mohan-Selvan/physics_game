package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import org.jbox2d.common.Vec2;

public class Level_3 implements ILevel {
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
		
		Vec2 ballPosition = new Vec2(2, 5);
		float ballRadius = 0.2f;
		game.createBall(ballPosition, ballRadius);

		new KeyDoorSet(
				new Vec2(2, 6f),									//key position
				0.15f, 												//key radius
				new Vec2(2, 3),										//door position
				new Vec2(1f, 0.2f),									//door size
				Constants.getNextColor(),							//pair color
			game);
		
		
		float cannonSize = 0.1f;
		float cannonInteractableRange = 1.2f;
		float cannonShootForce = 5f;
		new ElementCannon(
				new Vec2(2, 8),
				cannonSize,
				cannonInteractableRange,
				cannonShootForce,
				Color.white, 
				game); 
		
		
		new JumpPadRectangle(
				new Vec2(2, 2),							//position 
				new Vec2(2, 0.2f),						//size
				-30f,									//angle
				Constants.DEFAULT_JUMP_PAD_RESTITUTION,	//restitution
				Constants.JUMP_PAD_COLOR, 				//color
				game									//game reference
				);					
		
		
		
		//Angled spike
		new SpikeRectangle(
				new Vec2(12f, 6f), 	//position
				new Vec2(2f, 0.2f),	//size
				-45f, 				//angle in degrees
				game);

		//Attractor
		float attractorRadius = 0.1f;
		float attractorRange = 2.9f;
		float attractorPullForce = 0.3f;
		new ElementAttractor(
				new Vec2(8.5f, 3.2f),
				attractorRadius,
				attractorRange,
				attractorPullForce,
				game);
		
		new MovingPlatform(
				new Vec2(0.4f, 4), //size
				Color.darkGray,
				new ArrayList<Vec2>(Arrays.asList(		//waypoints list
						new Vec2(14, 8),
						new Vec2(14, 2)
						)),
				1f, 	//duration per waypoint
				0.25f, 	//wait duration per waypoint
				false, 	//is spike platform
				game);
				
		
		new KeyDoorSet(
				new Vec2(7, 5),
				0.2f, 
				new Vec2(15.5f, playAreaSize.y / 2),
				new Vec2(0.4f, playAreaSize.y),
				Constants.getNextColor(),
				game);
				
		new SpikeRectangle(
				new Vec2(8f, 0f), 	//position
				new Vec2(10f, 0.4f),	//size
				0f, 				//angle in degrees
				game);
		
		new WinPlatformPolygon(
				new Vec2(17.5f, playAreaSize.y / 2f),
				new Vec2(0.4f, playAreaSize.y),
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
