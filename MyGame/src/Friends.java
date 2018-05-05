import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Friends {
	
	private BufferedImage bomber;
	private BufferedImage napalm;
	private double bomberXloc, bomberYloc, napalmXloc, napalmYloc;
	private Rectangle napalmLoc;
	private int bomberDirectionToMove = 4;
	private Timer bomberDirectionTimer = new Timer();
	private Timer napalmTimer = new Timer();
	private Timer bomberTimer = new Timer();
	private int bomberSpeed = 5;
	private boolean bombDropped = false;
	private boolean authorizeBomber = false;
	
	public Friends()
	{
		fillImages();
		bomberXloc = 0;
		bomberYloc = Main.HEIGHT / 2;
		napalmXloc = -200;
		napalmYloc = -200;
	}
	
	public void fillImages()
	{
		try {
			bomber = ImageIO.read(new File("BomberRight.png"));
			napalm = ImageIO.read(new File("napalm.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g, int x, int y, BufferedImage img)
	{
		g.drawImage(img, x, y, null);
	}
	
	public void changeBomberImage(String direction)
	{
		try {
			bomber = ImageIO.read(new File("Bomber" + direction + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void changeNapalmImage(String direction)
	{
		try {
			napalm = ImageIO.read(new File(direction + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void moveBomber()
	{
		if (bomberDirectionTimer.getTime() > 2)
		{
			setBomberDirection((int) (Math.random() * 4 + 1));
			bomberDirectionTimer.resetTimer();
		}
		switch (bomberDirectionToMove){
		case 1: // down
			if (getBomberYLoc() + bomber.getHeight() > Main.HEIGHT)
				setBomberDirection(3);
			adjustBomberY(bomberSpeed);
			changeBomberImage("Down");
			break;
		case 2: // left
			if (getBomberXLoc() < 0)
				setBomberDirection(4);
			adjustBomberX(-bomberSpeed);
			changeBomberImage("Left");
			break;
		case 3: // up
			if (getBomberYLoc() < 0)
				setBomberDirection(1);
			adjustBomberY(-bomberSpeed);
			changeBomberImage("Up");
			break;
		case 4: // right
			if (getBomberXLoc() > Main.WIDTH - bomber.getWidth())
				setBomberDirection(2);
			adjustBomberX(bomberSpeed);
			changeBomberImage("Right");
			break;
		}
	}
	
	public void authorizeBomber()
	{
		if (Input.wasKeyPressed(KeyEvent.VK_B) && Ammunition.getBombers() > 0 && !authorizeBomber)
		{
			bomberDirectionTimer.startTimer();
			napalmTimer.startTimer();
			bomberTimer.startTimer();
			Ammunition.decrementBombers();
			authorizeBomber = true;
		}
		if (bomberTimer.getTime() > 10)
		{
			authorizeBomber = false;
			bomberTimer.resetTimer();
			napalmXloc = -200;
			napalmYloc = -200;
			napalmTimer.resetTimer();
		}
	}
	
	public void dropBomb(Graphics g, Vector <Enemy> enemies)
	{
		if (napalmTimer.getTime() > 1 && !bombDropped)
		{
			napalmXloc = bomberXloc;
			napalmYloc = bomberYloc;
			napalmLoc = getNapalmLocation();
			bombDropped = true;
			Audio.playSound("grenadeAudio.wav");
			if (bomberDirectionToMove == 2 || bomberDirectionToMove == 4)
				changeNapalmImage("napalm");
			else
				changeNapalmImage("napalmUp");
		}
		if (napalmTimer.getTime() > 3)
		{
			napalmTimer.resetTimer();
			bombDropped = false;
		}
		paint(g, (int) napalmXloc, (int) napalmYloc, napalm);
		for (int i = 0; i < enemies.size(); i++)
		{
			if (enemies.elementAt(i).getLocation().intersects(getNapalmLocation()))
			{
				enemies.elementAt(i).decrementHealth();
				if (enemies.elementAt(i).getHealth() == 0)
					Main.score += 100;
			}
		}
	}
	
	public BufferedImage getBomber(){return bomber;}
	public BufferedImage getNapalm(){return napalm;}
	public double getBomberXLoc(){return bomberXloc;}	
	public double getBomberYLoc(){return bomberYloc;}	
	public void adjustBomberX(double x){bomberXloc += x;}	
	public void adjustBomberY(double y){bomberYloc += y;}
	public int getBomberDirection(){return bomberDirectionToMove;}
	public void setBomberDirection(int x){bomberDirectionToMove = x;}
	public boolean bomberAuthorized(){return authorizeBomber;}
	public Rectangle getNapalmLocation(){return new Rectangle((int)napalmXloc, 
			(int)napalmYloc, napalm.getWidth(), napalm.getHeight());}
}
