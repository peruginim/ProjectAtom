package cs252.lab6.atoms;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{
	//MediaPlayer damnson;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//damnson = MediaPlayer.create(Splash.this, R.raw.music);
		//damnson.setLooping(true);
		//damnson.start();
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(500);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMain = new Intent("cs252.lab6.atoms.Game");
					startActivity(openMain);
				}
			}
		};
		timer.start();
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

}
