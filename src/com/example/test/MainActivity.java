package com.example.test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.crashlytics.android.Crashlytics;
import com.example.test.SimpleGestureFilter.SimpleGestureListener;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		SimpleGestureListener {
	Button btnSafe, settings, btnShow, acc;
	ImageView ADM;
	String[] names;
	int Enable;
	String lat = null;
	String lon = null;
	Boolean ADMstatus = true;
	Drawable dr1;
	AppLocationService appLocationService;
	Context mcontext;
	private SimpleGestureFilter detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_main);
		mcontext = MainActivity.this;
		detector = new SimpleGestureFilter(this, this);
		appLocationService = new AppLocationService(MainActivity.this);
		// checkcontacts();
		// dr1 = getResources().getDrawable(R.drawable.on);
		// ADM.setImageDrawable(dr1);

		ADM = (ImageView) findViewById(R.id.IVADM);
		ADM.setImageResource(R.drawable.off);
		final Intent i = new Intent(MainActivity.this, AccidentService.class);
		ADM.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Drawable dr;
				if (ADMstatus) {
					ADM.setImageResource(R.drawable.on);
					startService(i);
					Toast.makeText(getApplicationContext(),
							"Accident Detection Mode ON", Toast.LENGTH_SHORT)
							.show();
					ADMstatus = false;
				} else {
					ADMstatus = true;
					ADM.setImageResource(R.drawable.off);
					stopService(i);
					Toast.makeText(getApplicationContext(),
							"Accident Detection Mode OFF", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		// acc.setOnClickListener(this);
		// settings = (Button) findViewById(R.id.SetButton);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnShow.setOnClickListener(this);
		btnSafe = (Button) findViewById(R.id.btnSafe);
		btnSafe.setOnClickListener(this);

		/*
		 * this is code for blinking button final Animation animation = new
		 * AlphaAnimation(1, 0); // Change alpha // from fully // visible to //
		 * invisible animation.setDuration(250); // duration 250 milliseconds
		 * animation.setInterpolator(new LinearInterpolator()); // do not alter
		 * // animation // rate animation.setRepeatCount(Animation.INFINITE); //
		 * Repeat animation // infinitely
		 * animation.setRepeatMode(Animation.REVERSE); // Reverse animation at
		 * the // end so the button will // fade back in
		 * //btnShow.startAnimation(animation);
		 */

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	public void SendSMS(int type) {

		try {
			Location nwLocation = NWmethod();

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

			String pinpoint = "http://www.maps.google.com/maps?q=" + lat + ","
					+ lon;
			Drawable dr;
			String address = GetAddress(lat, lon);
			DataInsertion obj = new DataInsertion();
			String[] phonenumber = obj.getphonenumbers(getApplicationContext());
			String[] messages = obj.getmessages(getApplicationContext());
			if (type == 1) {
				for (int i = 0; i <= phonenumber.length; i++)
					SmsManager.getDefault().sendTextMessage(phonenumber[i],
							null,
							messages[i] + "Im at: " + address + " " + pinpoint,
							null, null);

			}
			if (type == 2) {

				for (int i = 0; i <= phonenumber.length; i++)
					SmsManager.getDefault().sendTextMessage(phonenumber[i],
							null,
							"I'm Safe and I'm at: " + address + " " + pinpoint,
							null, null);

			}
		} catch (Exception ex) {
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
			// this is new way of starting settings Activity
			Intent s = new Intent(MainActivity.this,
					ListViewContactActivity.class);
			startActivity(s);

			// this was old way starting settings activity
			// Intent s = new Intent(MainActivity.this , Settings.class);
			// startActivity(s);
			break;

		}
		return true;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnShow:
			// NWmethod();
			// v.clearAnimation();
			SendSMS(1);
			// NWmethod();
			// new Send().execute();
			break;
		case R.id.btnSafe:
			SendSMS(2);
			break;
		}
	}

	private Location NWmethod() {
		boolean gps_enabled = false;
		boolean network_enabled = false;

		LocationManager lm = (LocationManager) mcontext
				.getSystemService(Context.LOCATION_SERVICE);

		gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		network_enabled = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Location net_loc = null, gps_loc = null, finalLoc = null;

		if (gps_enabled)
			gps_loc = appLocationService
					.getLocation(LocationManager.GPS_PROVIDER);
		if (network_enabled)
			net_loc = appLocationService
					.getLocation(LocationManager.NETWORK_PROVIDER);

		if (gps_loc != null) {
			finalLoc = gps_loc;

		} else if (net_loc != null) {
			finalLoc = net_loc;

		}
		return finalLoc;
	}

	private class Send extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Sending...");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// SendSMS();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			super.onPostExecute(result);
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
				ret = " ";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = " ";
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
				Toast.makeText(getApplicationContext(), "up",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				// TODO
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		switch (direction) {

		case SimpleGestureFilter.SWIPE_RIGHT:

			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			startActivity(new Intent(MainActivity.this, LoginActivity.class));

			break;

		}

	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub

	}
}