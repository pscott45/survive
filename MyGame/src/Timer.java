
public class Timer {
	private long timeStart;
	private boolean timeStarted = false;
	
	public Timer()
	{}
	
	public void startTimer()
	{
		timeStart = System.currentTimeMillis() / 1000;
		timeStarted = true;
	}
	
	public void stopTimer()
	{
		timeStarted = false;
	}
	
	public int getTime()
	{
		long currentTime = System.currentTimeMillis() / 1000;
		return (int) (currentTime - timeStart);
	}
	
	public void resetTimer()
	{
		timeStart = System.currentTimeMillis() / 1000;
	}
	
	public boolean getTimeStarted()
	{
		return timeStarted;
	}

}
