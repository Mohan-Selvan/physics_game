package pbg_wrapperForJBox2D;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.jbox2d.common.Vec2;

public class InputListener extends KeyAdapter {

	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	private static boolean spaceKeyPressed, upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed;
	
	private static boolean spaceKeyFlag;
	private static boolean spaceKeyDown, spaceKeyReleased;
	
	private static boolean upKeyFlag;
	private static boolean upKeyDown, upKeyReleased;
	
	private static boolean downKeyFlag;
	private static boolean downKeyDown, downKeyReleased;
	
	private static boolean leftKeyFlag;
	private static boolean leftKeyDown, leftKeyReleased;
	
	private static boolean rightKeyFlag;
	private static boolean rightKeyDown, rightKeyReleased;
	
	private static final int xKeyEvent = KeyEvent.VK_X;
	private static boolean xKeyPressed;
	private static boolean xKeyFlag;
	private static boolean xKeyDown, xKeyReleased;
	
	private static final int yKeyEvent = KeyEvent.VK_C;
	private static boolean yKeyPressed;
	private static boolean yKeyFlag;
	private static boolean yKeyDown, yKeyReleased;
	
	private static final int zKeyEvent = KeyEvent.VK_R;
	private static boolean zKeyPressed;
	private static boolean zKeyFlag;
	private static boolean zKeyDown, zKeyReleased;
	
	private static final int aKeyEvent = KeyEvent.VK_T;
	private static boolean aKeyPressed;
	private static boolean aKeyFlag;
	private static boolean aKeyDown, aKeyReleased;


	public static boolean isRightKeyPressed() {
		return rightKeyPressed;
	}

	public static boolean isLeftKeyPressed() {
		return leftKeyPressed;
	}

	public static boolean isUpKeyPressed() {
		return upKeyPressed;
	}
	
	public static boolean isDownKeyPressed() {
		return downKeyPressed;
	}
	
	public static boolean isSpaceKeyPressed()
	{
		return spaceKeyPressed;
	}
	
	public static boolean isXKeyPressed()
	{
		return xKeyPressed;
	}
	
	public static boolean isYKeyPressed()
	{
		return yKeyPressed;
	}
	
	public static boolean isZKeyPressed()
	{
		return zKeyPressed;
	}
	
	public static boolean isAKeyPressed()
	{
		return aKeyPressed;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			upKeyPressed=true;
			break;
		case KeyEvent.VK_DOWN:
			downKeyPressed=true;
			break;	
		case KeyEvent.VK_LEFT:
			leftKeyPressed=true;
			break;
		case KeyEvent.VK_RIGHT:
			rightKeyPressed=true;
			break;
		case KeyEvent.VK_SPACE:
			spaceKeyPressed = true;
			break;
		case xKeyEvent:
			xKeyPressed = true;
			break;
		case yKeyEvent:
			yKeyPressed = true;
			break;
		case zKeyEvent:
			zKeyPressed = true;
			break;
		case aKeyEvent:
			aKeyPressed = true;
			break;		
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			upKeyPressed=false;
			break;
		case KeyEvent.VK_DOWN:
			downKeyPressed=false;
			break;	
		case KeyEvent.VK_LEFT:
			leftKeyPressed=false;
			break;
		case KeyEvent.VK_RIGHT:
			rightKeyPressed=false;
			break;
		case KeyEvent.VK_SPACE:
			spaceKeyPressed = false;
			break;
		case xKeyEvent:
			xKeyPressed = false;
			break;
		case yKeyEvent:
			yKeyPressed = false;
			break;
		case zKeyEvent:
			zKeyPressed = false;
			break;
		case aKeyEvent:
			aKeyPressed = false;
			break;
		}
	}
	

	public static boolean isSpaceKeyDown()
	{
		return spaceKeyDown;
	}
	
	public static boolean isSpaceKeyReleased()
	{
		return spaceKeyReleased;
	}
	
	public static boolean isUpKeyDown()
	{
		return upKeyDown;
	}
	
	public static boolean isUpKeyReleased()
	{
		return upKeyReleased;
	}
	
	public static boolean isDownKeyDown()
	{
		return downKeyDown;
	}
	
	public static boolean isDownKeyReleased()
	{
		return downKeyReleased;
	}
	
	public static boolean isLeftKeyDown()
	{
		return leftKeyDown;
	}
	
	public static boolean isLeftKeyReleased()
	{
		return leftKeyReleased;
	}
	
	public static boolean isRightKeyDown()
	{
		return rightKeyDown;
	}
	
	public static boolean isLefRightKeyReleased()
	{
		return rightKeyReleased;
	}
	
	public static boolean isXKeyDown()
	{
		return xKeyDown;
	}
	
	public static boolean isXKeyReleased()
	{
		return xKeyReleased;
	}
	
	public static boolean isYKeyDown()
	{
		return yKeyDown;
	}
	
	public static boolean isYKeyReleased()
	{
		return yKeyReleased;
	}
	
	public static boolean isZKeyDown()
	{
		return zKeyDown;
	}
	
	public static boolean isZKeyReleased()
	{
		return zKeyReleased;
	}
	
	public static boolean isAKeyDown()
	{
		return aKeyDown;
	}
	
	public static boolean isAKeyReleased()
	{
		return aKeyReleased;
	}
	
	public static void tick()
	{		
		spaceKeyDown = !spaceKeyFlag && spaceKeyPressed;
		spaceKeyReleased = spaceKeyFlag && !spaceKeyPressed;
		spaceKeyFlag = spaceKeyPressed;
		
		upKeyDown = !upKeyFlag && upKeyPressed;
		upKeyReleased = upKeyFlag && !upKeyPressed;
		upKeyFlag = upKeyPressed;
		
		downKeyDown = !downKeyFlag && downKeyPressed;
		downKeyReleased = downKeyFlag && !downKeyPressed;
		downKeyFlag = downKeyPressed;
		
		leftKeyDown = !leftKeyFlag && leftKeyPressed;
		leftKeyReleased = leftKeyFlag && !leftKeyPressed;
		leftKeyFlag = leftKeyPressed;
		
		rightKeyDown = !rightKeyFlag && rightKeyPressed;
		rightKeyReleased = rightKeyFlag && !rightKeyPressed;
		rightKeyFlag = rightKeyPressed;
		
		xKeyDown = !xKeyFlag && xKeyPressed;
		xKeyReleased = xKeyFlag && !xKeyPressed;
		xKeyFlag = xKeyPressed;
		
		yKeyDown = !yKeyFlag && yKeyPressed;
		yKeyReleased = yKeyFlag && !yKeyPressed;
		yKeyFlag = yKeyPressed;
		
		zKeyDown = !zKeyFlag && zKeyPressed;
		zKeyReleased = zKeyFlag && !zKeyPressed;
		zKeyFlag = zKeyPressed;
		
		aKeyDown = !aKeyFlag && aKeyPressed;
		aKeyReleased = aKeyFlag && !aKeyPressed;
		aKeyFlag = aKeyPressed;	
	}
	
	public static Vec2 getInputDirection()
	{
		Vec2 direction = new Vec2(0, 0);
		
		if(isUpKeyPressed())
		{
			direction = direction.add(new Vec2(0, 1));
		}
		
		if(isDownKeyPressed())
		{
			direction = direction.add(new Vec2(0, -1));
		}
		
		if(isLeftKeyPressed())
		{
			direction = direction.add(new Vec2(-1, 0));
		}
		
		if(isRightKeyPressed())
		{
			direction = direction.add(new Vec2(1, 0));
		}
		
		direction.normalize();
		return direction;
	}
	
	public static void resetFlags()
	{
		upKeyFlag = false;
		downKeyFlag = false;
		leftKeyFlag = false;
		rightKeyFlag = false;
		spaceKeyFlag = false;
		
		xKeyFlag = false;
		yKeyFlag = false;
		zKeyFlag = false;
		aKeyFlag = false;
	}
}
