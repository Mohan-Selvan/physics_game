package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import org.jbox2d.common.Vec2;

public class Level_FlappyBird implements ILevel {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public BasicPhysicsEngineUsingBox2D game;
	
	private Image worldBgSprite = null;
	
	@Override
	public int getNumberOfCharges() {
		return Constants.INFINITE_BALL_CHARGES;
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
		
		int numberOfPillarPairs = 10;
		float posX = 5;
		Vec2 pillarWidthRange = new Vec2(0.8f, 1f);
		float distanceBetweenPillarsX = 2;
		
		float playAreaWallWidth = 10f;
		Vec2 playAreaSize = new Vec2(
				(numberOfPillarPairs * pillarWidthRange.y * distanceBetweenPillarsX) + posX + 50f,
				10);
		Vec2 playAreaPosition = new Vec2((playAreaSize.x /2f) - 0.008f, (playAreaSize.y/2));
		
		Vec2 ballPosition = new Vec2(2, 6);
		float ballRadius = 0.2f;
		game.createBall(ballPosition, ballRadius);
				
		//starting platform
		new BasicRectangle(
				new Vec2(2, 2), 
				new Vec2(2, 0.2f),
				0f,
				Color.darkGray, game);
		
		//top wall
		new SpikeRectangle(
				playAreaPosition.add(new Vec2(0, 1).mul(playAreaSize.y / 2)),	//position
				new Vec2(playAreaSize.x, 0.2f), 	//size
				0f, 								//angle
				Constants.SPIKE_COLOR,				//color
				false,								//drawBorders,
				Constants.BORDER_COLOR,				//border color
				game
				);
		//bottom wall
		new SpikeRectangle(
				playAreaPosition.add(new Vec2(0, -1).mul(playAreaSize.y / 2)),	//position
				new Vec2(playAreaSize.x, 0.2f), 	//size
				0f, 								//angle
				Constants.SPIKE_COLOR,				//color
				false,								//drawBorders,
				Constants.BORDER_COLOR,				//border color
				game
				);
		
		//pillars
		for(int i = 0; i < numberOfPillarPairs; i++)
		{
			createPillarPair(
					posX,																//posX
					Utilities.getRandomFloatNormalised(), 								//posYNormalized
					Utilities.getRandomFloat(1f, 2f), 									//gapSpaceY
					Utilities.getRandomFloat(pillarWidthRange.x, pillarWidthRange.y),	//pillarWidthX
					playAreaPosition.y,													//pillarMidY
					playAreaSize,														//playAreaSize
					Utilities.getRandomBool(),											//isTopPillarSpike
					Utilities.getRandomBool()											//isBottomPillarSpike
					);
			
			posX += (distanceBetweenPillarsX + (pillarWidthRange.y * 2));
		}
		
		if(playAreaSize.x < (numberOfPillarPairs * distanceBetweenPillarsX * pillarWidthRange.y))
		{
			System.out.println("Play area size X is not enough!");
		}
		
		WinPlatformPolygon winPlatformPolygon = new WinPlatformPolygon(
				new Vec2(posX, playAreaSize.y / 2f),
				new Vec2(1, playAreaSize.y),
				Color.green,
				game);
				
		new ConcentricRectanglePolygon(
				playAreaPosition,				//position
				playAreaSize,				//size
				playAreaWallWidth,					//skin width
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
