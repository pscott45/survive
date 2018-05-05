import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter {
	
	private static final boolean[] keys = new boolean[256];
	private static final boolean[] keys2 = new boolean[256];
	
	@Override
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e){
			keys[e.getKeyCode()] = false;
	}
	
	public static boolean isKeyDown(int keyCode){
		return keys[keyCode];
	}
	
	public static void update() {
		for(int i = 0; i < keys.length; i++)
			keys2[i] = keys[i];
	}
	
	public static boolean wasKeyPressed(int keyCode)
	{
		return isKeyDown(keyCode) && !keys2[keyCode];
	}
	
	public static void resetKeys()
	{
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = false;
			keys2[i] = false;
		}
	}
}
