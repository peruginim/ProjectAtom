package cs252.lab6.atoms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView
{
	private GameThread thread = null;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private SurfaceHolder holder;
	private int size;
	private int yOffset;
	public static Player[] players;
	private Atom[][] grid;
	private Point[][] strays;
	private int straysPos;
	private int numStrays;
	private int turn;
	private boolean exploding;
	private boolean gameOver;
	private Player nullPlayer;
	private boolean updated = false;
	public boolean isNetwork = false;
	public static String net_move = null;
	
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
		//players = new Player[4];
		
		int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
		//players[0] = new Player("me", Color.RED, false);
		
		if(isNetwork)
		{
			Player loc = players[0];
			Player rem = players[1];
			players = null;
			players = new Player[2];
			players[0] = loc;
			players[1] = rem;
			players[0].setName("Local Player");
			players[0].bot = false;
			players[0].setColor(Color.GREEN);
			players[1].setName("Remote Player");
			players[1].setColor(Color.RED);
			players[1].bot = false;
			
			//new postMove().execute("P_NUM");
			//new fetchMove().execute();
			
			//if(net_move != null)
			//{
				//Game.player = Integer.parseInt(net_move);
			//}
			Log.v("!", Game.player + "");
			if(Game.player == 2)
			{
				turn = 1;
			}

		}
		
		/*if(!isNetwork)
		{
			int bot_num = 1;
			if(players.length < 4)
			{
				for(int i=(players.length-1);i<4;i++)
					players[i] = new Player("Bot " + bot_num, colors[i], true);
			}
		}*/
		
		nullPlayer = new Player("null", Color.GRAY, true);
		
		grid = new Atom[5][5];
		
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				int rings = 3;
				if(i == 0 || i == 4) rings--;
				if(j == 0 || j == 4) rings--;
				grid[i][j] = new Atom(nullPlayer, rings);
			}
		}
		
		strays = new Point[1024][];
		straysPos = 0;
		numStrays = 0;
		if(!isNetwork)
		{
			turn = 0;
		}
			exploding = false;
		gameOver = false;
		holder = getHolder();
		thread = new GameThread(this, holder);
		thread.setRunning(true);
		thread.start();
		if(Game.player == 2)
		{
			new fetchMove().execute();
		}
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
	
	public void botTurn()
	{
		try
		{
			Thread.sleep(700); //Gives the appearance of the game "Thinking"...
		}
		catch(Exception e){}
		
		int x;
		int y;
		
		do
		{
			x = (int)(Math.random() * 5);
			y = (int)(Math.random() * 5);
		}
		while(grid[x][y].getPlayer() != nullPlayer && grid[x][y].getPlayer() != players[turn]);
		
		explode(x, y);
	}
	
	public void explode(int x, int y)
	{
		exploding = true;
		grid[x][y].setPlayer(players[turn]);
		grid[x][y].addElectron();
		checkWin();
		if(grid[x][y].getRings() < grid[x][y].getNum())
		{
			grid[x][y].sendOutElectrons();
			if(x > 0) sendStray(x, y, x-1, y);
			if(x < 4) sendStray(x, y, x+1, y);
			if(y > 0) sendStray(x, y, x, y-1);
			if(y < 4) sendStray(x, y, x, y+1);
		}
		
		if(numStrays == 0)
			nextTurn();
	}
	
	public void sendStray(int x, int y, int dx, int dy)
	{
		float nucSize = size / 4;
		float ringInc = size / 16;
		
		int elec = grid[x][y].getOutermostElec();
		double electron = grid[x][y].getElecs()[elec];
		Point[] points = new Point[2];
		points[0] = new Point((int)((x + 0.5) * size + (nucSize + ringInc * elec) * Math.cos(electron)), (int)(((y + 0.5) * size + (nucSize + ringInc * elec) * Math.sin(electron)) + yOffset));
		points[1] = new Point(dx, dy);
		strays[straysPos++] = points;
		numStrays++;
	}
	
	public void nextTurn()
	{
		straysPos = 0;
		exploding = false;
		turn = (turn + 1) % players.length;
		
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				if(grid[i][j].getPlayer() == nullPlayer || grid[i][j].getPlayer() == players[turn])
				{
					if(players[turn].isBot())
					{
						botTurn();
					}
					
					if(isNetwork)
					{
						if(turn == 1)
						{
								Log.v("!", "GOT " + net_move);
								if(net_move != null)
								{
									int x = Integer.parseInt(net_move.split(" ")[0]);
									int y = Integer.parseInt(net_move.split(" ")[1]);
									net_move = null;
									if(!gameOver && !exploding && x >= 0 && x < 5 && y >= 0 && y < 5 && (grid[x][y].getPlayer() == nullPlayer || grid[x][y].getPlayer() == players[turn]))
									{
										explode(x, y);
									}
								}
							
						}
					}
					
					return;
				}
		
		nextTurn();
	}
	
	public void checkWin()
	{
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				if(grid[i][j].getPlayer() == nullPlayer || grid[i][j].getPlayer() != players[turn])
					return;
		
		gameOver = true;
		if(!updated)
		{
			updated = true;
			new updateSQL().execute();
		}
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		if(isNetwork && turn == 1)
		{
			return false;
		}
		
		int x = (int)(event.getX() / size);
		int y = (int)((event.getY() - yOffset) / size);
		
		if(!gameOver && !exploding && x >= 0 && x < 5 && y >= 0 && y < 5 && (grid[x][y].getPlayer() == nullPlayer || grid[x][y].getPlayer() == players[turn]))
		{
			if(isNetwork)
			{
				new postMove().execute(x + " " + y);
				new fetchMove().execute();
				
			}
			explode(x, y);
			
		}
		
		return false;
	}
	
	public void updateStates()
	{
		float nucSize = size / 4;
		float ringInc = size / 16;
		float straySpeed = size / 10;
		
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				grid[i][j].moveElectrons();
		
		for(int i=0;i<strays.length;i++)
		{
			if(strays[i] != null)
			{
				Point[] elec = strays[i];
				int outermost = grid[elec[1].x][elec[1].y].getOutermostElec();
				double electron = grid[elec[1].x][elec[1].y].getElecs()[outermost];
				double dx = (elec[1].x + 0.5) * size + (nucSize + ringInc * outermost) * Math.cos(electron);
				double dy = (elec[1].y + 0.5) * size + (nucSize + ringInc * outermost) * Math.sin(electron) + yOffset;
				double theta = Math.atan2(dy - elec[0].y, dx - elec[0].x);
				double distX = straySpeed * Math.cos(theta);
				double distY = straySpeed * Math.sin(theta);
				if(dx - elec[0].x < straySpeed && dy - elec[0].y < straySpeed)
				{
					explode(elec[1].x, elec[1].y);
					strays[i] = null;
					if(--numStrays == 0)
						nextTurn();
				}
				else
				{
					elec[0].x += distX;
					elec[0].y += distY;
				}
			}
		}
	}
	
	public void drawAtom(Canvas canvas, float x, float y, Atom atom)
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
		try
		{
			float elecSize = size / 32;
			
			size = this.getWidth() / 5;
			yOffset = (int)((this.getHeight() / 2) - (size * 2.5));
			
			canvas.drawARGB(255, 48, 74, 97);
			
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTypeface(Typeface.SANS_SERIF);
			paint.setStrokeWidth(0);
			paint.setTextSize((float)(size / 1.5));
			paint.setColor(Color.RED);
			canvas.drawText("Atoms!", this.getWidth() / 2, yOffset / 2, paint);
			paint.setTextSize(size / 3);
			paint.setColor(players[turn].getColor());
			if(!gameOver)
			{
				canvas.drawText(players[turn].getName() + "'s turn", this.getWidth() / 2, this.getHeight() - (yOffset / 2), paint);
			}
			else
			{
				canvas.drawText(players[turn].getName() + " has won!", this.getWidth() / 2, this.getHeight() - (yOffset / 2), paint);
			}
			paint.setStrokeWidth((float)1.5);
			
			for(int i=0;i<5;i++)
				for(int j=0;j<5;j++)
					drawAtom(canvas, (float)((i + 0.5) * size), (float)(yOffset + (j + 0.5) * size), grid[i][j]);
			
			for(int i=0;i<strays.length;i++)
			{
				if(strays[i] != null)
				{
					Point[] elec = strays[i];
					paint.setColor(players[turn].getColor());
					paint.setStyle(Paint.Style.FILL);
					canvas.drawCircle((float)elec[0].x, (float)elec[0].y, elecSize, paint);
					paint.setColor(Color.rgb(50, 50, 50));
					paint.setStyle(Paint.Style.STROKE);
					canvas.drawCircle((float)elec[0].x, (float)elec[0].y, elecSize, paint);
				}
			}
		}
		catch(Exception e) {}
	}
	
	private class fetchMove extends AsyncTask<Void, Integer, Void>
	{
		BufferedReader in = null;
		
		protected void onPreExecute()
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(Game.s.getInputStream()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		protected Void doInBackground(Void... arg0)
		{
			String move = "";
			try
			{
				if(Game.s != null && in != null)
				{
					Log.v("!", "TRYING READ");
					while((move = in.readLine()) != null)
					{
						Log.v("!", "READ " + move);
						GameSurface.net_move = move;
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(isNetwork)
			{
				if(turn == 1)
				{
						Log.v("!", "GOT " + net_move);
						if(net_move != null)
						{
							int x = Integer.parseInt(net_move.split(" ")[0]);
							int y = Integer.parseInt(net_move.split(" ")[1]);
							net_move = null;
							if(!gameOver && !exploding && x >= 0 && x < 5 && y >= 0 && y < 5 && (grid[x][y].getPlayer() == nullPlayer || grid[x][y].getPlayer() == players[turn]))
							{
								explode(x, y);
							}
						}
					
				}
			}
			
			return null;
		}

	}
	
	private class postMove extends AsyncTask<String, Integer, Void>
	{
		PrintWriter out = null;
		
		protected void onPreExecute()
		{
			try
			{
				out = new PrintWriter(Game.s.getOutputStream());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		protected Void doInBackground(String... arg0) 
		{
	        try
	        {
	        	Log.v("!", arg0[0] + " ");
	        	out.println(arg0[0]);
	        	out.flush();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        return null;
	    }
	}
	
	private class updateSQL extends AsyncTask<Void, Integer, Void>
	{

		protected Void doInBackground(Void... arg0) 
		{
	        try
	        {
	        	Socket s = new Socket(Splash.SQL_PATH, Splash.SQL_PORT);
	        	PrintWriter out = new PrintWriter(s.getOutputStream());
	        	
	        	for(Player p : players)
	        	{
	        		if(!p.isBot())
	        		{
		        		p.games_played++;
		        		String query = "";
		        		
		        		if(p.equals(players[turn]))
		        		{
		        			p.games_won++;
		        			query = "UPDATE players SET games_played=" + p.games_played + ", games_won=" + p.games_won + " WHERE player_id=" + p.getPlayerID();
		        			out.println(query);
		        			out.flush();
		        		}
		        		else
		        		{
		        			query = "UPDATE players SET games_played=" + p.games_played + " WHERE player_id=" + p.getPlayerID();
		        			out.println(query);
		        			out.flush();
		        		}
	        		}
	        	}
	        	
	        	out.println("TERM");
	        	out.flush();
	        	out.close();
	        	s.close();
	        	
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
			return null;
		}
		
	}
}