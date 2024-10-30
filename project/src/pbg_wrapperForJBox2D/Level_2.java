package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import org.jbox2d.common.Vec2;

public class Level_2 implements ILevel {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	public BasicPhysicsEngineUsingBox2D game;
	
	private Image worldBgSprite = null;
	
	@Override
	public int getNumberOfCharges() {
		return 8;
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
		
		float playAreaCeilingY = rectPosition.y + (playAreaSize.y / 2);
		float playAreaFloorY = rectPosition.y - (playAreaSize.y / 2);
		
		//Parameters
		float cannonSize = 0.1f;
		float cannonInteractableRange = 1.2f;
		float cannonShootForce = 10f;
		
		//Creating ball
		Vec2 ballPosition = new Vec2(2, 6);
		float ballRadius = 0.2f;
		game.createBall(ballPosition, ballRadius);
				
		//Starting polygon
		new BasicRectangle(
				new Vec2(2, 2), 	//position
				new Vec2(2, 0.5f),	//size
				0f,					//angle
				Color.darkGray,		//color 
				game);
		
		//Spike next to pillar
		new SpikeRectangle(
				new Vec2(16f, 1.5f),
				new Vec2(0.4f, 3f),
				game
				);
		
		//Bottom spike
		new SpikeRectangle(
				new Vec2(6f, 0f), 		//position
				new Vec2(20f, 0.4f),	//size
				0f, 					//angle in degrees
				game);
		
		
		//Bottom spike below portal
		new SpikeRectangle(
				new Vec2(21f, playAreaFloorY), 		//position
				new Vec2(10f, 0.4f),					//size
				0f, 									//angle in degrees
				game);
		
		//Cannon
		new ElementCannon(
				new Vec2(6, 4),
				cannonSize,
				cannonInteractableRange,
				cannonShootForce,
				Color.white, 
				game); 
		
		new KeyDoorSet(
				new Vec2(6, 9f),									//key position
				0.15f, 												//key radius
				new Vec2(14.5f, playAreaSize.y / 2),				//door position
				new Vec2(0.4f, playAreaSize.y),						//door size
				Constants.getNextColor(),							//pair color
				game);	
		
		new KeyDoorSet(
				new Vec2(13, 1.5f),									//key position
				0.15f, 												//key radius
				new Vec2(15.5f, playAreaSize.y / 2),				//door position
				new Vec2(0.4f, playAreaSize.y),						//door size
				Constants.getNextColor(),							//pair color
				game);			
		
		//Rectangle next to the pillar
		new BasicRectangle(
				new Vec2(19f, 3f),
				new Vec2(6f, 0.4f),
				game
				);
		
		//Connecting Spike
		new SpikeRectangle(
				new Vec2(22f - 0.2f, 5 + 0.2f),
				new Vec2(0.4f, 4f),
				game
				);
		
		//pillar pair with doors
		createPillarPair(
				15,									//posX
				0.65f, 								//posYNormalized
				2f, 								//gapSpaceY
				2f,									//pillarWidthX
				rectPosition.y,						//pillarMidY
				playAreaSize,						//playAreaSize
				false,								//isTopPillarSpike
				false								//isBottomPillarSpike
				);
		
		new ElementCannon(
				//new Vec2(18, Utilities.lerp(playAreaFloorY, playAreaCeilingY, 0.55f)),
				new Vec2(23.75f, 8.5f), 
				cannonSize,
				cannonInteractableRange,
				cannonShootForce,
				Color.white, 
				game); 
		
		
		float grappleSize = 0.1f;
		float grappleInteractableRange = 1.2f;
		new ElementGrapple(
				new Vec2(18, Utilities.lerp(playAreaFloorY, playAreaCeilingY, 0.73f)),
				//new Vec2(23, 8.5f),
				grappleSize,
				grappleInteractableRange,
				BasicPhysicsEngineUsingBox2D.world, game); 
			
		//Blocking spike wall
		new SpikeRectangle(
				new Vec2(26f, rectPosition.y), 		//position
				new Vec2(0.4f, playAreaSize.y),	//size
				0f, 					//angle in degrees
				game);
		
		new PortalSet(
				new Vec2(19f, 1.2f),													//portal 1 position
				180f,																	//portal 1 angle in degrees
				new Vec2(28f, Utilities.lerp(playAreaFloorY, playAreaCeilingY, 0.8f)),	//portal 2 position
				-90f,																	//portal 2 angle in degrees
				0.3f,																	//portal radius
				BasicPhysicsEngineUsingBox2D.world,										//world object
				game																	//game reference
				);

		createPillarPair(
				30,									//posX
				0.25f, 								//posYNormalized
				1f, 								//gapSpaceY
				0.5f,								//pillarWidthX
				rectPosition.y,						//pillarMidY
				playAreaSize,						//playAreaSize
				false,								//isTopPillarSpike
				false								//isBottomPillarSpike
				);
		
		new ElementCannon(
				//new Vec2(18, Utilities.lerp(playAreaFloorY, playAreaCeilingY, 0.55f)),
				//new Vec2(28, 2.5f),
				new Vec2(28, 2.5f),
				cannonSize,
				cannonInteractableRange,
				cannonShootForce * 4f,
				Color.white, 
				game); 
		
		createPillarPair(
				34,									//posX
				0.70f, 								//posYNormalized
				0.5f, 								//gapSpaceY
				0.4f,									//pillarWidthX
				rectPosition.y,						//pillarMidY
				playAreaSize,						//playAreaSize
				true,								//isTopPillarSpike
				true								//isBottomPillarSpike
				);
		
		//Bottom jump pad
		new JumpPadRectangle(
				new Vec2(32f, 2),							//position 
				new Vec2(1.5f, 0.2f),							//size
				45f,										//angle
				Constants.DEFAULT_JUMP_PAD_RESTITUTION,		//restitution
				Constants.JUMP_PAD_COLOR, 					//color
				game										//game reference
				);	
		
		//Top jump pad
		new JumpPadRectangle(
				new Vec2(31, Utilities.lerp(playAreaFloorY, playAreaCeilingY, 0.73f)),							//position 
				new Vec2(1.5f, 0.2f),							//size
				45f,										//angle
				Constants.DEFAULT_JUMP_PAD_RESTITUTION,		//restitution
				Constants.JUMP_PAD_COLOR, 					//color
				game										//game reference
				);

		new MovingPlatform(
				new Vec2(0.4f, 4), //size
				Color.darkGray,
				new ArrayList<Vec2>(Arrays.asList(		//waypoints list
						new Vec2(36, 8),
						new Vec2(36, 2)
						)),
				1f, 	//duration per waypoint
				0.25f, 	//wait duration per waypoint
				false, 	//is spike platform
				game);
				
		new MovingPlatform(
				new Vec2(0.4f, 2), //size
				Color.darkGray,
				new ArrayList<Vec2>(Arrays.asList(		//waypoints list
						new Vec2(38, 2),
						new Vec2(38, 8)
						)),
				1f, 	//duration per waypoint
				0.25f, 	//wait duration per waypoint
				true, 	//is spike platform
				game);
		
		WinPlatformPolygon winPlatformPolygon = new WinPlatformPolygon(
				new Vec2(40, playAreaSize.y / 2f),
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
	
	
	private void createPillarPair(float posX, float gapPosYNormalized, float gapSpaceY, float pillarWidthX, float playAreaMidY, Vec2 playAreaSize, boolean isTopSpike, boolean isBottomSpike)
	{
		float playAreaTopY = playAreaMidY + (playAreaSize.y / 2f);
		float playAreaBottomY = playAreaMidY - (playAreaSize.y / 2f);
		
		float gapMidPointY = Utilities.lerp(playAreaBottomY, playAreaTopY, gapPosYNormalized);

		if(gapPosYNormalized < (1f - 0.01f))
		{
			Vec2 topRectSize = new Vec2(pillarWidthX, (playAreaTopY - gapMidPointY) - gapSpaceY);
			Vec2 topRectPos = new Vec2(posX, playAreaTopY - (topRectSize.y / 2));
			
			if(isTopSpike)
			{
				new SpikeRectangle(
						topRectPos,
						topRectSize,
						game
						);
			}
			else {
				new BasicRectangle(
						topRectPos,
						topRectSize,
						Color.darkGray,
						game
						);
			}

		}

		if(gapPosYNormalized > 0.01f)
		{
			Vec2 bottomRectSize = new Vec2(pillarWidthX, gapMidPointY - playAreaBottomY - gapSpaceY);
			Vec2 bottomRectPos = new Vec2(posX, playAreaBottomY + (bottomRectSize.y / 2));
			
			if(isBottomSpike)
			{
				new SpikeRectangle(
						bottomRectPos,
						bottomRectSize,
						game
						);
			}
			else {
				new BasicRectangle(
						bottomRectPos,
						bottomRectSize,
						Color.darkGray,
						game
						);
			}

		}
	}
}