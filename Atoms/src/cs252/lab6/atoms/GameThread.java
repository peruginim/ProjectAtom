package cs252.lab6.atoms;

public class GameThread extends Thread
{
	private volatile boolean running = false;
	private GameSurface surface;
	private long sleepTime;
	
	public GameThread(GameSurface surface, long sleepTime)
	{
		super();
		this.surface = surface;
		this.sleepTime = sleepTime;
	}
	
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	
	public void run()
	{
		while (running)
		{
			try
			{
				sleep(sleepTime);
				surface.update();
			}
			catch(InterruptedException e) { e.printStackTrace(); }
		}
	}
}