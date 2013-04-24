package cs252.lab6.atoms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity{

	String classes[] = {"New Game", "Load Game", "Options", "Instructions", "Exit"};

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String cheese = classes[position];
		
		if(cheese.equals("Load Game")){
			cheese = "LoadGame";
		}
		if(cheese.equals("New Game")){
			cheese = "Game";
		}
		if(cheese.equals("Exit")){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		if(cheese.equals("Options"))
		{
			cheese = "Settings";
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

}
