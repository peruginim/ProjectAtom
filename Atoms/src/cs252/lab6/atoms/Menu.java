package cs252.lab6.atoms;

import java.net.Socket;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity{

	String classes[] = {"Local Game", "Network Game", "High Scores", "Instructions", "Settings", "Exit"};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String cheese = classes[position];
		
		if(cheese.equals("Local Game")){
			cheese = "Game";
		}
		if(cheese.equals("Network Game")){
			cheese = "Game";
			Game.isNetwork = true;
			new connectGame().execute();
			
		}
		if(cheese.equals("Exit")){
			android.os.Process.killProcess(android.os.Process.myPid());
		}

		
		try {
			Class room = Class.forName("cs252.lab6.atoms." + cheese);
			Intent malicious = new Intent(Menu.this, room);
			startActivity(malicious);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private class connectGame extends AsyncTask<Void, Integer, Void>
	{
		protected Void doInBackground(Void... arg0) 
		{
			try
			{
				Game.s = new Socket(Game.SERVER_PATH, Game.SERVER_PORT);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}
	        

}
