package pbg_wrapperForJBox2D;

import java.lang.annotation.Target;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

class JumpPadContactListener implements ContactListener
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
		
		JumpPadUserData jumpPadData = null;
		BallUserData ballData = null;

		if((f1UserData instanceof JumpPadUserData) && (f2UserData instanceof BallUserData))
		{
			jumpPadData = (JumpPadUserData) f1UserData;
			ballData = (BallUserData) f2UserData;
		}
		else if((f2UserData instanceof JumpPadUserData) && (f1UserData instanceof BallUserData))
		{
			jumpPadData = (JumpPadUserData) f2UserData;
			ballData = (BallUserData) f1UserData;
		}

		if(jumpPadData == null || ballData == null) {
			
			return;
		}
		
		//Applying restituted velocity
		BallParticle ball = ballData.ballParticle;
		Vec2 originalVelocity = ball.body.getLinearVelocity().clone();
		System.out.println("jump pad data res : " + jumpPadData.restitution);
		Vec2 targetVelocity = originalVelocity.mul(jumpPadData.restitution);
		ball.setLinearVelocity(targetVelocity);

		System.out.println("Applying restitution, Original velocity : " + originalVelocity + "  Modified velocity : " + targetVelocity);
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
