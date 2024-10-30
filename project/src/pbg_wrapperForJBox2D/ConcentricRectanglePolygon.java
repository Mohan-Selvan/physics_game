package pbg_wrapperForJBox2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.jbox2d.common.Vec2;

public class ConcentricRectanglePolygon implements ICompoundElement {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private Vec2 position;
	private Vec2 size;
	private Color color;
	private float skinWidth;
	private boolean drawBorders = true;
	private BasicPhysicsEngineUsingBox2D game;
	
	public ConcentricRectanglePolygon(Vec2 position, Vec2 size, float skinWidth, Color color, boolean drawBorders, BasicPhysicsEngineUsingBox2D game)
	{		
		this.position = position;
		this.size = size;
		this.skinWidth = skinWidth;
		this.color = color;
		
		this.drawBorders = drawBorders;
		this.game = game;
		
		createRectangles();
		
		game.compoundElements.add(this);
	}
	
	private void createRectangles()
	{
		Vec2 halfSize = new Vec2(size.x / 2, size.y / 2);
		
		Vec2 topRectPosition = new Vec2(position.x, position.y + (halfSize.y + (skinWidth / 2f)));
		Vec2 bottomRectPosition = new Vec2(position.x, position.y - (halfSize.y + (skinWidth / 2f)));
		Vec2 leftRectPosition = new Vec2(position.x - (halfSize.x + (skinWidth / 2f)), position.y);
		Vec2 rightRectPosition = new Vec2(position.x + (halfSize.x + (skinWidth / 2f)), position.y);

		new BasicRectangle(
				topRectPosition,
				new Vec2(size.x + (skinWidth * 2f), skinWidth),
				color,
				false,							//draw borders
				game
				);
			
		new BasicRectangle(
				bottomRectPosition,
				new Vec2(size.x + (skinWidth * 2f), skinWidth),
				color,
				false,							//draw borders
				game
				);
		
		new BasicRectangle(
				leftRectPosition,
				new Vec2(skinWidth, size.y),
				color,
				false,							//draw borders
				game
				);
		
		new BasicRectangle(
				rightRectPosition,
				new Vec2(skinWidth, size.y),
				color,
				false,							//draw borders
				game
				);
	}

	@Override
	public void draw(Graphics2D g) {
		
		if(drawBorders && Constants.DRAW_BORDERS)
		{
			AffineTransform at = g.getTransform();
			
			g.setStroke(Constants.getBorderStroke());
			g.setColor(Constants.BORDER_COLOR);
			
			int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(this.position.x - (this.size.x / 2));
			int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(this.position.y + (this.size.y / 2));
			
			int width = (int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(this.size.x),1);
			int height = (int)Math.max(BasicPhysicsEngineUsingBox2D.convertWorldLengthToScreenLength(this.size.y),1);
			
			g.drawRect(x, y, width, height);
			
			g.setTransform(at);
		}
	}
}
