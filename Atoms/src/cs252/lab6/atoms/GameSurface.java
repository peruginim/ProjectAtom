package cs252.lab6.atoms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView
{
	private GameThread thread = null;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private SurfaceHolder holder;
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
	
	public void updateStates()
	{
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				grid[i][j].moveElectrons();
	}
	
	public void drawAtom(Canvas canvas, float x, float y, int size, Atom atom)
	{
		float nucSize = size / 4;
		float neutSize = size / 6;
		float proSize = size / 8;
		float ringInc = size / 16;
		float elecSize = size / 32;
		
		for(int i=0;i<atom.getRings();i++)
		{
			double pro = atom.getNuc() + i * ((2*Math.PI) / 3);
			paint.setColor(atom.getBackColor());
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle((float)(x + proSize * Math.cos(pro)), (float)(y + proSize * Math.sin(pro)), proSize, paint);
			paint.setColor(Color.rgb(50, 50, 50));
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)(x + proSize * Math.cos(pro)), (float)(y + proSize * Math.sin(pro)), proSize, paint);
			paint.setColor(Color.WHITE);
			canvas.drawCircle((float)x, (float)y, nucSize + ringInc * (i + 1), paint);
		}
		
		paint.setColor(atom.getColor());
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(x, y, neutSize, paint);
		paint.setColor(Color.rgb(50, 50, 50));
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(x, y, neutSize, paint);
		
		int elecs = atom.getNum() < atom.getRings() ? atom.getNum() : atom.getRings();
		for(int i=0;i<elecs;i++)
		{
			paint.setColor(atom.getColor());
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle((float)(x + (nucSize + ringInc * (i + 1)) * Math.cos(atom.getElecs()[i])), (float)(y + (nucSize + ringInc * (i + 1)) * Math.sin(atom.getElecs()[i])), elecSize, paint);
			paint.setColor(Color.rgb(50, 50, 50));
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)(x + (nucSize + ringInc * (i + 1)) * Math.cos(atom.getElecs()[i])), (float)(y + (nucSize + ringInc * (i + 1)) * Math.sin(atom.getElecs()[i])), elecSize, paint);
		}
	}
	
	public void paint(Canvas canvas)
	{
		canvas.drawARGB(255, 0, 100, 255);
		
		int size = canvas.getWidth() / 5;
		int yOffset = (int)((canvas.getHeight() / 2) - (size * 2.5));
		
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				drawAtom(canvas, (float)((i + 0.5) * size), (float)(yOffset + (j + 0.5) * size), size, grid[i][j]);
	}
}