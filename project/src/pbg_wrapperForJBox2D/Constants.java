package pbg_wrapperForJBox2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Constants {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */

	//Collections
	public static final List<ILevel> Levels= new ArrayList<ILevel>(
		Arrays.asList(
				//new LevelDisplay(),
				new Level_1(),
				new Level_2(),
				new Level_3(),
				new Level_FlappyBird()
		)
	);
	
	private static int cyclicColorIndex = 0;
	private static final List<Color> cyclicColorsList = new ArrayList<Color>(Arrays.asList(
			new Color(100, 200, 200, 170),
			 new Color(175, 108, 200, 170)
			));
	
	//App settings
	public static final boolean AUTO_MAXIMIZE_WINDOW = true;
	
	//Physics fixes
	public static float BALL_VELOCITY_MAX_THRESHOLD = 20f;
	public static boolean ENABLE_BALL_FRICTION = false; //Ball friction is disabled, set this variable to true to enable it, but please note, enabling friction degrades player experience due to frictional movements
	
	
	//Game settings
	public static boolean DRAW_SPRITES = true;
	public static boolean DRAW_BORDERS = true;
	
	//Ball properties and visuals
	public static boolean DRAW_BALL_TRAIL = true;
	public static boolean DRAW_BALL_TRAIL_ONLY_WHEN_TIME_SLOW = false;
	public static int BALL_TRAIL_LENGTH = 6;
	public static float BALL_TRAIL_UPDATE_DELAY = 0.04f;

	public static Color BALL_COLOR_1 = Color.magenta;
	public static Color BALL_COLOR_2 = Utilities.getColorWithAlpha(Color.magenta, 0.6f);

	//Camera properties
	public static float CAMERA_LOOK_AHEAD_DISTANCE = 2f;
	public static float CAMERA_FOLLOW_SPEED = 0.03f;
	public static float CAMERA_FOLLOW_OFFSET_X_NORMALIZED = 0.35f;

	//World element Colors
	public static Color SPIKE_COLOR = Color.red;
	public static Color BORDER_COLOR = Color.white;
	public static Color WALL_COLOR = Color.darkGray;
	public static Color JUMP_PAD_COLOR = Color.yellow;
	public static Color CANNON_COLOR = Color.green;
	public static Color GRAPPLE_COLOR = Color.cyan;
	public static Color ATTRACTOR_COLOR = Color.blue;
	public static Color ACTIVE_COLOR = Color.white;
	
	//Interactable element properties
	public static float DEFAULT_JUMP_PAD_RESTITUTION = 1.5f;
		
	//Ability properties
	public static float TIME_SLOW_ABILITY_MAX_TIME = 3f;
	public static float TIME_SLOW_ABILITY_RECHARGE_COOL_DOWN = 1f;
	public static float TIME_SLOW_ABILITY_RECHARGE_SPEED = 3f;
	public static float TIME_SLOW_ABILITY_MIN_THRESHOLD_NORMALIZED = 0.3f;
	
	//Drawing elements
	public static final BasicStroke borderStroke = new BasicStroke(2f);
	public static final BasicStroke normalStroke = new BasicStroke(1f);
	public static final BasicStroke thinStroke = new BasicStroke(0.5f);

	
	// DO NOT CHANAGE BELOW VARIABLES
	public static int BALL_ID = 1;
	public static int SPIKE_ID = 2;
	public static int WIN_PLATFORM_ID = 3;
	public static int INFINITE_BALL_CHARGES = -1;
	
	public static int getMaxNumberOfLevels()
	{
		return Levels.size();
	}
	
	public static ILevel getLevel(int levelId)
	{
		try 
		{
			int levelIndex = levelId - 1;
			
			if(levelIndex < 0 || levelIndex >= getMaxNumberOfLevels())
			{ 
				return null;
			}
			
			return Levels.get(levelIndex);
		}
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("Error : Invalid level ID : " + levelId);
			return null;
		}
	}
	
	public static float getBallVelocityMaxThresholdSquared()
	{
		return BALL_VELOCITY_MAX_THRESHOLD * BALL_VELOCITY_MAX_THRESHOLD;
	}
	
	public static BasicStroke getBorderStroke()
	{
		return borderStroke;
	}
	
	public static BasicStroke getNormalStroke()
	{
		return normalStroke;
	}
	
	public static BasicStroke getThinStroke()
	{
		return thinStroke;
	}
	
	public static ILevel getDefaultLevel()
	{
		return getLevel(1);
	}
    
	public static int getSpikeFixtureUserData()
	{
		return (int) SPIKE_ID;
	}
	
	public static int getWinPlatformFixtureUserData()
	{
		return (int) WIN_PLATFORM_ID;
	}
	
	public static Color getNextColor()
	{
		Color col = cyclicColorsList.get(cyclicColorIndex);
		
		cyclicColorIndex = cyclicColorIndex + 1;
		if(cyclicColorIndex > (cyclicColorsList.size() - 1))
		{
			cyclicColorIndex = 0;
		}
		
		return col;
	}
}
