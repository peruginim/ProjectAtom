package cs252.lab6.atoms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class Settings extends Activity implements OnClickListener {
	int content = R.layout.settings;
	
	CheckBox checkboxes[];
	EditText edittexts[];
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(content);
		
		CheckBox player1_bot = (CheckBox) findViewById(R.id.checkBox1);
		CheckBox player1_playing = (CheckBox) findViewById(R.id.checkBox2);
		player1_bot.setChecked(GameSurface.players[0].bot);
		player1_playing.setChecked(true);
		EditText player1 = (EditText) findViewById(R.id.player1);
		player1.setText(GameSurface.players[0].getName());
		player1.setOnClickListener(this);
		
		CheckBox player2_bot = (CheckBox) findViewById(R.id.checkBox3);
		CheckBox player2_playing = (CheckBox) findViewById(R.id.checkBox4);
		player2_bot.setChecked(GameSurface.players[1].bot);
		player2_playing.setChecked(true);
		EditText player2 = (EditText) findViewById(R.id.player2);
		player2.setText(GameSurface.players[1].getName());
		player2.setOnClickListener(this);
		
		CheckBox player3_bot = (CheckBox) findViewById(R.id.checkBox5);
		CheckBox player3_playing = (CheckBox) findViewById(R.id.checkBox6);
		player3_bot.setChecked(GameSurface.players[2].bot);
		player3_playing.setChecked(true);
		EditText player3 = (EditText) findViewById(R.id.player3);
		player3.setText(GameSurface.players[2].getName());
		player3.setOnClickListener(this);
		
		CheckBox player4_bot = (CheckBox) findViewById(R.id.checkBox7);
		CheckBox player4_playing = (CheckBox) findViewById(R.id.checkBox8);
		player4_bot.setChecked(GameSurface.players[3].bot);
		player4_playing.setChecked(true);
		EditText player4 = (EditText) findViewById(R.id.player4);
		player4.setText(GameSurface.players[3].getName());
		player4.setOnClickListener(this);
		
		checkboxes = new CheckBox[4];
		edittexts = new EditText[4];
		checkboxes[0] = player1_bot;
		checkboxes[1] = player2_bot;
		checkboxes[2] = player3_bot;
		checkboxes[3] = player4_bot;
		edittexts[0] = player1;
		edittexts[1] = player2;
		edittexts[2] = player3;
		edittexts[3] = player4;
		
		View back = findViewById(R.id.back_button);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_button:
			new updateSQL().execute();
			new loadSQL().execute();
			finish();
			break;
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
	        	int i = 0;
	        	
	        	for(Player p : GameSurface.players)
	        	{
	        		int tmp = 0;
	        		if(checkboxes[i].isChecked())
	        		{
	        			tmp = 1;
	        		}
	        		
	        		String query = "UPDATE players SET player_name='" + edittexts[i].getText().toString() + "', BOT=" + tmp + " WHERE player_id=" + p.getPlayerID();
	        		Log.v("WRITE_SQL", query);
	        		i++;
	        		out.println(query);
	        		out.flush();
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

	
	private class loadSQL extends AsyncTask<Void, Integer, Void>
	{

		protected Void doInBackground(Void... arg0) 
		{
	        try
	        {
	        	Socket s = new Socket(Splash.SQL_PATH, Splash.SQL_PORT);
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
		        		int weboshi = 0;
		        		if(checkboxes[i].isChecked())
		        		{
		        			weboshi = 1;
		        		}
	        			Player tmp = new Player(Integer.parseInt(player_data[0]), player_data[1], weboshi, colors[i], Integer.parseInt(player_data[4]), Integer.parseInt(player_data[5]), Integer.parseInt(player_data[6]));
	        			Log.v("READ SQL", tmp.toString());
	        			players[i] = tmp;
	        			i++;
		        		
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
