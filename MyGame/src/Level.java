import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Level {
	private static BufferedImage background;
	private int levelNumber;
	
	public Level()
	{
	}
	
	public static void createBackground(String file)
	{
		try {
			background = ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		background = resizeImage(background);
	}
	
	public static BufferedImage resizeImage(BufferedImage original) 
	{
        BufferedImage resized = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, Main.WIDTH, Main.HEIGHT, null);
        g.dispose();
        return resized;
	}
	
	public static BufferedImage getBackground()
	{
		return background;
	}
}
