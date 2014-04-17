package com.example.test;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class AccidentWarning extends Activity {
	 MediaPlayer mp ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accident_warning);
		
		
	mp	= MediaPlayer.create(AccidentWarning.this, R.raw.alarm);	
		notification();
		
		
		
	}

	private void notification() {
	mp.setLooping(true);
	mp.start();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Emergeny Message"); // Set Alert dialog title
											// here
		 alert.setMessage("Is everything OK?"); 

		
		
		
		 
		alert.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.cancel();
					}
					// End of onClick(DialogInterface dialog, int
					// whichButton)
				}); // End of alert.setPositiveButton
		alert.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// Canceled.
						dialog.cancel();
					mp.stop();
						
					}
				}); // End of alert.setNegativeButton
		AlertDialog alertDialog = alert.create();
		alertDialog.show();

Toast.makeText(getApplicationContext(), "accident", Toast.LENGTH_SHORT).show();				

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accident_warning, menu);
		return true;
	}

}
