package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class PortalSet {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private static int CurrentPortalID = 1;
	
	public static int GetNextPortalSensorID()
	{
		return CurrentPortalID++;
	}

	private Portal portal1;
	private Portal portal2;

	public PortalSet(Vec2 spawnPosition1, float portal1OutAngle, Vec2 spawnPosition2, float portal2OutAngle, float radius, World world, BasicPhysicsEngineUsingBox2D game) {
		
		portal1 = new Portal(spawnPosition1, radius, portal1OutAngle, Color.orange);
		portal2 = new Portal(spawnPosition2, radius, portal2OutAngle, Color.blue);
		
		PortalUserData fixtureUserDataPortal1 = new PortalUserData();
		fixtureUserDataPortal1.portalId = GetNextPortalSensorID();
		fixtureUserDataPortal1.portalSet = this;
		fixtureUserDataPortal1.connectedPortal = portal2;
		
		PortalUserData fixtureUserDataPortal2 = new PortalUserData();
		fixtureUserDataPortal2.portalId = GetNextPortalSensorID();
		fixtureUserDataPortal2.portalSet = this;
		fixtureUserDataPortal2.connectedPortal = portal1;
		
		portal1.SetFixtureUserData(fixtureUserDataPortal1);
		portal2.SetFixtureUserData(fixtureUserDataPortal2);

		game.particles.add(portal1);
		game.particles.add(portal2);
	}
}