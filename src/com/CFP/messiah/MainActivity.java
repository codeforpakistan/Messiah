package com.CFP.messiah;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	Button sms, maps, settings, speeddial;
	private ProgressBar progBar;
	private Handler mHandler = new Handler();;
	private int mProgressStatus = 0;
	String lat = null;
	String lon = null;
	// final GoogleAnalyticsTracker tracker =
	// GoogleAnalyticsTracker.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		// tracker.startNewSession("UA-50966088-1", this);
		progBar = (ProgressBar) findViewById(R.id.progressBar1);
		try {
			sms = (Button) findViewById(R.id.btnSMS);
			sms.setOnClickListener(this);
			maps = (Button) findViewById(R.id.btnMaps);
			maps.setOnClickListener(this);
			speeddial = (Button) findViewById(R.id.btnContact);
			speeddial.setOnClickListener(this);
			settings = (Button) findViewById(R.id.btnSettings);
			settings.setOnClickListener(this);
		} catch (Exception e) {

			Log.v("Error", e.toString());
		}

		// ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
		// Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
		// an.setFillAfter(true);
		// pb.startAnimation(an);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSMS:
			DataInsertion obj = new DataInsertion();
			int count = obj.countcontacts(getApplicationContext());
			if (count > 0)
				Send();
			else
				Toast.makeText(getApplicationContext(), "No Contact Found",
						Toast.LENGTH_LONG).show();
			// tracker.trackEvent(
			// "Button", // Category, i.e. Player Buttons
			// "HELP", // Action, i.e. Play
			// "clicked", // Label i.e. Play
			// 3);
			break;
		case R.id.btnMaps:
			
			
			if (CheckNetwork.isInternetAvailable(MainActivity.this)){
			startActivity(new Intent(MainActivity.this, ImplementMaps.class));
			}
			else{
				Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_SHORT).show();
				
			}
			// tracker.trackEvent(
			// "Button", // Category, i.e. Player Buttons
			// "MAPS", // Action, i.e. Play
			// "clicked", // Label i.e. Play
			// 0);
			break;
		case R.id.btnSettings:
			startActivity(new Intent(MainActivity.this, Settings.class));
			// tracker.trackEvent(
			// "Button", // Category, i.e. Player Buttons
			// "Settings", // Action, i.e. Play
			// "clicked", // Label i.e. Play
			// 1);
			break;
		case R.id.btnContact:
			startActivity(new Intent(MainActivity.this, MessiahContacts.class));
			// tracker.trackEvent(
			// "Button", // Category, i.e. Player Buttons
			// "Speed Dial", // Action, i.e. Play
			// "clicked", // Label i.e. Play
			// 2);
			break;

		}

	}

	private void Send() {
		String address = " ";
		GPSTracker gps = new GPSTracker(MainActivity.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			lon = String.valueOf(longitude);
			lat = String.valueOf(latitude);
			if (CheckNetwork.isInternetAvailable(MainActivity.this)) {

				address = " near by" + GetAddress(lat, lon);
			}
			String pinpoint = "http://www.maps.google.com/maps?q=" + lat + ","
					+ lon;

			DataInsertion obj = new DataInsertion();
			String[] phonenumber = obj.getphonenumbers(getApplicationContext());
			String[] messages = obj.getmessages(getApplicationContext());
			try {
				for (int i = 0; i < phonenumber.length; i++)
					SmsManager.getDefault()
							.sendTextMessage(
									phonenumber[i],
									null,
									messages[i] + " Im at: " + " " + pinpoint
											+ address, null, null);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.i("error of array", e.toString());
			}
		}

	}

	public String GetAddress(String lat, String lon) {
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		String ret = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(
					Double.parseDouble(lat), Double.parseDouble(lon), 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder(
						"Address:\n");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
				}
				ret = strReturnedAddress.toString();
			} else {
				ret = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}

	private void dowork() {
		new Thread(new Runnable() {
			public void run() {
				while (mProgressStatus < 100) {
					mProgressStatus += 1;
					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							progBar.setProgress(mProgressStatus);
						}
					});
					try {
						// Display progress slowly
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void Temp() {
		GPSTracker gps = new GPSTracker(MainActivity.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

	}


}
