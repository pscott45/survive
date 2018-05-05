import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player{
	private BufferedImage img;
	private boolean canSwitchImg = true;
	private int delaySwitch = 0;
	private String facing = "right";
	private double xLoc, yLoc;
	private int lives = 5;
	private final int DEFAULT_SPEED = 2;
	private int playerSpeed;
	
	public Player(double x, double y)
	{
		try {
			img = ImageIO.read(new File("handgunRight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xLoc = x;
		yLoc = y;
	}
	
	public void paint(Graphics g, int x, int y)
	{
			g.drawImage(img, x, y, null);
	}
	
	public void setDefaultSpeed()
	{
		playerSpeed = DEFAULT_SPEED;
	}
	
	public void setSpeed(int speed)
	{
		playerSpeed = speed;
	}
	
	public int getSpeed()
	{
		return playerSpeed;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	public void decrementLives()
	{
		lives--;
	}
	
	public int getHeight()
	{
		return img.getHeight();
	}
	
	public int getWidth()
	{
		return img.getWidth();
	}
	
	public void move(String direction)
	{
		String image;
		if(delaySwitch == 5) {
			if (Weapons.useStarPower) {
				if (canSwitchImg) {
					image = "handgun" + direction + "(starPower).png";
					changeImage(image);
					canSwitchImg = false;
				} else {
					image = "handgun" + direction + "1.png";
					changeImage(image);
					canSwitchImg = true;
				}
			}
			else
			{
				if (canSwitchImg) {
					image = "handgun" + direction + ".png";
					changeImage(image);
					canSwitchImg = false;
				} else {
					image = "handgun" + direction + "1.png";
					changeImage(image);
					canSwitchImg = true;
				}
			}
			delaySwitch = 0;
		}
		delaySwitch++;
		facing = direction;
	}
	
	public void changeImage(String name)
	{
		try {
			img = ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(name + " not recognized!");
		}
	}
	
	public String getFacing()
	{
		return facing;
	}
	
	public double getXLoc()
	{
		return xLoc;
	}
	
	public double getYLoc()
	{
		return yLoc;
	}
	
	public void setX(double x)
	{
		xLoc = x;
	}
	
	public void setY(double y)
	{
		yLoc = y;
	}
	
	public void moveX(double x)
	{
		xLoc += x;
	}
	
	public void moveY(double y)
	{
		yLoc += y;
	}
	
	public Rectangle getLocation()
	{
		return new Rectangle((int)getXLoc(), (int)getYLoc(), getWidth(), getHeight());
	}
}
