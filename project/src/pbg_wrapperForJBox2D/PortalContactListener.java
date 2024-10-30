package pbg_wrapperForJBox2D;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

class PortalContactListener implements ContactListener
{
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	@Override
	public void beginContact(Contact contact) {
		
		Fixture f1 = contact.getFixtureA();
		Fixture f2 = contact.getFixtureB();

		if(f1.getUserData() == null && f2.getUserData() == null)
		{
			return;
		}
		
		Object f1UserData = f1.getUserData();
		Object f2UserData = f2.getUserData();
		
		PortalUserData portalData = null;
		BallUserData ballData = null;

		if((f1UserData instanceof PortalUserData) && (f2UserData instanceof BallUserData))
		{
			portalData = (PortalUserData) f1UserData;
			ballData = (BallUserData) f2UserData;
		}
		else if((f2UserData instanceof PortalUserData) && (f1UserData instanceof BallUserData))
		{
			portalData = (PortalUserData) f2UserData;
			ballData = (BallUserData) f1UserData;
		}

		if(portalData == null || ballData == null) {
			
			return;
		}
		
		Portal connectedPortal = portalData.connectedPortal;
		
		BallParticle ball = ballData.ballParticle; 
		Vec2 outPosition = connectedPortal.getOutPosition();
		Vec2 outDirection = connectedPortal.getOutDirection();
		float velocityMagnitude = ball.body.getLinearVelocity().length();
		
		ball.MoveToPositionNextFrame(outPosition, outDirection.mul(velocityMagnitude));
		
		System.out.println("Portal collision : (" + portalData.portalId + " -> " + connectedPortal.GetPortalID() + ")");
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
}
