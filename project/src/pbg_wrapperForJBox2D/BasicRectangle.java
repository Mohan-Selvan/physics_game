package pbg_wrapperForJBox2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class BasicRectangle extends BasicPolygon {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	private final Vec2 position;
	private final Vec2 size;

	//Visuals
	private boolean drawBorders = true;
	private Color borderColor;
	
	public BasicRectangle(Vec2 position, Vec2 size, BasicPhysicsEngineUsingBox2D game)
	{
		this(position, size, 0f, Constants.WALL_COLOR, game); //boolean parameter refer to drawBorders
	}
	
	public BasicRectangle(Vec2 position, Vec2 size, Color color, BasicPhysicsEngineUsingBox2D game)
	{
		this(position, size, 0f, color, true, Constants.BORDER_COLOR, game); //boolean parameter refer to drawBorders
	}
	
	public BasicRectangle(Vec2 position, Vec2 size, float angleInDegrees, Color color, BasicPhysicsEngineUsingBox2D game)
	{
		this(position, size, angleInDegrees, color, true, Constants.BORDER_COLOR, game); //boolean parameter refer to drawBorders
	}
	
	public BasicRectangle(Vec2 position, Vec2 size, float angleInDegrees, Color color, boolean drawBorders, BasicPhysicsEngineUsingBox2D game)
	{
		this(position, size, angleInDegrees, color, drawBorders, Constants.BORDER_COLOR, game); //boolean parameter refer to drawBorders
	}
	
	public BasicRectangle(Vec2 position, Vec2 size, Color color, boolean drawBorders, BasicPhysicsEngineUsingBox2D game)
	{
		this(position, size, 0f, color, drawBorders, Constants.BORDER_COLOR, game); //boolean parameter refer to drawBorders
	}
	
	public BasicRectangle(Vec2 position, Vec2 size, float angleInDegrees, Color color, boolean drawBorder, Color borderColor, BasicPhysicsEngineUsingBox2D game)
	{
		super(
				position.x, position.y, 				//position
				0, 0,									//velocity
				1f,										//radius
				color, 									//color
				1f,										//mass
				0f,										//rolling friction
				Utilities.getPathForRectangle(size),	//path
				4										//number of sides
				);
		
		this.position = position;
		this.size = size;
		this.drawBorders = drawBorder;
		this.borderColor = borderColor;

		this.body.setType(BodyType.STATIC);
		this.body.setTransform(position, (float)Math.toRadians(angleInDegrees));
		
		game.polygons.add(this);
	}
	
	@Override
	public void draw(Graphics2D g)
	{	
		super.draw(g);
		
		if(this.drawBorders && Constants.DRAW_BORDERS)
		{
			int x = BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(position.x);
			int y = BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(position.y);
			
			//g.fillOval(x - 50, y - 50, 100, 100);

			g.setStroke(Constants.getBorderStroke());
			g.setColor(borderColor);

			Vec2 position = body.getPosition();
			float angle = body.getAngle(); 
			AffineTransform af = new AffineTransform();
			af.translate(BasicPhysicsEngineUsingBox2D.convertWorldXtoScreenX(position.x), BasicPhysicsEngineUsingBox2D.convertWorldYtoScreenY(position.y));
			af.scale(ratioOfScreenScaleToWorldScale, -ratioOfScreenScaleToWorldScale);// there is a minus in here because screenworld is flipped upsidedown compared to physics world
			af.rotate(angle); 
			Path2D.Float p = new Path2D.Float (Utilities.getPathForRectangle(size),af);
			g.draw(p);		
		}
	}
}
