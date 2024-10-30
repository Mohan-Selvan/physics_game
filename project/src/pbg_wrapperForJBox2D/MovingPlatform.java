package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class MovingPlatform extends BasicRectangle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public final int SCREEN_RADIUS;
	public final int WAYPOINT_DRAW_RADIUS;
	
	private final Path2D.Float polygonPath;
	private static final float radius = 1f;
	
	//Trackers
	private List<Vec2> wayPoints;
	private int currentWaypointIndex = 0;
	private int targetWaypointIndex = 0;
	private int waypointsTotalCount = 1;
	private float durationPerWaypoint = 1f;
	private float waitDurationPerWaypoint = 0f;
	
	private float timer = 0f;
	private float waitCooldownTimer = 0f;
	
	private BasicPhysicsEngineUsingBox2D game;
	
	public MovingPlatform(Vec2 platformSize, Color platformColor, List<Vec2> wayPoints, float durationPerWaypoint, float waitDurationPerWaypoint, boolean isSpikePlatform, BasicPhysicsEngineUsingBox2D game)
	{
		super(wayPoints.get(0), platformSize, isSpikePlatform ? Constants.SPIKE_COLOR : platformColor, true, game);
		
		this.SCREEN_RADIUS=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(radius),1);
		this.WAYPOINT_DRAW_RADIUS=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(0.1f),1);
		
		this.wayPoints = wayPoints;
		this.polygonPath = Utilities.getPathForRectangle(platformSize);
		
		Vec2 startPosition = wayPoints.get(0);
		this.body.setTransform(startPosition, 0f);
		this.currentWaypointIndex = 0;
		this.targetWaypointIndex = 1;
		this.waypointsTotalCount = wayPoints.size();
		this.durationPerWaypoint = durationPerWaypoint;
		this.waitDurationPerWaypoint = waitDurationPerWaypoint;
		
		this.body.setType(BodyType.KINEMATIC);
		
		this.game = game;
		
		game.polygons.add(this);
		game.movingPlatforms.add(this);
		
		if(isSpikePlatform)
		{
			this.body.getFixtureList().setUserData(Constants.getSpikeFixtureUserData());
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		super.draw(g);
		
//		g.setColor(col);
//		Vec2 position = body.getPosition();
//		float angle = body.getAngle(); 
//		AffineTransform af = new AffineTransform();
//		af.translate(BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(position.x), BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(position.y));
//		af.scale(ratioOfScreenScaleToWorldScale, -ratioOfScreenScaleToWorldScale);// there is a minus in here because screenworld is flipped upsidedown compared to physics world
//		af.rotate(angle); 
//		Path2D.Float p = new Path2D.Float (polygonPath,af);
//		g.fill(p);
		
		if(game.IsDebugDrawEnabled)
		{	
			for(Vec2 wp : this.wayPoints)
			{
				int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(wp.x);
				int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(wp.y);
				g.setColor(Utilities.getColorWithAlpha(Color.pink, 0.02f));
				g.fillOval(x - WAYPOINT_DRAW_RADIUS, y - WAYPOINT_DRAW_RADIUS, 2 * WAYPOINT_DRAW_RADIUS, 2 * WAYPOINT_DRAW_RADIUS);
				
				g.setColor(Constants.BORDER_COLOR);
				g.drawOval(x - WAYPOINT_DRAW_RADIUS, y - WAYPOINT_DRAW_RADIUS, 2 * WAYPOINT_DRAW_RADIUS, 2 * WAYPOINT_DRAW_RADIUS);
			}
		}
	}
	
	@Override
	public void notificationOfNewTimestep() {
		
	}
	
	public void tick(float dt)
	{	
		if(waitCooldownTimer > 0f)
		{
			waitCooldownTimer -= dt;
			return;
		}
		
		timer = (timer + (dt * (1f / durationPerWaypoint)));
		
		Vec2 currentPosition = this.body.getPosition();
		Vec2 targetWaypointPosition = this.wayPoints.get(targetWaypointIndex);
		
		if(Utilities.getDistanceBetweenVectors(currentPosition, targetWaypointPosition) < 0.01f)
		{
			currentWaypointIndex = targetWaypointIndex;
			targetWaypointIndex = (targetWaypointIndex + 1) % waypointsTotalCount;
			
			timer = 0f;
			waitCooldownTimer = waitDurationPerWaypoint;
		}
		
		Vec2 targetPosition = Utilities.lerpVector(wayPoints.get(currentWaypointIndex), wayPoints.get(targetWaypointIndex), timer);
				
		this.body.setTransform(targetPosition, 0f);
	}
}


