package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class Portal extends BasicParticle {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private final int OUT_POINT_RADIUS;
	
	private Vec2 outDirection = new Vec2(1f, 0f);
	
	public Portal(Vec2 spawnPosition, float radius, float outAngle, Color color)
	{
		super(spawnPosition, radius, color, 1f, 1f);

		this.body.setType(BodyType.KINEMATIC);
		this.body.getFixtureList().setSensor(true);
		
		float outPointRadius = 0.1f;
		this.outDirection = Utilities.getDirectionFromAngle(outAngle);
		
		this.OUT_POINT_RADIUS=(int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(outPointRadius),1);
	}
	
	public void SetFixtureUserData(PortalUserData fixtureUserData)
	{
		this.body.getFixtureList().setUserData(fixtureUserData);
	}
	
	public int GetPortalID()
	{
		PortalUserData userData = (PortalUserData) this.body.getFixtureList().getUserData();
		
		if(userData == null)
		{
			return -1;
		}
		
		return userData.portalId;
	}
	
	public Vec2 getOutPosition()
	{
		return this.body.getPosition().add(outDirection);
	}
	
	public Vec2 getOutDirection()
	{
		Vec2 direction = getOutPosition().sub(this.body.getPosition());
		direction.normalize();
		return direction;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		Vec2 outPosition = getOutPosition();
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(outPosition.x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(outPosition.y);
		
		g.setColor(Utilities.getColorWithAlpha(col, 0.5f));
		g.fillOval(x - OUT_POINT_RADIUS, y - OUT_POINT_RADIUS, 2 * OUT_POINT_RADIUS, 2 * OUT_POINT_RADIUS);
		
	}
}
