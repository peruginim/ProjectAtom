package cs252.lab6.atoms;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class Splash extends Activity
{
	public static final int SQL_PORT = 8000;
	public static final String SQL_PATH = "sslab15.cs.purdue.edu";
	//MediaPlayer music;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		//music = MediaPlayer.create(Splash.this, R.raw.music);
		//music.setLooping(true);
		//music.start();
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMain = new Intent("cs252.lab6.atoms.Menu");
					startActivity(openMain);
				}
			}
		};
		timer.start();
		
		new loadSQL().execute();
	}
	
	protected void onDestroy() 
	{
		
	    super.onDestroy();
	    finish();
	    // The activity is about to be destroyed.
	}
	
	protected void onStop() 
	{
		
	    super.onDestroy();
	    finish();
	    // The activity is about to be destroyed.
	}
	
	private class loadSQL extends AsyncTask<Void, Integer, Void>
	{

		protected Void doInBackground(Void... arg0) 
		{
	        try
	        {
	        	Socket s = new Socket(SQL_PATH, SQL_PORT);
	        	PrintWriter out = new PrintWriter(s.getOutputStream());
	        	BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        	String player = "";
	        	out.println("SELECT * FROM players"); //Raw SQL query
	        	out.flush();
	        	
	        	Player players[] = new Player[4];
	        	int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
	        	int i = 0;
	        	
	        	while((player = in.readLine()) != null)
	        	{
	        		if(player.equalsIgnoreCase("EOF")) //Signal from server that all results have been returned.
	        		{
	        			break;
	        		}
	        		else
	        		{
		        		String player_data[] = player.split(" ");
		        		if(Integer.parseInt(player_data[2]) == 0) //Not a bot.
		        		{
			        			Player tmp = new Player(Integer.parseInt(player_data[0]), player_data[1], 0, colors[i], Integer.parseInt(player_data[4]), Integer.parseInt(player_data[5]), Integer.parseInt(player_data[6]));
			        			Log.v("READ SQL", tmp.toString());
			        			players[i] = tmp;
			        			i++;
		        		}
	        		}
	        	}
	        	
	        	out.println("TERM"); //Signal to close connection
	        	out.flush();
	        	GameSurface.players = players;
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
