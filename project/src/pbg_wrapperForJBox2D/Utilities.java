package pbg_wrapperForJBox2D;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.Random;

import org.jbox2d.common.Vec2;

public final class Utilities {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	//Random - Not seeded
	private static final Random random = new Random(); 
	
	public static float getDistanceBetweenVectors(Vec2 v1, Vec2 v2)
	{
		return v2.sub(v1).length();
	}
	
	public static Vec2 polarToCartesian(float angle, float r)
	{
		return new Vec2(
				(float) (r * Math.cos(Math.toRadians(angle))),
				(float) (r * Math.sin(Math.toRadians(angle)))
				);		
	}
	
	public static Vec2 getDirection(Vec2 A, Vec2 B)
	{
		Vec2 direction = B.sub(A);
		direction.normalize();
		
		return direction;
	}
	
	public static float lerp(float a, float b, float t)
	{
		float modT = clamp01(t);
		return a + ((b-a) * modT);
	}
	
	public static int lerp(int a, int b, float t)
	{
		float modT = clamp01(t);
		return (int)(a + ((b-a) * modT));
	}
	
	public static double lerp(double a, double b, float t)
	{
		float modT = clamp01(t);
		return a + ((b-a) * modT);
	}
	
	public static Vec2 clampVector(Vec2 vector, float scale)
	{
		Vec2 v = vector.clone();
		v.normalize();
		v = v.mul(scale);
		return v;
	}
	
	public static Vec2 lerpVector(Vec2 v1, Vec2 v2, float t)
	{
		return new Vec2(lerp(v1.x, v2.x, t), lerp(v1.y, v2.y, t));
	}
	
	public static Color lerpColor(Color c1, Color c2, float t)
	{
		return new Color(
				lerp(c1.getRed(), c2.getRed(), t),
				lerp(c1.getGreen(), c2.getGreen(), t),
				lerp(c1.getBlue(), c2.getBlue(), t),
				lerp(c1.getAlpha(), c2.getAlpha(), t)
				);
	}
	
	public static float clamp01(float n)
	{
		return clamp(0f, 1f, n);
	}
	
	public static float clamp(float min, float max, float value)
	{
		return Math.max(Math.min(value, max), min);
	}
	
	public static int clamp(int min, int max, int value)
	{
		return (int) Math.max(Math.min(value, max), min);
	}
	
	public static Path2D.Float getPathForRectangle(Vec2 size)
	{
		Path2D.Float path = new Path2D.Float();
		
		//Drawing a rectangle
		path.moveTo(-size.x * 0.5f , -size.y * 0.5f);
		path.lineTo(size.x * 0.5f, -size.y * 0.5f);
		path.lineTo(size.x * 0.5f, size.y * 0.5f);
		path.lineTo(-size.x * 0.5f, size.y * 0.5f);
		path.lineTo(-size.x * 0.5f, -size.y * 0.5f);

		return path;
	}

	public static Color getColorWithAlpha(Color color, float normalizedAlpha)
	{
		int a = lerp(0, 255, clamp01(normalizedAlpha));
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
	}
	
	public static float getRandomFloatNormalised() {
		return random.nextFloat();
	}
	
	public static float getRandomFloat(float min, float max) {
		return lerp(min, max, random.nextFloat());
	}
	
	public static boolean getRandomBool() {
		return getRandomFloatNormalised() >= 0.5f;
	}
	
	//Rotates the given vector, positive angle rotates the vector anti-clockwise. Angle is specified in degrees.
	public static Vec2 getRotatedVector(Vec2 vector, float angleInDegrees)
	{
		float angleInRad = (float) Math.toRadians(angleInDegrees);
		
		vector = new Vec2(
				(float)((Math.cos(angleInRad) * vector.x) - (Math.sin(angleInRad) * vector.y)),
				(float)((Math.sin(angleInRad) * vector.x) + (Math.cos(angleInRad) * vector.y))
			);
		
		return vector;
	}
	
	public static Vec2 getDirectionFromAngle(float angleInDegrees)
	{
		Vec2 vector = new Vec2(1f, 0f);
		vector = getRotatedVector(vector, angleInDegrees);
		vector.normalize();

		return vector;
	}
}
