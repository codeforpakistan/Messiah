package com.example.test;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
		
		names = new String[3];
		
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
			double lat=0.0,longi=0.0;
			
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria cri= new Criteria();
			cri.setAccuracy(Criteria.ACCURACY_FINE);
					
			String bbb = locationManager.getBestProvider(cri, true);
			Location myLocation = locationManager.getLastKnownLocation(bbb);

			lat = myLocation.getLatitude();
			longi = myLocation.getLongitude();
			
			String pinpoint = "http://www.maps.google.com/maps?q="+lat+","+longi;
			
			SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
			int Contacts = 0;
			
			for(int i =0; i<3; i++)
			{
				String val = prefs.getString("contactNumber"+i, "");
				if(!val.equals(""))
				{
					Contacts++;
					names[i] = val;
					Log.d("Name:", names[i]);
				}
				
			}
			
			String[] msgs = new String[3];
			String number = "";
			
			
			for(int i =0; i<Contacts; i++)
			{
				if(!names[i].equals(""))
				{
					number = names[i];
					msgs[i] = prefs.getString(names[i]+"Mes", "");
					SmsManager.getDefault().sendTextMessage(number, null, msgs[i] + "Im at:" + pinpoint, null, null);
				}
				
			}
			
			
			
		}
		catch(Exception ex)
		{
			Log.d("SMS Error: ", ex.getMessage().toString());
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
