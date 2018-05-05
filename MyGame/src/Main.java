import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends Canvas implements Runnable {

	public static final String TITLE = "MyGame";
	public static final int WIDTH = 1200;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static boolean running;
	private BufferStrategy bs = getBufferStrategy();
	private Player player;
	private static Vector<Enemy> enemies;
	private Friends friends;
	private static int numEnemies = 1;
	private Weapons weapons;
	private Level level;
	//public static boolean gameStarted = false;
	private boolean levelComplete = true;
	private Menu menu;
	private int wave = 1;
	private Timer starPowerTimer = new Timer();
	private Timer backgroundTimer = new Timer();
	public static double score = 0;
	private int HUDweaponOutline = 105;
	private Ammunition ammo = new Ammunition();

	public Main() 
	{
		friends = new Friends();
		Level.createBackground("Backgrounds/neverLoseHope.jpg");
		menu = new Menu();
		player = new Player(WIDTH / 2, HEIGHT / 2);
		fillEnemies();
		weapons = new Weapons();
		addKeyListener(new Input());
	}
	
	public static void resetLevel()
	{
		fillEnemies();
	}
	
	public static void fillEnemies()
	{
		enemies = new Vector<Enemy>();
		for(int i = 0; i < numEnemies; i++)
		{
			int randX, randY;
			int spawn = (int) (Math.random() * 4 + 1);
			if (spawn == 1)
			{
				randX = (int)(Math.random() * (WIDTH - 54));
				randY = -64;
			}
			else if (spawn == 2)
			{
				randX = WIDTH;
				randY = (int)(Math.random() * (HEIGHT - 64));
			}
			else if (spawn == 3)
			{
				randX = (int)(Math.random() * (WIDTH - 54));
				randY = HEIGHT;
			}
			else
			{
				randX = -54;
				randY = (int)(Math.random() * (HEIGHT - 64));
			}
			enemies.addElement(new Enemy(randX, randY, spawn));
		}
	}
	
	public boolean levelComplete()
	{
		levelComplete = true;
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.elementAt(i).getHealth() > 0)
				levelComplete = false;
		}
		return levelComplete;
	}
	
	public void start() {
		if (running)
			return;
		running = true;
		new Thread(this, "Main-Thread").start();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

	private void move() {
		for(int i = 0; i < enemies.size(); i++)
			charactersIntersect(enemies.elementAt(i));
		weapons.pickupMG(player.getLocation());
		weapons.pickupFT(player.getLocation());
		weapons.pickupSP(player.getLocation());
		weapons.pickupG(player.getLocation());
		ammo.pickupFTcan(player.getLocation());
		ammo.pickupMGcase(player.getLocation());
		ammo.pickupBomber(player.getLocation());
		if (Input.isKeyDown(KeyEvent.VK_RIGHT) && (int) player.getXLoc() < WIDTH - player.getWidth()) {
			player.moveX(player.getSpeed());
			player.move("Right");
		}
		if (Input.isKeyDown(KeyEvent.VK_LEFT) && (int) player.getXLoc() > 0) {
			player.moveX(-player.getSpeed());
			player.move("Left");     
		}
		if (Input.isKeyDown(KeyEvent.VK_UP) && (int) player.getYLoc() > 0) {
			player.moveY(-player.getSpeed());
			player.move("Up");
		}
		if (Input.isKeyDown(KeyEvent.VK_DOWN) && (int) player.getYLoc() < HEIGHT - player.getHeight()) {
			player.moveY(player.getSpeed());
			player.move("Down");
		}
		if (Input.isKeyDown(KeyEvent.VK_UP) && Input.isKeyDown(KeyEvent.VK_LEFT)){
			player.move("diagUL");
		}
		if (Input.isKeyDown(KeyEvent.VK_UP) && Input.isKeyDown(KeyEvent.VK_RIGHT)){
			player.move("diagUR");
		}
		if (Input.isKeyDown(KeyEvent.VK_DOWN) && Input.isKeyDown(KeyEvent.VK_RIGHT)){
			player.move("diagDR");
		}
		if (Input.isKeyDown(KeyEvent.VK_DOWN) && Input.isKeyDown(KeyEvent.VK_LEFT)){
			player.move("diagDL");
		}
		Enemy.moveEnemies(enemies);
		if (friends.bomberAuthorized())
			friends.moveBomber();
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void decideWeapon(Graphics g)
	{
		if (weapons.getPistolBool())
			usePistol(g);
		else if (weapons.getMGBool())
		{
			HUDweaponOutline = 187;
			if (Ammunition.getMGammo() > 0)
				useMachineGun(g);
		}
		else if (weapons.getFTBool()){
			HUDweaponOutline = 275;
			if (Ammunition.getFTammo() > 0)
				useFlameThrower(g);
		}
	}
	
	public void usePistol(Graphics g)
	{
		weapons.usePistol(g, enemies, player);
	}
	
	public void useMachineGun(Graphics g)
	{
		weapons.useMachineGun(g, enemies, player);
	}
	
	public void useFlameThrower(Graphics g)
	{
		weapons.useFlameThrower(g, enemies, player);
	}
	
	public void throwGrenade(Graphics g)
	{
		weapons.throwGrenade(g, enemies, player);
	}

	@Override
	public void run() {
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		//while(!gameStarted)
			//menu.display();
		//menu.dispose();
		requestFocus();
		backgroundTimer.startTimer();
		Audio.playBackground();
		double target = 60;
		double nsPerTick = 1000000000 / target;
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double unprocessed = 0;
		int fps = 0;
		int tps = 0;
		boolean canRender = false;
		while (running) 
		{			
			if(backgroundTimer.getTime() > 320)
			{
				Audio.playBackground();
				backgroundTimer.resetTimer();
			}
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			if(unprocessed >= 1)
			{
				unprocessed--;
				tps++;
				canRender = true;
			}
			else
				canRender = false;
			if(canRender)
			{
				if(levelComplete())
				{
					numEnemies += 3;
					fillEnemies();
					wave++;
				}
				render();
				move();
			}
			if (System.currentTimeMillis() - 1000 > timer)
			{
				timer += 1000;
				fps = 0;
				tps = 0;
			}
			//move();
		}
		Audio.stopBackground();
		menu.displayGameOver(wave, (int)score);
	}

	public void render() {
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(Level.getBackground(), 0, 0, null);
		player.paint(g, (int) player.getXLoc(), (int) player.getYLoc());
		for(int i = 0; i < enemies.size(); i++)
			enemies.elementAt(i).paint(g, (int) enemies.elementAt(i).getXLoc(), (int) enemies.elementAt(i).getYLoc());
		friends.authorizeBomber();
		if (friends.bomberAuthorized())
		{
			friends.dropBomb(g, enemies);
			friends.paint(g, (int) friends.getBomberXLoc(), (int) friends.getBomberYLoc(), friends.getBomber());
		}
		System.out.println(friends.getBomberXLoc());
		displayWeapons(g);
		if (Weapons.useStarPower)
		{
			player.setSpeed(6);
			if (!starPowerTimer.getTimeStarted())
				starPowerTimer.startTimer();
			if (starPowerTimer.getTime() > 10)
			{
				starPowerTimer.stopTimer();
				Weapons.useStarPower = false;
			}
		}
		else
			player.setDefaultSpeed();
		
		paintHUD(g);
		
		weapons.displaySP(player.getLocation());
		weapons.displayG(player.getLocation());
		ammo.displayFTcan(player.getLocation());
		ammo.displayMGcase(player.getLocation());
		ammo.displayBomber(player.getLocation());
		ammo.paintFTcanister(g, ammo.getFTcanXLoc(), ammo.getFTcanYLoc());
		ammo.paintMGammoCase(g, ammo.getMGcaseXLoc(), ammo.getMGcaseYLoc());
		ammo.paintBomber(g, ammo.getBomberXloc(), ammo.getBomberYloc());
		weapons.paint(g, (int)weapons.getSPXLoc(), (int)weapons.getSPYLoc(), weapons.getSPimage());
		
		weapons.paint(g, (int)weapons.getGXLoc(), (int)weapons.getGYLoc(), weapons.getGimage());
		decideWeapon(g);
		throwGrenade(g);
		Input.update();
		g.dispose();
		bs.show();
	}
	
	public void displayWeapons(Graphics g)
	{
		if(wave >= 2 && !weapons.getMGBool() && !weapons.getIsMGDisp())
		{
			int randX = (int)(Math.random() * (Main.WIDTH - weapons.getMGimage().getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - weapons.getMGimage().getHeight()));
			weapons.setMGXLoc(randX);
			weapons.setMGYLoc(randY);
			weapons.setMGLocation();
			weapons.setIsMGDisp(true);
		}
		weapons.paint(g, (int)weapons.getMGXLoc(), (int)weapons.getMGYLoc(), weapons.getMGimage());
		if(wave >= 3 && !weapons.getFTBool() && !weapons.getIsFTDisp())
		{
			int randX = (int)(Math.random() * (Main.WIDTH - weapons.getMGimage().getWidth()));
			int randY = (int)(Math.random() * (Main.HEIGHT - weapons.getMGimage().getHeight()));
			weapons.setFTXLoc(randX);
			weapons.setFTYLoc(randY);
			weapons.setFTLocation();
			weapons.setIsFTDisp(true);
		}
		weapons.paint(g, (int)weapons.getFTXLoc(), (int)weapons.getFTYLoc(), weapons.getFTimage());
	}
	
	public void outlineWeapon()
	{
		Graphics2D gg = (Graphics2D) bs.getDrawGraphics();
		gg.setColor(Color.ORANGE);
		gg.draw3DRect(HUDweaponOutline, HEIGHT - 100, 70, 50, false);
		if (Input.wasKeyPressed(KeyEvent.VK_1)) 
		{
			HUDweaponOutline = 105;
			weapons.setPistolBool(true);
			weapons.setMGBool(false);
			weapons.setFTBool(false);
		}
		if (Input.wasKeyPressed(KeyEvent.VK_2) && weapons.getMGAuthorization()) 
		{
			HUDweaponOutline = 187;
			weapons.setMGBool(true);
			weapons.setPistolBool(false);
			weapons.setFTBool(false);
		}		
		if (Input.wasKeyPressed(KeyEvent.VK_3) && weapons.getFTAuthorization()) 
		{
			HUDweaponOutline = 275;
			weapons.setFTBool(true);
			weapons.setPistolBool(false);
			weapons.setMGBool(false);
		}		
	}
	
	public void paintHUD(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.setFont(new Font("Algerian", Font.PLAIN, 50));
		g.drawString("Lives: " + String.valueOf(player.getLives()), 50, 50);
		FontMetrics fm = g.getFontMetrics();
		int x = WIDTH - fm.stringWidth(String.valueOf((int)score)) - 50;
		g.drawString(String.valueOf((int)score), x, 50);
		score += .01;
		x = (WIDTH - fm.stringWidth("Wave: " + String.valueOf((int)wave))) / 2;
		g.drawString("Wave: " + String.valueOf((int)wave), x, 50);
		weapons.paint(g, 50, HEIGHT - 100, weapons.getGimage());
		weapons.paint(g, 125, HEIGHT - 80, weapons.getPimage());
		weapons.paint(g,  190, HEIGHT - 82, weapons.getMGimage());
		weapons.paint(g,  280, HEIGHT - 84, weapons.getFTimage());
		ammo.paintBomber(g, 357, HEIGHT - 90);
		outlineWeapon();
		g.setFont(new Font("Algerian", Font.PLAIN, 30));
		g.drawString("X" + String.valueOf((int)weapons.getNumGrenades()), 45, HEIGHT - 110);
		g.drawString("X" + String.valueOf((int)ammo.getMGammo()), 190, HEIGHT - 110);
		g.drawString("X" + String.valueOf((int)ammo.getFTammo()), 280, HEIGHT - 110);
		g.drawString("X" + String.valueOf((int)ammo.getBombers()), 370, HEIGHT - 110);
		g.setColor(Color.GREEN);  
		g.setFont(new Font("Algerian", Font.PLAIN, 20));
		for (int i = 0; i < enemies.size(); i++)
		{
			if (enemies.elementAt(i).getHealth() > 0)
				g.drawString(String.valueOf(enemies.elementAt(i).getHealth()), 
					(int)enemies.elementAt(i).getXLoc() + 15, 
					(int)enemies.elementAt(i).getYLoc() - 5);
		}
	}
	
	public void charactersIntersect(Enemy e)
	{
		e.charactersIntersect(player.getLocation(), player);
	}

	public static void main(String[] args) {
		/*Main game = new Main();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);*/
		Menu menu2 = new Menu();
		menu2.displayIntro();
		
		//game.start();
	}

}
