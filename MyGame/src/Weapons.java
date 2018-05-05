import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Weapons {
	private boolean usePistol = true;
	private boolean useMachineGun = false;
	private boolean useFlameThrower = false;
	private boolean MGDisplayed = false;
	private boolean FTDisplayed = false;
	private boolean authorizeMG = false;
	private boolean authorizeFT = false;
	private static boolean showExplosion = false;
	public static boolean useStarPower = false;
	private BufferedImage pistol;
	private BufferedImage machineGun;
	private BufferedImage flameThrower;
	private BufferedImage starPower;
	private BufferedImage grenade;
	private BufferedImage explosion;
	private Ammunition ammo = new Ammunition();
	private int numGrenades = 0;
	private String directionToShoot = "right";
	private int explosionX = 0;
	private int explosionY = 0;
	private String directionToThrow = "Right";
	private Rectangle MGlocation;
	private Rectangle FTlocation;
	private Rectangle SPlocation;
	private Rectangle Glocation;
	private Rectangle Elocation;
	private double MGxLoc, MGyLoc, FTxLoc, FTyLoc, SPxLoc, SPyLoc, GxLoc, GyLoc, ExLoc, EyLoc;
	private Timer spt = new Timer();
	private Timer gt = new Timer();
	private Timer explosionTimer = new Timer();
	private Timer MGsound = new Timer();
	
	public Weapons()
	{
		loadImages();
		setMGXLoc(-100);
		setMGYLoc(-100);
		MGlocation = getMGLocation();
		setFTXLoc(-100);
		setFTYLoc(-100);
		FTlocation = getFTLocation();
		setSPXLoc(-100);
		setSPYLoc(-100);
		SPlocation = getSPLocation();
		setGXLoc(-100);
		setGYLoc(-100);
		Glocation = getGLocation();
		setEXLoc(-100);
		setEYLoc(-100);
		Elocation = getELocation();
		spt.startTimer();
		gt.startTimer();
		MGsound.startTimer();
	}
	
	public void paint(Graphics g, int x, int y, BufferedImage img)
	{
		g.drawImage(img, x, y, null);
	}
	
	public void setPistolBool(boolean x){usePistol = x;}
	public void setMGBool(boolean x){useMachineGun = x;}
	public void setFTBool(boolean x){useFlameThrower = x;}
	public void setSPBool(boolean x){useStarPower = x;}
	public void setIsMGDisp(boolean x){MGDisplayed = x;}
	public void setIsFTDisp(boolean x){FTDisplayed = x;}
	public boolean getIsMGDisp(){return MGDisplayed;}
	public boolean getIsFTDisp(){return FTDisplayed;}
	public void AuthorizeMG(boolean x){authorizeMG = x;}
	public void AuthorizeFT(boolean x){authorizeFT = x;}
	public boolean getPistolBool(){return usePistol;}
	public boolean getMGBool(){return useMachineGun;}
	public boolean getFTBool(){return useFlameThrower;}
	public boolean getSPBool(){return useStarPower;}
	public boolean getMGAuthorization(){return authorizeMG;}
	public boolean getFTAuthorization(){return authorizeFT;}
	public Ammunition getAmmo(){return ammo;}
	public double getMGXLoc(){return MGxLoc;}
	public double getMGYLoc(){return MGyLoc;}
	public double getFTXLoc(){return FTxLoc;}
	public double getFTYLoc(){return FTyLoc;}
	public double getEXLoc(){return ExLoc;}
	public double getEYLoc(){return EyLoc;}
	public double getSPXLoc(){return SPxLoc;}
	public double getSPYLoc(){return SPyLoc;}
	public double getGXLoc(){return GxLoc;}
	public double getGYLoc(){return GyLoc;}
	public int getNumGrenades(){return numGrenades;}
	public void setMGXLoc(double x){MGxLoc = x;}
	public void setMGYLoc(double y){MGyLoc = y;}
	public void setFTXLoc(double x){FTxLoc = x;}
	public void setFTYLoc(double y){FTyLoc = y;}
	public void setEXLoc(double x){ExLoc = x;}
	public void setEYLoc(double y){EyLoc = y;}
	public void setSPXLoc(double x){SPxLoc = x;}
	public void setSPYLoc(double y){SPyLoc = y;}
	public void setGXLoc(double x){GxLoc = x;}
	public void setGYLoc(double y){GyLoc = y;}
	public BufferedImage getPimage(){return pistol;}
	public BufferedImage getMGimage(){return machineGun;}
	public Rectangle getMGLocation(){return new Rectangle((int)getMGXLoc(), 
											(int)getMGYLoc(), machineGun.getWidth(), 
											machineGun.getHeight());}
	public void setMGLocation(){MGlocation = getMGLocation();}
	public BufferedImage getFTimage(){return flameThrower;}
	public Rectangle getFTLocation(){return new Rectangle((int)getFTXLoc(), 
											(int)getFTYLoc(), flameThrower.getWidth(), 
											flameThrower.getHeight());}
	public void setFTLocation(){FTlocation = getFTLocation();}
	public BufferedImage getSPimage(){return starPower;}
	public Rectangle getSPLocation(){return new Rectangle((int)getSPXLoc(), 
											(int)getSPYLoc(), starPower.getWidth(), 
											starPower.getHeight());}
	public BufferedImage getGimage(){return grenade;}
	public Rectangle getGLocation(){return new Rectangle((int)getGXLoc(), 
											(int)getGYLoc(), grenade.getWidth(), 
											grenade.getHeight());}
	public BufferedImage getEimage(){return explosion;}
	public Rectangle getELocation(){return new Rectangle((int)getEXLoc(), 
											(int)getEYLoc(), explosion.getWidth(), 
											explosion.getHeight());}
	
	public void loadImages()
	{
		try {
			machineGun = ImageIO.read(new File("MachineGunPickup.png"));
			pistol = ImageIO.read(new File("pistol.png"));
			flameThrower = ImageIO.read(new File("FlameThrowerPickup.png"));
			starPower = ImageIO.read(new File("StarPowerPickup.png"));
			grenade = ImageIO.read(new File("grenade.png"));
			explosion = ImageIO.read(new File("explosion.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pickupMG(Rectangle player)
	{
		if(player.intersects(MGlocation))
		{
			setMGBool(true);
			setPistolBool(false);
			AuthorizeMG(true);
			setMGXLoc(-100);
			setMGYLoc(-100);
			setMGLocation();
			Ammunition.addMGammo(100);
		}
	}
	
	public void pickupFT(Rectangle player)
	{
		if(player.intersects(FTlocation))
		{
			setFTBool(true);
			setPistolBool(false);
			setMGBool(false);
			AuthorizeFT(true);
			setFTXLoc(-100);
			setFTYLoc(-100);
			setFTLocation();
			Ammunition.addFTammo(100);
		}
	}
	
	public void displaySP(Rectangle player)
	{
		if (spt.getTime() == 60)
		{
			int randX = (int)(Math.random() * (Main.WIDTH - starPower.getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - starPower.getHeight()));
			setSPXLoc(randX);
			setSPYLoc(randY);
			SPlocation = getSPLocation();
			while (player.intersects(SPlocation))
			{
				randX = (int)(Math.random() * (Main.WIDTH - starPower.getWidth()));
				randY = (int)(Math.random() * (Main.HEIGHT - starPower.getHeight()));
				setSPXLoc(randX);
				setSPYLoc(randY);
				SPlocation = getSPLocation();
			}
		}
		if (spt.getTime() == 70)
		{
			setSPXLoc(-100);
			setSPYLoc(-100);
			spt.resetTimer();
			SPlocation = getSPLocation();
		}
	}
	
	public void displayG(Rectangle player)
	{
		if (gt.getTime() == 5)
		{
			int randX = (int)(Math.random() * (Main.WIDTH - grenade.getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - grenade.getHeight()));
			setGXLoc(randX);
			setGYLoc(randY);
			Glocation = getGLocation();
			while (player.intersects(Glocation))
			{
				randX = (int)(Math.random() * (Main.WIDTH - grenade.getWidth()));
				randY = (int)(Math.random() * (Main.HEIGHT - grenade.getHeight()));
				setGXLoc(randX);
				setGYLoc(randY);
				Glocation = getGLocation();
			}
		}
		if(gt.getTime() == 15)
		{
			setGXLoc(-100);
			setGYLoc(-100);
			gt.resetTimer();
			Glocation = getGLocation();
		}
	}
	
	public void pickupSP(Rectangle player)
	{
		if(player.intersects(SPlocation))
		{
			setSPBool(true);
			setSPXLoc(-100);
			setSPYLoc(-100);
			SPlocation = getSPLocation();
		}
	}
	
	public void pickupG(Rectangle player)
	{
		if(player.intersects(Glocation))
		{
			numGrenades++;
			setGXLoc(-100);
			setGYLoc(-100);
			Glocation = getGLocation();
		}
	}
	
	public void usePistol(Graphics g, Vector<Enemy> enemies, Player player)
	{
		ammo.paintBullet(g, ammo.getBulletXLoc(), ammo.getBulletYLoc());
		for (int i = 0; i < enemies.size(); i++) 
		{
			if (ammo.beenShot(enemies.elementAt(i), ammo.getBulletLocation())) 
			{
				enemies.elementAt(i).decrementHealth();
				if (enemies.elementAt(i).getHealth() == 0)
					Main.score += 100;
				ammo.setBulletXLoc(-100);
				ammo.setBulletYLoc(-100);

			}
		}
		if (Input.wasKeyPressed(KeyEvent.VK_SPACE) || Input.wasKeyPressed(KeyEvent.VK_Z)) 
		{
			Audio.playSound("pistolAudio.wav");
			directionToShoot = player.getFacing();
			ammo.setBulletXLoc((int) player.getXLoc() + 15);
			ammo.setBulletYLoc((int) player.getYLoc() + 25);
		}
		int x = ammo.getBulletXLoc();
		int y = ammo.getBulletYLoc();
		switch (directionToShoot) {
		case "Up":
			ammo.setBulletYLoc(y -= 35);
			break;
		case "Down":
			ammo.setBulletYLoc(y += 35);
			break;
		case "Left":
			ammo.setBulletXLoc(x -= 35);
			break;
		case "Right":
			ammo.setBulletXLoc(x += 35);
			break;
		case "diagUR":
			ammo.setBulletYLoc(y -= 35);
			ammo.setBulletXLoc(x += 35);
			break;
		case "diagUL":
			ammo.setBulletYLoc(y -= 35);
			ammo.setBulletXLoc(x -= 35);
			break;
		case "diagDR":
			ammo.setBulletYLoc(y += 35);
			ammo.setBulletXLoc(x += 35);
			break;
		case "diagDL":
			ammo.setBulletYLoc(y += 35);
			ammo.setBulletXLoc(x -= 35);
			break;
		default:
			break;
		}
	}
	
	public void useMachineGun(Graphics g, Vector<Enemy> enemies, Player player) {
		for (int i = 0; i < enemies.size(); i++) 
		{
			if (ammo.beenShot(enemies.elementAt(i), ammo.getBulletLocation())) 
			{
				enemies.elementAt(i).decrementHealth();
				if (enemies.elementAt(i).getHealth() == 0)
					Main.score += 100;
				ammo.setBulletXLoc((int) player.getXLoc() + 15);
				ammo.setBulletYLoc((int) player.getYLoc() + 25);
			}
		}
		
		if (Input.isKeyDown(KeyEvent.VK_SPACE) || Input.isKeyDown(KeyEvent.VK_Z)) 
		{
			Ammunition.decrementMGammo();
			if (MGsound.getTime() > .1)
			{
				Audio.playSound("machineGunAudio.wav");
				MGsound.resetTimer();
			}
			ammo.paintBullet(g, ammo.getBulletXLoc(), ammo.getBulletYLoc());
			directionToShoot = player.getFacing();
			if (ammo.getBulletXLoc() <= 0 || ammo.getBulletXLoc() >= Main.WIDTH || ammo.getBulletYLoc() <= 0
					|| ammo.getBulletYLoc() >= Main.HEIGHT) 
			{
				ammo.setBulletXLoc((int) player.getXLoc() + 15);
				ammo.setBulletYLoc((int) player.getYLoc() + 25);
			} 
			else 
			{
				int x = ammo.getBulletXLoc();
				int y = ammo.getBulletYLoc();
				switch (directionToShoot) {
				case "Up":
					ammo.setBulletYLoc(y -= 70);
					break;
				case "Down":
					ammo.setBulletYLoc(y += 70);
					break;
				case "Left":
					ammo.setBulletXLoc(x -= 70);
					break;
				case "Right":
					ammo.setBulletXLoc(x += 70);
					break;
				case "diagUR":
					ammo.setBulletYLoc(y -= 70);
					ammo.setBulletXLoc(x += 70);
					break;
				case "diagUL":
					ammo.setBulletYLoc(y -= 70);
					ammo.setBulletXLoc(x -= 70);
					break;
				case "diagDR":
					ammo.setBulletYLoc(y += 70);
					ammo.setBulletXLoc(x += 70);
					break;
				case "diagDL":
					ammo.setBulletYLoc(y += 70);
					ammo.setBulletXLoc(x -= 70);
					break;
				default:
					break;
				}
			}
		}
		else
		{
			ammo.setBulletXLoc((int) player.getXLoc() + 15);
			ammo.setBulletYLoc((int) player.getYLoc() + 25);
		}
	}
	
	public void useFlameThrower(Graphics g, Vector<Enemy> enemies, Player player)
	{
		for (int i = 0; i < enemies.size(); i++) 
		{
			if (ammo.beenShot(enemies.elementAt(i), ammo.getFlamesLocation())) 
			{
				enemies.elementAt(i).decrementHealth();
				if (enemies.elementAt(i).getHealth() == 0)
					Main.score += 100;
			}
		}
		
		if (Input.isKeyDown(KeyEvent.VK_SPACE) || Input.isKeyDown(KeyEvent.VK_Z))
		{
			Ammunition.decrementFTammo();
			if (MGsound.getTime() > .1) 
			{
				Audio.playSound("flamethrowerAudio.wav"); 
				MGsound.resetTimer(); 
			}
			 
			directionToShoot = player.getFacing();
			int x = (int) player.getXLoc();
			int y = (int) player.getYLoc();
			switch (directionToShoot) {
			case "Up":
				ammo.setFlamesXLoc(x -= 17);
				ammo.setFlamesYLoc(y -= 150);
				ammo.burnFlames(directionToShoot);
				break;
			case "Down":
				ammo.setFlamesXLoc(x += 3);
				ammo.setFlamesYLoc(y += 50);
				ammo.burnFlames(directionToShoot);
				break;
			case "Left":
				ammo.setFlamesXLoc(x -= 150);
				ammo.setFlamesYLoc(y += 1);
				ammo.burnFlames(directionToShoot);
				break;
			case "Right":
				ammo.setFlamesXLoc(x += 55);
				ammo.setFlamesYLoc(y += 1);
				ammo.burnFlames(directionToShoot);
				break;
			case "diagUR":
				ammo.setFlamesXLoc(x += 10);
				ammo.setFlamesYLoc(y -= 150);
				ammo.burnFlames(directionToShoot);
				break;
			case "diagUL":
				ammo.setFlamesXLoc(x -= 120);
				ammo.setFlamesYLoc(y -= 150);
				ammo.burnFlames(directionToShoot);
				break;
			case "diagDR":
				ammo.setFlamesXLoc(x += 20);
				ammo.setFlamesYLoc(y += 50);
				ammo.burnFlames(directionToShoot);
				break;
			case "diagDL":
				ammo.setFlamesXLoc(x -= 170);
				ammo.setFlamesYLoc(y += 50);
				ammo.burnFlames(directionToShoot);
				break;
			default:
				break;
			}
			ammo.paintFlames(g, x, y);
		}

		else 
		{
			ammo.setFlamesXLoc(-100);
			ammo.setFlamesYLoc(-100);
		}
	}
	
	public void throwGrenade(Graphics g, Vector<Enemy> enemies, Player player)
	{
		if	(Input.wasKeyPressed(KeyEvent.VK_G) && numGrenades > 0)
		{
			Audio.playSound("grenadeAudio.wav");
			explosionX = (int) player.getXLoc();
			explosionY = (int) player.getYLoc();
			directionToThrow = player.getFacing();
			explosionTimer.startTimer();
			showExplosion = true;
			numGrenades--;
		}
		if (showExplosion) {
			int distance = 400;
			int x = 0;
			int y = 0;
			switch (directionToThrow) {
			case "Up":
				x = explosionX - explosion.getWidth() / 2;
				y = explosionY - distance;
				break;
			case "Down":
				x = explosionX - explosion.getWidth() / 2;
				y = explosionY + distance - explosion.getHeight();
				break;
			case "Left":
				x = explosionX - distance;
				y = explosionY - explosion.getHeight() / 2;
				break;
			case "Right":
				x = explosionX + distance - explosion.getWidth() / 2;
				y = explosionY - explosion.getHeight() / 2;
				break;
			case "diagUR":
				x = explosionX + distance - explosion.getWidth() / 2;
				y = explosionY - distance + explosion.getHeight() / 2;
				break;
			case "diagUL":
				x = explosionX - distance;
				y = explosionY - distance + explosion.getHeight() / 2;
				break;
			case "diagDR":
				x = explosionX + distance - explosion.getWidth();
				y = explosionY + distance - explosion.getHeight();
				break;
			case "diagDL":
				x = explosionX - distance;
				y = explosionY + distance - explosion.getHeight();
				break;
			default:
				break;
			}
			if(x < 0)
				x = 0;
			if(x > Main.WIDTH - explosion.getWidth())
				x = Main.WIDTH - explosion.getWidth();
			if(y < 0)
				y = 0;
			if(y > Main.HEIGHT - explosion.getHeight())
				y = Main.HEIGHT - explosion.getHeight();
			paint(g, x, y, explosion);
			setEXLoc(x);
			setEYLoc(y);
		}
		if (explosionTimer.getTime() >= 2)
		{			
			showExplosion = false;
			explosionTimer.resetTimer();
		}
		for (int i = 0; i < enemies.size(); i++) 
		{
			if (enemies.elementAt(i).getLocation().intersects(getELocation())) 
			{
				enemies.elementAt(i).decrementHealth();
				if (enemies.elementAt(i).getHealth() == 0)
					Main.score += 100;
			}
		}
	}
}
