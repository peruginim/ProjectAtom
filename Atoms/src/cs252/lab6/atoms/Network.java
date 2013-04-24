package cs252.lab6.atoms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Network extends Activity
{	
	List<Map<String, String>> matchList = new ArrayList<Map<String,String>>();
	SimpleAdapter simpleAdpt;
	String name = "";
	String num_players = "2";

	public void onDestroy()
	{
		super.onDestroy();
		new joinMatch().execute("TERM");
	}
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        ListView lv = (ListView) findViewById(R.id.listview);
        simpleAdpt = new SimpleAdapter(this, matchList, android.R.layout.simple_list_item_1, new String[] {"match"}, new int[] {android.R.id.text1});
        new getMatches().execute();
        try
        {
        	Thread.sleep(300);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        simpleAdpt.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	 
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
            	TextView clickedView = (TextView) view;    
            	if(clickedView.getText().equals("+ New Lobby"))
            	{
            		promptName();
            		simpleAdpt.notifyDataSetChanged();
            	}
            	else
            	{
            		new joinMatch().execute(String.valueOf(clickedView.getText()));
            		startGame();
            	}
            }
       });
        lv.setAdapter(simpleAdpt);
    }

	public void promptName()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Create Lobby");
		alert.setMessage("Lobby Name:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String value = input.getText().toString();
			matchList.remove(matchList.size()-1);
			  matchList.add(createMatch("match", value));
			  matchList.add(createMatch("match", "+ New Lobby"));
			  new joinMatch().execute("NEW_GAME " + value + " " + num_players);
		  
		  }
		});
		alert.show();
		
		
		  
	}
	
	public void startGame()
	{
		try {
			Game.isNetwork = true;
			Class room = Class.forName("cs252.lab6.atoms.Game");
			Intent malicious = new Intent(Network.this, room);
			startActivity(malicious);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class getMatches extends AsyncTask<Void, Integer, Void>
	{
		protected Void doInBackground(Void... arg0) 
		{
	        try
	        {
	        	for(int i = 0; i < matchList.size(); i++)
	        	{
	        		matchList.remove(i);
	        	}
	        	
	        	Socket s = new Socket(Game.SERVER_PATH, Game.SERVER_PORT);
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(s.getOutputStream());
	        	String choice = "";
	        	while((choice = in.readLine()) != null)
	        	{
	        		if(choice.equals("EOF"))
	        		{
	        			break;
	        		}
	        		else
	        		{
		        		matchList.add(createMatch("match", choice));
	        		}
	        	}
	        	
	        	matchList.add(createMatch("match", "+ New Lobby"));
	        	
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        return null;
		}
	}
	
	private HashMap<String, String> createMatch(String key, String name) {
	    HashMap<String, String> match = new HashMap<String, String>();
	    match.put(key, name);
	     
	    return match;
	}
	
	private class joinMatch extends AsyncTask<String, Integer, Void>
	{
		protected Void doInBackground(String... arg0) 
		{
	        try
	        {
	        	if(arg0[0].equalsIgnoreCase("TERM"))
	        	{
	        		Socket s = new Socket(Game.SERVER_PATH, Game.SERVER_PORT);
					PrintWriter out = new PrintWriter(s.getOutputStream());
					out.println(arg0[0]);
		        	out.flush();
	        	}
	        	else
	        	{
		        	Socket s = new Socket(Game.SERVER_PATH, Game.SERVER_PORT);
		        	Game.s = s;
					PrintWriter out = new PrintWriter(s.getOutputStream());
					Log.v("!", arg0[0]);
					out.println(arg0[0]);
		        	out.flush();
	        	}
	        	
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        return null;
		}
	}

}
