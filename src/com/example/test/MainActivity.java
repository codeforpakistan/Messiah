package com.example.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
Button settings,btnShow;
String[] names;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settings = (Button) findViewById(R.id.SetButton);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnShow.setOnClickListener(this);
		
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent s = new Intent(MainActivity.this , Settings.class);
				startActivity(s);
				MainActivity.this.finish();
			}
		});
		
		
		
	}
	
	public void SendSMS()
	{
		try{
			Bundle bundle = getIntent().getExtras();
			names = bundle.getStringArray("contacts");
			
			String[] msgs = new String[3];
			String number = "";
			
			SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
			for(int i =0; i<3; i++)
			{
				if(!names[i].equals(null))
				{
					number = prefs.getString(names[i], "");
					msgs[i] = prefs.getString(names[i]+"Mes", "");
					SmsManager.getDefault().sendTextMessage(number, null, msgs[i], null, null);
				}
				
			}
			
			
			
		}
		catch(Exception ex)
		{
			//Error
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btnShow:
			new Send().execute();
			break;
		}
	}
	
	private class Send extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Sending...");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			SendSMS();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(dialog.isShowing())
			{
				dialog.dismiss();
			}
			super.onPostExecute(result);
		}
		
		
	}
	

}
