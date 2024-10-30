package pbg_wrapperForJBox2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import javax.swing.SpinnerNumberModel;

public class UiHandler {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private Font font = null;
	private Paint paint = null;
	
	
	private UiProgressBar timeSlowAbilityBar;
	
	private int basePositionX, basePositionY;
	
	private BasicPhysicsEngineUsingBox2D game;
	
	public UiHandler(BasicPhysicsEngineUsingBox2D game)
	{
		this.game = game;
		
		font = new Font("Helvetica", 3, 12);

		basePositionX = basePositionY = 0;
		
		timeSlowAbilityBar = new UiProgressBar(
				150, 			//sizeX
				14,				//sizeY
				4,				//skin width
				Color.white, 	//bar color
				Color.red,		//low bar color
				Color.white		//border color
				);	
	}
	
	public void draw(Graphics2D g, int atX, int atY, double uiCameraScale)
	{
		basePositionX = 50 - atX;
		basePositionY = 70 - atY;
		
		int posX  = basePositionX;
		int posY = basePositionY;
		
		posX -= 2; posY -= 5;
		drawBorderedRectangle(g, 
				posX, posY,					//Position
				280, 50,												//Size
				Utilities.getColorWithAlpha(Color.darkGray, 0.5f),		//Rect color
				Color.white												//Border color
				);
		
		posX += 10; posY += 20;
		drawCharges(g, posX, posY, BasicPhysicsEngineUsingBox2D.getBall());
		
//		posX += 0; posY += 20;
//		g.drawString("Timer : " + game.getTimeSlowAbilityLeftNormalised(), posX, posY);
		
		posX += 0; posY += 20;
		g.setColor(Color.white);
		g.drawString("Timer : ", posX, posY);
		
		posX += 134; posY += -5;
		
		g.setStroke(Constants.getNormalStroke());
		timeSlowAbilityBar.draw(g, game.getTimeSlowAbilityLeftNormalised(), posX, posY);
		//g.setColor(Color.white);
		//g.fillRect(basePositionX, basePositionY, 200, 100);	
	}
	
	private void drawBorderedRectangle(Graphics2D g, int posX, int posY, int width, int height, Color rectColor, Color borderColor)
	{
		g.setStroke(new BasicStroke(2f));
		g.setColor(borderColor);
		g.drawRect(posX, posY, width, height);
		g.setColor(rectColor);		
		g.fillRect(posX, posY, width, height);
	}
	
	private void drawCharges(Graphics2D g, int drawPositionX, int drawPositionY, BallParticle ball)
	{
		if(ball == null || ball.getMaxOfCharges() == 0) {return;}
		
		g.setColor(Color.white);
		g.setFont(font);		
		
		if(ball.getMaxOfCharges() < 0)
		{
			g.drawString("Charges:   INFINITE", drawPositionX, drawPositionY);
			return;
		}
		else 
		{
			g.drawString("Charges: ", drawPositionX, drawPositionY);
		}
		
		g.setColor(Color.red);
		int offsetX = 15; 
		int posX = drawPositionX + 70;
		int posY = drawPositionY + 1;

		int numberOfChargeSlots = Utilities.clamp(0, 10, ball.getMaxOfCharges());
		int numberOfChargesToDraw = ball.getNumberOfChargesLeft();
		
		int circleRadius = 10;
		
		for(int i = 0; i < numberOfChargeSlots; i++)
		{
			int halfRadius = circleRadius;
			
			if(i < numberOfChargesToDraw)
			{
				g.fillOval(posX - halfRadius, posY - halfRadius, circleRadius, circleRadius);
			}
			else 
			{
				g.drawOval(posX - halfRadius, posY - halfRadius, circleRadius, circleRadius);
			}
			
			posX += offsetX;
		}
	}
}
