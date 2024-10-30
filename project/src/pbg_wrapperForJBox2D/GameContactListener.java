package pbg_wrapperForJBox2D;

import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class GameContactListener implements ContactListener {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */

	List<ContactListener> contactListeners;
	
	public GameContactListener(List<ContactListener> contactListeners)
	{
		this.contactListeners = contactListeners;
	}

	@Override
	public void beginContact(Contact contact) {
	
		for(ContactListener c : this.contactListeners)
		{
			c.beginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact) {
		
		for(ContactListener c : this.contactListeners)
		{
			c.endContact(contact);
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
		for(ContactListener c : this.contactListeners)
		{
			c.preSolve(contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		for(ContactListener c : this.contactListeners)
		{
			c.postSolve(contact, impulse);
		}	
	}

		
}
