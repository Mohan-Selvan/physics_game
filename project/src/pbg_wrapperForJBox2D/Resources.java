package pbg_wrapperForJBox2D;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Resources {
	/*
	 * Author: Moganaselvan Ramamoorthy
	 */
	
	public static String PATH_BALL_IMAGE = "ball.png";
	public static String PATH_CANNON_IMAGE = "cannon.png";
	public static String PATH_KEY_SPRITE =  "key.png";
	public static String PATH_STAR_FIELD_BG =  "star_field_bg.png";
	
	public static BufferedImage loadImageFromFile(String fileName)
	{
		BufferedImage sprite;
		
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(fileName);
            sprite = ImageIO.read(input);
            return sprite;
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
            return null;
        }
	}
}
