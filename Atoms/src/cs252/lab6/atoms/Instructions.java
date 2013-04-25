package cs252.lab6.atoms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Instructions extends Activity implements OnClickListener {
	int content = R.layout.settings;
	
	public void onCreate(Bundle savedInstanceState) 
	{
super.onCreate(savedInstanceState);
		
		setContentView(content);
		
		View back = findViewById(R.id.button1);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.button1:
			finish();
			break;
		}
	}
}
