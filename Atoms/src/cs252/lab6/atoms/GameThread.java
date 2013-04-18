package cs252.lab6.atoms;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
	private volatile boolean running = false;
	private GameSurface surface;
	private SurfaceHolder surfaceHolder;
	private final static int MAX_FPS = 60;
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;
	
	public GameThread(GameSurface surface, SurfaceHolder surfaceHolder)
	{
		this.surface = surface;
		this.surfaceHolder = surfaceHolder;
	}
	
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	
	public void run()
	{
		Canvas canvas;
		long beginTime;
		long timeDiff;
		int sleepTime = 0;
		
		while (running)
		{
			canvas = null;
			
			try
			{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized(surfaceHolder)
				{
					beginTime = System.currentTimeMillis();
					this.surface.updateStates();
					this.surface.paint(canvas);
					timeDiff = System.currentTimeMillis() - beginTime;
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if(sleepTime > 0)
					{
						try
						{
							Thread.sleep(sleepTime);
						}
						catch(InterruptedException e) {}
					}
				}
			}
			finally
			{
				if(canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
}