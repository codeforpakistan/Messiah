package com.CFP.messiah;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	Button sms, maps, settings, speeddial;
	private ProgressBar progBar;
	private Handler mHandler = new Handler();;
	private int mProgressStatus = 0;
	String lat = null;
	String lon = null;
	TextView tip, maptext, settingstext, contacttext;

	// ShowcaseView sv;
	// final GoogleAnalyticsTracker tracker =
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EasyTracker.getInstance(this).activityStart(this);
		progBar = (ProgressBar) findViewById(R.id.progressBar1);
		try {
			
			
//			LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//			View view = inflater.inflate( R.layout.helpoverlay, null );
//			setContentView(view);
//			view.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//				setContentView(R.layout.activity_main);
//					
//				}
//			});
			tip = (TextView) findViewById(R.id.textView4);
			maptext = (TextView) findViewById(R.id.tvmap);
			settingstext = (TextView) findViewById(R.id.textView2);
			contacttext = (TextView) findViewById(R.id.textView3);
			Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
			tip.setTypeface(font);
			
			maptext.setTypeface(font);
			settingstext.setTypeface(font);
			contacttext.setTypeface(font);
			sms = (Button) findViewById(R.id.btnSMS);
			sms.setOnClickListener(this);
			maps = (Button) findViewById(R.id.btnMaps);
			maps.setOnClickListener(this);
			speeddial = (Button) findViewById(R.id.btnContact);
			speeddial.setOnClickListener(this);
			settings = (Button) findViewById(R.id.btnSettings);
			settings.setOnClickListener(this);
			tip.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							MainActivity.this);
					builder1.setMessage("If your car is on fire, pull over as quickly as it is safe to do so.");
					builder1.setCancelable(true);
					builder1.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();

				}
			});
		} catch (Exception e) {

			Log.v("Error", e.toString());
		}

		// ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
		// Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
		// an.setFillAfter(true);
		// pb.startAnimation(an);

		// ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
		// co.hideOnClickOutside = true;
		// ViewTarget target = new ViewTarget(R.id.btnSMS, this);
		// sv = ShowcaseView.insertShowcaseView(target, this, "Hello",
		// "Welcome", co);
		// sv.setOnShowcaseEventListener(this);

		// sv = (ShowcaseView) findViewById(R.id.showcase);
		// sv.setShowcaseView(findViewById(R.id.btnSMS));
		// sv.setOnShowcaseEventListener(this);
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
		int id = v.getId();
		if (id == R.id.btnSMS) {
			EasyTracker easyTracker = EasyTracker.getInstance(this);

			// MapBuilder.createEvent().build() returns a Map of event fields
			// and values
			// that are set and sent with the hit.
			easyTracker.send(MapBuilder.createEvent("ui_action", // Event
																	// category
																	// (required)
					"button_press", // Event action (required)
					"Settings", // Event label
					null) // Event value
					.build());
			DataInsertion obj = new DataInsertion();
			int count = obj.countcontacts(getApplicationContext());
			if (count > 0)
				Send();
			else
				Toast.makeText(getApplicationContext(), "No Contact Found",
						Toast.LENGTH_LONG).show();
		} else if (id == R.id.btnMaps) {
			EasyTracker easyTracker = EasyTracker.getInstance(this);

			// MapBuilder.createEvent().build() returns a Map of event fields
			// and values
			// that are set and sent with the hit.
			easyTracker.send(MapBuilder.createEvent("ui_action", // Event
																	// category
																	// (required)
					"button_press", // Event action (required)
					"Maps", // Event label
					null) // Event value
					.build());
			if (CheckNetwork.isInternetAvailable(MainActivity.this)) {
				startActivity(new Intent(MainActivity.this, ImplementMaps.class));
			} else {
				Toast.makeText(getApplicationContext(),
						"No Internet Connection", Toast.LENGTH_SHORT).show();

			}
		} else if (id == R.id.btnSettings) {
			EasyTracker easyTracker = EasyTracker.getInstance(this);

			// MapBuilder.createEvent().build() returns a Map of event fields
			// and values
			// that are set and sent with the hit.
			easyTracker.send(MapBuilder.createEvent("ui_action", // Event
																	// category
																	// (required)
					"button_press", // Event action (required)
					"Settings", // Event label
					null) // Event value
					.build());
			startActivity(new Intent(MainActivity.this, Settings.class));
		} else if (id == R.id.btnContact) {
			EasyTracker easyTracker = EasyTracker.getInstance(this);

			// MapBuilder.createEvent().build() returns a Map of event fields
			// and values
			// that are set and sent with the hit.
			easyTracker.send(MapBuilder.createEvent("ui_action", // Event
																	// category
																	// (required)
					"button_press", // Event action (required)
					"Contact", // Event label
					null) // Event value
					.build());
			startActivity(new Intent(MainActivity.this, MessiahContacts.class));
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
