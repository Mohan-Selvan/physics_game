package pbg_wrapperForJBox2D;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

class KeyContactListener implements ContactListener
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
		
		KeyUserData keyData = null;
		BallUserData ballData = null;

		if((f1UserData instanceof KeyUserData) && (f2UserData instanceof BallUserData))
		{
			keyData = (KeyUserData) f1UserData;
			ballData = (BallUserData) f2UserData;
		}
		else if((f2UserData instanceof KeyUserData) && (f1UserData instanceof BallUserData))
		{
			keyData = (KeyUserData) f2UserData;
			ballData = (BallUserData) f1UserData;
		}

		if(keyData == null || ballData == null) {
			
			return;
		}
		
		BallParticle ball = ballData.ballParticle;
		
		//NOTE :: Disabling key and door
		keyData.keyDoorSet.disableKeyDoor();	
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
