import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Enemy {
	private BufferedImage img;
	private boolean canSwitchImg = true;
	private int delaySwitch = 0;
	private String facing = "right";
	private int health = 5;
	private double xLoc, yLoc;
	Rectangle location;
	private int directionToMove; // 1 down, 2 left
	public static double enemySpeed = 2;
	public static Timer t;
	public int lives = 5;
	
	public Enemy(double x, double y, int direction)
	{
		try {
			img = ImageIO.read(new File("enemy.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xLoc = x;
		yLoc = y;
		location = new Rectangle((int)getXLoc(), (int)getYLoc(), img.getWidth(), img.getHeight());
		directionToMove = direction;
		t = new Timer();
		t.startTimer();
	}
	
	public void paint(Graphics g, int x, int y)
	{
		if (health > 0)
			g.drawImage(img, x, y, null);
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
		xLoc += x;
	}
	
	public void setY(double y)
	{
		yLoc += y;
	}
	
	public void changeImage(String name)
	{
		String image;
		if (delaySwitch == 10) {
			if (canSwitchImg) {
				image = "enemy" + name + ".png";
				canSwitchImg = false;
			} else {
				image = "enemy" + name + "1.png";
				canSwitchImg = true;
			}
			try {
				img = ImageIO.read(new File(image));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(name + " not recognized!");
			}
			delaySwitch = 0;
		}		
		delaySwitch++;
	}
	
	public String getFacing()
	{
		return facing;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}
	
	public void decrementHealth()
	{
		health--;
	}
	
	public void charactersIntersect(Rectangle playerLoc, Player player)
	{
		if(playerLoc.intersects(location) && Weapons.useStarPower)
		{
			decrementHealth();
			if (health == 0)
				Main.score += 100;
		}
		else if(playerLoc.intersects(location))
		{
			if (player.getLives() > 1)
			{
				player.decrementLives();
				player.setX(Main.WIDTH / 2);
				player.setY(Main.HEIGHT / 2);
				Main.resetLevel();
			}
			
			else 
				Main.running = false;
		}
	}
	
	public boolean beenShot(Rectangle ammo)
	{
		if(health <= 0)
		{
			location.setLocation(-100, -100);
			return false;
		}
		boolean beenShot = false;
		if(ammo.intersects(location))
			beenShot = true;
		return beenShot;
	}
	
	public int getDirection()
	{
		return directionToMove;
	}
	
	public void setDirection(int x)
	{
		directionToMove = x;
	}
	
	public Rectangle getLocation()
	{
		return location;
	}
	
	public static void moveEnemies(Vector<Enemy> enemies)
	{
		if (t.getTime() > 1)
		{
			for(int i = 0; i < enemies.size(); i++)
			{
				enemies.elementAt(i).setDirection((int) (Math.random() * 4 + 1));
			}
			t.resetTimer();
		}
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.elementAt(i).getDirection() == 1) //down
			{
				if (enemies.elementAt(i).getYLoc() + 64 > Main.HEIGHT)
					enemies.elementAt(i).setDirection(3);
				enemies.elementAt(i).setY(enemySpeed);
				enemies.elementAt(i).getLocation().setLocation((int)enemies.elementAt(i).getXLoc(), (int)enemies.elementAt(i).getYLoc());
				enemies.elementAt(i).changeImage("Down");
			}   
			if(enemies.elementAt(i).getDirection() == 2) //left
			{
				if (enemies.elementAt(i).getXLoc() < 0)
					enemies.elementAt(i).setDirection(4);
				enemies.elementAt(i).setX(-enemySpeed);
				enemies.elementAt(i).getLocation().setLocation((int)enemies.elementAt(i).getXLoc(), (int)enemies.elementAt(i).getYLoc());
				enemies.elementAt(i).changeImage("Left");
			}
			if(enemies.elementAt(i).getDirection() == 3) //up
			{
				if (enemies.elementAt(i).getYLoc() < 0)
					enemies.elementAt(i).setDirection(1);
				enemies.elementAt(i).setY(-enemySpeed);
				enemies.elementAt(i).getLocation().setLocation((int)enemies.elementAt(i).getXLoc(), (int)enemies.elementAt(i).getYLoc());
				enemies.elementAt(i).changeImage("Up");
			}
			if(enemies.elementAt(i).getDirection() == 4) //right
			{
				if (enemies.elementAt(i).getXLoc() > Main.WIDTH - 54)
					enemies.elementAt(i).setDirection(2);
				enemies.elementAt(i).setX(enemySpeed);
				enemies.elementAt(i).getLocation().setLocation((int)enemies.elementAt(i).getXLoc(), (int)enemies.elementAt(i).getYLoc());
				enemies.elementAt(i).changeImage("Right");
			}
		}
	}
	
	
}
