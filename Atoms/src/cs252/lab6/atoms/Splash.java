package cs252.lab6.atoms;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity{
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
