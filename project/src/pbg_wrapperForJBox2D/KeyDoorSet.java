package pbg_wrapperForJBox2D;

import java.awt.Color;

import org.jbox2d.common.Vec2;

public class KeyDoorSet {	
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	//Unique ID generator
	public static int CurrentKeyID = 0; 	
	public static int getUniqueKeyID()
	{
		return CurrentKeyID++;
	}
	
	
	//References
	private Door door = null;
	private Key key = null;
	
	//Constructor
	public KeyDoorSet(Vec2 keyPosition, float keyRadius, Vec2 doorPosition, Vec2 doorSize, Color color, BasicPhysicsEngineUsingBox2D game)
	{
		door = new Door(doorPosition, doorSize, color, game);
		key = new Key(
				getUniqueKeyID(),
				keyPosition,
				keyRadius,
				color,
				game);

		KeyUserData keyUserData = new KeyUserData();
		keyUserData.keyDoorSet = this;
		
		key.setKeyFixtureUserData(keyUserData);
	}
	
	public void disableKeyDoor()
	{
		System.out.println("Disabling Key Door set : " + key.getKeyId());
		this.key.disableKey();
		this.door.disableDoor();
	}
}
