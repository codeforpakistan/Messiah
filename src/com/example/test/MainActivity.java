package com.example.test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.crashlytics.android.Crashlytics;


import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
Button btnSafe,settings,btnShow,acc;
String[] names;
int Enable;
String lat=null;
String lon=null;
AppLocationService appLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_main);
		
		appLocationService = new AppLocationService(
				MainActivity.this);
		acc = (Button) findViewById(R.id.btnService);
		acc.setOnClickListener(this);
		settings = (Button) findViewById(R.id.SetButton);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnShow.setOnClickListener(this);
		btnSafe = (Button) findViewById(R.id.btnSafe);
		btnSafe.setOnClickListener(this);
		final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
		animation.setDuration(250); // duration 250 milliseconds
		animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
		btnShow.startAnimation(animation);
		names = new String[3];
		Enable = -1;
//		SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
//		String val = prefs.getString("contactNumber"+1, "");
//		if(val.equals(""))
//		{
//			btnShow.setEnabled(false);
//			Toast.makeText(this, "Please set the Settings for the Application", Toast.LENGTH_LONG).show();
//		}
	
		
		
		
	}

	
	@Override
		protected void onResume() {
			
//		SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
//		String val = prefs.getString("contactNumber"+1, "");
//		if(!val.equals(""))
//		{
//			btnShow.setEnabled(true);
//		}
		
			super.onResume();
		}
	
	public void SendSMS(int type)
	{
		 
		try{
			Location nwLocation = appLocationService
					.getLocation(LocationManager.NETWORK_PROVIDER);

			if (nwLocation != null) {
				double latitude = nwLocation.getLatitude();
				double longitude = nwLocation.getLongitude();
				
				lon = String.valueOf(longitude);
				lat = String.valueOf(latitude);
				String add = GetAddress(lat, lon);
				Toast.makeText(
						getApplicationContext(),
						"Mobile Location (NW): \nLatitude: " + latitude
								+ "\nLongitude: " + longitude + " " + add,
						Toast.LENGTH_SHORT).show();
			
		}
			
			String pinpoint = "http://www.maps.google.com/maps?q="+lat+","+lon;
			String address = GetAddress(lat, lon);
			DataInsertion obj = new DataInsertion();
			String[] phonenumber = obj.getphonenumbers(getApplicationContext());
			String[] messages = obj.getmessages(getApplicationContext());
			if(type == 1){
			for(int i = 0; i<= phonenumber.length;i++ )
			SmsManager.getDefault().sendTextMessage(phonenumber[i], null, messages[i] + "Im at: " + address + " " + pinpoint, null, null);
			}
			if(type == 2){
				
				for(int i = 0; i<= phonenumber.length;i++ )
				SmsManager.getDefault().sendTextMessage(phonenumber[i], null,"I'm Safe and I'm at: " + address + " " + pinpoint, null, null);
				
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
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
		
		
	}

	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		    // action with ID action_refresh was selected
		    case R.id.action_settings:
		    	//this is new way of starting settings Activity
		    	Intent s = new Intent(MainActivity.this , ListViewDemoActivity.class);
		    	startActivity(s);
		    	
		    	//this was old way starting settings activity
//		    	Intent s = new Intent(MainActivity.this , Settings.class);
//				startActivity(s);
		      break;
		      
		 }
			return true;
					
		}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btnShow:
			//NWmethod();
			v.clearAnimation();
			SendSMS(1);
			//new Send().execute();
			break;
		case R.id.btnService:
			startService(new Intent(MainActivity.this,AccidentService.class));
			break;
		case R.id.btnSafe:
			SendSMS(2);
			break;
		}
	}
	
	private void NWmethod() {
		Location nwLocation = appLocationService
				.getLocation(LocationManager.NETWORK_PROVIDER);

		if (nwLocation != null) {
			double latitude = nwLocation.getLatitude();
			double longitude = nwLocation.getLongitude();
			
			lon = String.valueOf(longitude);
			lat = String.valueOf(latitude);
			String add = GetAddress(lat, lon);
			Toast.makeText(
					getApplicationContext(),
					"Mobile Location (NW): \nLatitude: " + latitude
							+ "\nLongitude: " + longitude + " " + add,
					Toast.LENGTH_SHORT).show();
		
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
			//SendSMS();
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
	public String GetAddress(String lat, String lon)
	{
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		String ret = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
			if(addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				ret = strReturnedAddress.toString();
			}
			else{
				ret = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}
@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	int action = event.getAction();
    int keyCode = event.getKeyCode();
        switch (keyCode) {
        case KeyEvent.KEYCODE_VOLUME_UP:
            if (action == KeyEvent.ACTION_UP) {
                Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT).show();
            }
            return true;
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            if (action == KeyEvent.ACTION_DOWN) {
                //TODO
            }
            return true;
        default:	
        	return super.dispatchKeyEvent(event);
	}
}
}