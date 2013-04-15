package cs252.lab6.atoms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView
{
	private SurfaceHolder holder;
	private GameThread thread = null;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Atom[][] grid;
	
	public GameSurface(Context context)
	{
		super(context);
	}
	
	public GameSurface(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public GameSurface(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public void onResume()
	{
		grid = new Atom[5][5];
		
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				int rings = 3;
				if(i == 0 || i == 4) rings--;
				if(j == 0 || j == 4) rings--;
				grid[i][j] = new Atom(rings);
			}
		}
		
		holder = getHolder();
		thread = new GameThread(this, 500);
		thread.setRunning(true);
		thread.start();
	}
	
	public void onPause()
	{
		boolean retry = true;
		thread.setRunning(false);
		while(retry)
		{
			try
			{
				thread.join();
				retry = false;
			}
			catch(InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	public void update()
	{
		Canvas canvas = null;
		
		try
		{
			canvas = holder.lockCanvas();
			
			synchronized(holder)
			{
				updateStates();
				paint(canvas);
			}
		}
		finally
		{
			if(canvas != null)
				holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void updateStates() {}
	
	public void paint(Canvas canvas)
	{
		paint.setStyle(Paint.Style.FILL);
		int size = 30;
		
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				paint.setColor(grid[i][j].getColor());
				canvas.drawOval(new RectF(i * size, j * size, i * size + size, j * size + size), paint);
			}
		}
	}
}