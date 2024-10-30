package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class Key extends BasicParticle 
{	
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	
	private int keyId;
	private BasicPhysicsEngineUsingBox2D game;
	
	private Color keyColor = Color.white;
	
	//Graphic resources
	private Image keySprite = null;
	
	public Key(int keyId, Vec2 spawnPosition, float radius, Color color, BasicPhysicsEngineUsingBox2D game) {
		super(spawnPosition, radius, color, 
				1f,	//mass 
				0f	//drag force
		);
		
		this.game = game;
		this.keyId = keyId;
		this.keyColor = color;
		
		this.body.setType(BodyType.KINEMATIC);
		this.body.getFixtureList().setSensor(true);		
		
		game.particles.add(this);
		
		//Sprite handling
		if(Constants.DRAW_SPRITES)
		{
			keySprite = Resources.loadImageFromFile(Resources.PATH_KEY_SPRITE);
			
			if(keySprite != null)
			{
				keySprite = keySprite.getScaledInstance(24, 24, 1);
			}	
		}
	}
	
	public void setKeyFixtureUserData(KeyUserData keyUserData)
	{
		this.body.getFixtureList().setUserData(keyUserData);
	}
	
	public int getKeyId()
	{
		return this.keyId;
	}
	
	public void disableKey()
	{
		this.body.setActive(false);
		game.particles.remove(this);
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(body.getPosition().x);
		int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(body.getPosition().y);
		
		if(Constants.DRAW_SPRITES && keySprite != null)
		{			
			AffineTransform at = g.getTransform();
			
			int posX = x - (keySprite.getWidth(null) /2);
			int posY = y - (keySprite.getHeight(null)/2);
			g.drawImage(keySprite, posX, posY, keyColor,  null);
			
			g.setTransform(at);
		}
		else 
		{
			super.draw(g);
			
			if(Constants.DRAW_BORDERS)
			{
				g.setColor(Constants.BORDER_COLOR);
				g.setStroke(Constants.getBorderStroke());
				g.drawOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
			}
		}
		
	}
}