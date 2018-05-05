import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Ammunition {
	private BufferedImage bullet;
	private BufferedImage flames;
	private BufferedImage MGcase;
	private BufferedImage FTcan;
	private BufferedImage bomber;
	private Rectangle FTcanLocation;
	private Rectangle MGcaseLocation;
	private Rectangle bomberLocation;
	private static double MGammo = 0;
	private static double FTammo = 0;
	private static int bombers = 0;
	private int flameSwitch = 0;
	private boolean switchFlames = true;
	private int xLoc, yLoc, fXloc, fYloc, MGcaseXloc, MGcaseYloc, FTcanXloc, FTcanYloc,
				bomberXloc, bomberYloc;
	private Timer FTcanTimer = new Timer();
	private Timer MGcaseTimer = new Timer();
	private Timer bomberTimer = new Timer();
	
	public Ammunition()
	{
		FTcanTimer.startTimer();
		MGcaseTimer.startTimer();
		bomberTimer.startTimer();
		setAmmoImages();
	}
	
	public void setAmmoImages()
	{
		try {
			bullet = ImageIO.read(new File("bullet.png"));
			flames = ImageIO.read(new File("flamesRight.png"));
			MGcase = ImageIO.read(new File("MGcase.png"));
			FTcan = ImageIO.read(new File("FTcan.png"));
			bomber = ImageIO.read(new File("bomberPickup.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xLoc = -100;
		yLoc = -100;
		fXloc = -100;
		fYloc = -100;
		MGcaseXloc = -100;
		MGcaseYloc = -100;
		MGcaseLocation = getMGcaseLocation();
		FTcanXloc = -100;
		FTcanYloc = -100;
		FTcanLocation = getFTcanLocation();
		bomberXloc = -100;
		bomberYloc = -100;
		bomberLocation = getBomberLocation();
	}
	
	public void switchFlameImage(String file)
	{
		try {
			flames = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void burnFlames(String direction)
	{
		if (flameSwitch > 10)
		{
			if (switchFlames)
			{
				switchFlameImage("flames" + direction + "1.png");
				flameSwitch = 0;
				switchFlames = false;
			}
			else
			{
				switchFlameImage("flames" + direction + ".png");
				flameSwitch = 0;
				switchFlames = true;
			}
		}
		flameSwitch++;
	}
	
	public void pickupFTcan(Rectangle player)
	{
		if(player.intersects(FTcanLocation))
		{
			FTammo += 50;
			setFTcanXLoc(-100);
			setFTcanYLoc(-100);
			FTcanLocation = getFTcanLocation();
		}
	}
	
	public void pickupMGcase(Rectangle player)
	{
		if(player.intersects(MGcaseLocation))
		{
			MGammo += 50;
			setMGcaseXLoc(-100);
			setMGcaseYLoc(-100);
			MGcaseLocation = getMGcaseLocation();
		}
	}
	
	public void pickupBomber(Rectangle player)
	{
		if(player.intersects(bomberLocation))
		{
			bombers++;
			setBomberXloc(-100);
			setBomberYloc(-100);
			bomberLocation = getBomberLocation();
		}
	}
	
	public void displayFTcan(Rectangle player)
	{
		if (FTcanTimer.getTime() == 15)
		{
			int randX = (int)(Math.random() * (Main.WIDTH - FTcan.getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - FTcan.getHeight()));
			setFTcanXLoc(randX);
			setFTcanYLoc(randY);
			FTcanLocation = getFTcanLocation();
			while (player.intersects(FTcanLocation))
			{
				randX = (int)(Math.random() * (Main.WIDTH - FTcan.getWidth()));
				randY = (int)(Math.random() * (Main.HEIGHT - FTcan.getHeight()));
				setFTcanXLoc(randX);
				setFTcanYLoc(randY);
				FTcanLocation = getFTcanLocation();
			}
		}
		if (FTcanTimer.getTime() == 25)
		{
			setFTcanXLoc(-100);
			setFTcanYLoc(-100);
			FTcanTimer.resetTimer();
			FTcanLocation = getFTcanLocation();
		}
	}
	
	public void displayMGcase(Rectangle player)
	{
		if (MGcaseTimer.getTime() == 10)
		{
			int randX = (int)(Math.random() * (Main.WIDTH - MGcase.getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - MGcase.getHeight()));
			setMGcaseXLoc(randX);
			setMGcaseYLoc(randY);
			MGcaseLocation = getMGcaseLocation();
			while (player.intersects(MGcaseLocation))
			{
				randX = (int)(Math.random() * (Main.WIDTH - MGcase.getWidth()));
				randY = (int)(Math.random() * (Main.HEIGHT - MGcase.getHeight()));
				setMGcaseXLoc(randX);
				setMGcaseYLoc(randY);
				MGcaseLocation = getMGcaseLocation();
			}
		}
		if (MGcaseTimer.getTime() == 20)
		{
			setMGcaseXLoc(-100);
			setMGcaseYLoc(-100);
			MGcaseTimer.resetTimer();
			MGcaseLocation = getMGcaseLocation();
		}
	}
	
	public void displayBomber(Rectangle player)
	{
		if (bomberTimer.getTime() == 1)
		{
			int randX = (int)(Math.random() * (Main.WIDTH - bomber.getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - bomber.getHeight()));
			setBomberXloc(randX);
			setBomberYloc(randY);
			bomberLocation = getBomberLocation();
			while (player.intersects(bomberLocation))
			{
				randX = (int)(Math.random() * (Main.WIDTH - bomber.getWidth()));
				randY = (int)(Math.random() * (Main.HEIGHT - bomber.getHeight()));
				setBomberXloc(randX);
				setBomberYloc(randY);
				bomberLocation = getBomberLocation();
			}
		}
		if (bomberTimer.getTime() == 10)
		{
			setBomberXloc(-100);
			setBomberYloc(-100);
			bomberTimer.resetTimer();
			bomberLocation = getBomberLocation();
		}
	}
	
	public boolean beenShot(Enemy e, Rectangle ammo){return e.beenShot(ammo);}
	public void paintBullet(Graphics g, int x, int y){g.drawImage(bullet, x, y, null);}
	public void paintFlames(Graphics g, int x, int y){g.drawImage(flames, x, y, null);}
	public void paintMGammoCase(Graphics g, int x, int y){g.drawImage(MGcase, x, y, null);}
	public void paintFTcanister(Graphics g, int x, int y){g.drawImage(FTcan, x, y, null);}
	public void paintBomber(Graphics g, int x, int y){g.drawImage(bomber, x, y, null);}
	public int getBulletXLoc(){return xLoc;}
	public int getBulletYLoc(){return yLoc;}
	public int getMGcaseXLoc(){return MGcaseXloc;}
	public int getMGcaseYLoc(){return MGcaseYloc;}
	public int getFTcanXLoc(){return FTcanXloc;}
	public int getFTcanYLoc(){return FTcanYloc;}
	public int getFlamesXLoc(){return fXloc;}
	public int getFlamesYLoc(){return fYloc;}
	public int getBomberXloc(){return bomberXloc;}
	public int getBomberYloc(){return bomberYloc;}
	public static double getMGammo(){return MGammo;}
	public static double getFTammo(){return FTammo;}
	public static int getBombers(){return bombers;}
	public void setBulletXLoc(int x){xLoc = x;}
	public void setBulletYLoc(int y){yLoc = y;}
	public void setFlamesXLoc(int x){fXloc = x;}
	public void setFlamesYLoc(int y){fYloc = y;}
	public void setMGcaseXLoc(int x){MGcaseXloc = x;}
	public void setMGcaseYLoc(int y){MGcaseYloc = y;}
	public void setFTcanXLoc(int x){FTcanXloc = x;}
	public void setFTcanYLoc(int y){FTcanYloc = y;}
	public void setBomberXloc(int x){bomberXloc = x;}
	public void setBomberYloc(int y){bomberYloc = y;}
	public static void addMGammo(int quantity){MGammo += quantity;}
	public static void addFTammo(int quantity){FTammo += quantity;}
	public static void addBombers(int quantity){bombers += quantity;}
	public static void decrementMGammo(){MGammo -= .1;}
	public static void decrementFTammo(){FTammo -= .2;}
	public static void decrementBombers(){bombers--;}
	public Rectangle getBulletLocation(){return new Rectangle(getBulletXLoc(), 
			getBulletYLoc(), bullet.getWidth(), bullet.getHeight());}
	public Rectangle getFlamesLocation(){return new Rectangle(getFlamesXLoc(), 
			getFlamesYLoc(), flames.getWidth(), flames.getHeight());}
	public Rectangle getMGcaseLocation(){return new Rectangle(getMGcaseXLoc(), 
			getMGcaseYLoc(), MGcase.getWidth(), MGcase.getHeight());}
	public Rectangle getFTcanLocation(){return new Rectangle(getFTcanXLoc(), 
			getFTcanYLoc(), FTcan.getWidth(), FTcan.getHeight());}
	public Rectangle getBomberLocation(){return new Rectangle(getBomberXloc(), 
			getBomberYloc(), bomber.getWidth(), bomber.getHeight());}
}
