package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.MouseJoint;


public class BasicPhysicsEngineUsingBox2D {
	/* Author: Moganaselvan Ramamoorthy
	 * Based on: Michael Fairbank's BasicPhysicsEngineUsingBox2D class
	 */
	
	// frame dimensions
	public static final int SCREEN_HEIGHT = 680;
	public static final int SCREEN_WIDTH = 640;
	public static final Dimension FRAME_SIZE = new Dimension(
			SCREEN_WIDTH, SCREEN_HEIGHT);
	public static final float WORLD_WIDTH=10;//metres
	public static final float WORLD_HEIGHT=SCREEN_HEIGHT*(WORLD_WIDTH/SCREEN_WIDTH);// meters - keeps world dimensions in same aspect ratio as screen dimensions, so that circles get transformed into circles as opposed to ovals
	public static final float GRAVITY=9.8f;
	public static final boolean ALLOW_MOUSE_POINTER_TO_DRAG_BODIES_ON_SCREEN=false;// There's a load of code in basic mouse listener to process this, if you set it to true

	public static World world; // Box2D container for all bodies and barriers 

	// sleep time between two drawn frames in milliseconds 
	public static final int DELAY = 10;
	public static final int NUM_EULER_UPDATES_PER_SCREEN_REFRESH=10;
	// estimate for time between two frames in seconds 
	public static float deltaT = DELAY / 1000.0f;
	public static final float NORMAL_DELTA_T = DELAY / 1000.0f;
	public static final float SLOWED_DELTA_T = DELAY / 10000f;
	
	public static int convertWorldXtoScreenX(float worldX) {
		return (int) (worldX/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static int convertWorldYtoScreenY(float worldY) {
		// minus sign in here is because screen coordinates are upside down.
		return (int) (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT));
	}
	public static float convertWorldLengthToScreenLength(float worldLength) {
		return (worldLength/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static float convertScreenXtoWorldX(int screenX) {
		return screenX*WORLD_WIDTH/SCREEN_WIDTH;
	}
	public static float convertScreenYtoWorldY(int screenY) {
		return (SCREEN_HEIGHT-screenY)*WORLD_HEIGHT/SCREEN_HEIGHT;
	}
	
	public static Vec2 convertWorldPositionToScreenPosition(Vec2 worldPosition)
	{
		return new Vec2(
				convertWorldXtoScreenX(worldPosition.x),
				convertWorldYtoScreenY(worldPosition.y)
				);
	}
		
	public List<BasicParticle> particles;
	public List<BasicPolygon> polygons;
	public List<AnchoredBarrier> barriers;
	public List<MovingPlatform> movingPlatforms;
	public List<ICompoundElement> compoundElements;

	private static BallParticle ballParticle = null;
	public float gameTimer = 0f;
	
	//Time slow ability
	private float timeSlowAbilityTimer = 0f;
	private float timeSlowAbilityCooldownTimer = 0f;
	private final float timeSlowAbilityMaxTime = Constants.TIME_SLOW_ABILITY_MAX_TIME;
	private final float timeSlowAbilityRechargeCooldown = Constants.TIME_SLOW_ABILITY_RECHARGE_COOL_DOWN;
	private final float timeSlowAbilityRechargeSpeed = Constants.TIME_SLOW_ABILITY_RECHARGE_SPEED;
	
	private boolean isTimeSlowAbilityActive = false;
	
	// Game properties
	private boolean isTimeSlowedDown;
	private boolean wasTimeSlowedDownLastFrame;
	
	//References
	private ILevel level;
	
	public BasicPhysicsEngineUsingBox2D(ILevel level) {
		
		this.level = level;
		
		initializeGame();
		
		InputListener.resetFlags();
		
		this.timeSlowAbilityTimer = this.timeSlowAbilityMaxTime;
		this.timeSlowAbilityCooldownTimer = 0f;
		
		isTimeSlowedDown = false;
		wasTimeSlowedDownLastFrame = false;
	}

	private void startThread(final BasicView view) throws InterruptedException {
		final BasicPhysicsEngineUsingBox2D game=this;
		while (true) {
			game.update();
			view.repaint();
			Toolkit.getDefaultToolkit().sync();
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
			}
		}
	}

	public void update() {
		int VELOCITY_ITERATIONS=NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
		int POSITION_ITERATIONS=NUM_EULER_UPDATES_PER_SCREEN_REFRESH;
		for (BasicParticle p:particles) {
			// give the objects an opportunity to add any bespoke forces, e.g. rolling friction
			p.notificationOfNewTimestep();
		}
		for (BasicPolygon p:polygons) {
			// give the objects an opportunity to add any bespoke forces, e.g. rolling friction
			p.notificationOfNewTimestep();
		}
		
		InputListener.tick();
		
		boolean isTimeSlowAbilityDesired = InputListener.isYKeyPressed(); 
		boolean canActivateTimeSlowAbility = timeSlowAbilityTimer > 0f 
			&& !isBallLocked() && getTimeSlowAbilityLeftNormalised() > Constants.TIME_SLOW_ABILITY_MIN_THRESHOLD_NORMALIZED;
				
		isTimeSlowAbilityActive = isTimeSlowAbilityDesired 
				&& ((isTimeSlowAbilityActive && timeSlowAbilityTimer > 0f) || (!isTimeSlowAbilityActive && canActivateTimeSlowAbility));		
				
		boolean isChargeDesiredThisFrame = InputListener.isXKeyPressed();
		boolean canBallCharge = getBall().canCharge() && !isBallLocked();
		
		isTimeSlowedDown = isTimeSlowAbilityActive || (isChargeDesiredThisFrame && canBallCharge);
		deltaT = isTimeSlowedDown ? SLOWED_DELTA_T : NORMAL_DELTA_T;
		world.step(deltaT, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		tick(deltaT);
		
		wasTimeSlowedDownLastFrame = isTimeSlowedDown;
	}
	
	public List<InteractableElement> elements = null;
	private InteractableElement previousElement = null;
	public InteractableElement ActiveElement = null;
	
	public static boolean IsDebugDrawEnabled = false;
	
	private void initializeGame()
	{
		world = new World(new Vec2(0, -GRAVITY));// create Box2D container for everything
		world.setContinuousPhysics(true);
		
		particles = new ArrayList<BasicParticle>();
		polygons = new ArrayList<BasicPolygon>();
		barriers = new ArrayList<AnchoredBarrier>();
		elements = new ArrayList<InteractableElement>();
		movingPlatforms = new ArrayList<MovingPlatform>();
		compoundElements = new ArrayList<ICompoundElement>();
		
		GameContactListener gameContactListener = new GameContactListener(
				new ArrayList<ContactListener>(
						Arrays.asList(
								new WinPlatformContactListener(),	
								new SpikeContactListener(), 		//Red wall
								new PortalContactListener(),
								new KeyContactListener(),
								new JumpPadContactListener()
								)
						)
				);

		world.setContactListener(gameContactListener);
		
		this.level.buildLevel(this);;
	}
	
	public void createBall(Vec2 ballPosition, float radius)
	{
		//Ball
		this.ballParticle = new BallParticle(
				ballPosition,								//position
				radius,  									//radius
				this.getActiveLevel().getNumberOfCharges(), // number of charges (determined by the level)
				this
		);
		
		BallUserData ballUserData = new BallUserData(
				Constants.BALL_ID, 	//Unique ball ID 
				ballParticle		//Reference to the ball particle
				);
		
		ballParticle.SetFixtureUserData(ballUserData);	
		particles.add(ballParticle);
	}
	
	private void tick(float dt)
	{		
		gameTimer += dt;
		
		//Game conditions
		if(InputListener.isZKeyDown()) //Restart level condition
		{
			ThreadedGuiForPhysicsEngine.RestartLevel();
		}
		
		if(InputListener.isAKeyDown())
		{
			this.toggleDebugDraw();
		}

		//Time slow ability
		if(isTimeSlowAbilityActive && timeSlowAbilityTimer > 0f)
		{
			timeSlowAbilityTimer -= NORMAL_DELTA_T;
			timeSlowAbilityCooldownTimer = timeSlowAbilityRechargeCooldown;
		}
		else
		{
			 if(timeSlowAbilityCooldownTimer > 0f)
			 {
				 timeSlowAbilityCooldownTimer -= dt;
			 }
			 else if(timeSlowAbilityTimer < timeSlowAbilityMaxTime)
			 {
				timeSlowAbilityTimer = Utilities.clamp(
						0f, 														//min
						timeSlowAbilityMaxTime, 									//max
						timeSlowAbilityTimer + timeSlowAbilityRechargeSpeed * dt	//value
						);
			 }
		}
		
		if(ballParticle != null)
		{
			ballParticle.Tick(dt);
		}
		
		for (MovingPlatform platform : movingPlatforms)
		{
			platform.tick(dt);
		}
		
		if(ActiveElement != null)
		{
			ActiveElement.processInput();
		}
			
			
		if(ActiveElement != null && ActiveElement.isLocked()) {
			return;
		}
		
		//InteractableElement closestElement = getClosestInteractableElementToBody(ballParticle.body);			
		InteractableElement closestElement = getClosestElementToBody(ballParticle.body);
		if(closestElement != this.ActiveElement)
		{
			previousElement = ActiveElement;
			
			if(previousElement != null)
			{				
				previousElement.resetElement();
			}
			
			
			ActiveElement = closestElement;
		}
	}
	
	public boolean isBallLocked()
	{
		return ActiveElement != null && ActiveElement.isLocked();
	}
	
	private InteractableElement getClosestInteractableElementToBody(Body targetBody)
	{
		InteractableElement closestElement = null;
		float closestDistance = Float.MAX_VALUE;

		for(InteractableElement e : elements)
		{
			float distance = Utilities.getDistanceBetweenVectors(e.getBody().getPosition(), targetBody.getPosition());
			if(distance <= e.getInteractionRange())
			{
				if(distance < closestDistance)
				{
					closestElement = e;
					closestDistance = distance;
				}
			}
		}
		
		return closestElement;
	}
	
	private InteractableElement getClosestElementToBody(Body targetBody)
	{
		InteractableElement closestElement = null;
		float closestDistance = Float.MAX_VALUE;

		for(InteractableElement e : elements)
		{
			float distance = Utilities.getDistanceBetweenVectors(e.getBody().getPosition(), targetBody.getPosition());
			if(distance < closestDistance)
			{
				closestElement = e;
				closestDistance = distance;
			}
		}
		
		return closestElement;
	}

	public static BallParticle getBall()
	{
		return ballParticle;
	}
	
	public ILevel getActiveLevel()
	{
		return level;
	}
	
	public float getTimeSlowAbilityLeftNormalised()
	{
		if(timeSlowAbilityMaxTime > 0f)
		{			
			return Utilities.clamp01(timeSlowAbilityTimer / timeSlowAbilityMaxTime);
		}
		else {
			return 0f;
		}
	}
	
	public void handleBallDied()
	{
		ThreadedGuiForPhysicsEngine.HandleBallDied();
	}
	
	public void handleBallTouchedWinPlatform()
	{
		ThreadedGuiForPhysicsEngine.HandleBallCompletedLevel();
	}
	
	public void toggleDebugDraw()
	{
		BasicPhysicsEngineUsingBox2D.IsDebugDrawEnabled = !BasicPhysicsEngineUsingBox2D.IsDebugDrawEnabled;
	}
	
	public boolean getIsTimeSlowedDown()
	{
		return isTimeSlowedDown;
	}
	
	public boolean getWasTimeSlowedDownLastFrame()
	{
		return wasTimeSlowedDownLastFrame;
	}
}


