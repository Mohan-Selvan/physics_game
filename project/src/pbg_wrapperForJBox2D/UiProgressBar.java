package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.Graphics2D;

public class UiProgressBar
{
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private int sizeX, sizeY, skinWidth;
	private Color barColor, lowBarColor, borderColor;
	
	private float lowBarThreshold = 0.3f;
	
	public UiProgressBar(int sizeX, int sizeY, int skinWidth, Color barColor, Color lowBarColor, Color borderColor)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.skinWidth = skinWidth;
		
		this.barColor = barColor;
		this.borderColor = borderColor;
		this.lowBarColor = lowBarColor;
	}
	
	public void draw(Graphics2D g, float progressNormalised, int posX, int posY) {
		
		g.setColor((progressNormalised > lowBarThreshold) ? this.barColor : this.lowBarColor);
		g.fillRect(
				posX - (sizeX / 2) + (skinWidth / 2), 
				posY - (sizeY / 2) + (skinWidth / 2), 
				(int)((float)sizeX * progressNormalised) - skinWidth, 
				sizeY - skinWidth
			);
		
		g.setColor(this.borderColor);
		g.drawRect(
				posX - (sizeX / 2),
				posY - (sizeY / 2),
				sizeX,
				sizeY
			);
		
	}
	
	public int getSizeX()
	{
		return sizeX;
	}
}
