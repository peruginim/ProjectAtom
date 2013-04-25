package cs252.lab6.atoms;

import java.net.*;
import java.io.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity
{
	public static boolean isNetwork = false;
	public static final String SERVER_PATH = "sslab15.cs.purdue.edu";
	public static final int SERVER_PORT = 8001;
	public static Socket s;
	public static int player = 0;

	
	GameSurface surface;
	
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_game);
        surface = (GameSurface)findViewById(R.id.gameview);
        if(isNetwork)
        {
        	surface.isNetwork = true;
        }
        
    }
    
    protected void onResume()
    {
    	super.onResume();
    	surface.onResume();
    }
    
    protected void onPause()
    {
    	super.onResume();
    	surface.onPause();
    }
}