package pbg_wrapperForJBox2D;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

class WinPlatformContactListener implements ContactListener
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
		
		int winPlatformData = -1;
		BallUserData ballData = null;

		if((f1UserData instanceof Integer) && (f2UserData instanceof BallUserData))
		{
			winPlatformData = (Integer) f1UserData;
			ballData = (BallUserData) f2UserData;
		}
		else if((f2UserData instanceof Integer) && (f1UserData instanceof BallUserData))
		{
			winPlatformData = (Integer) f2UserData;
			ballData = (BallUserData) f1UserData;
		}

		if(winPlatformData < 0 || ballData == null) {
			
			return;
		}
		
		if(winPlatformData == Constants.WIN_PLATFORM_ID)
		{
			ballData.ballParticle.HandleBallTouchedWinPlatform();
		}
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