package com.CFP.messiah;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.bu;

public class Verification extends Activity {
	EditText Verfication;
	Button Verify;
	String PhoneNumber, VerficationCode;
	TextView tv1, tv2, tv3, tv4;
	int status, status1;
	SharedPreferences users;
	Editor editor;
	double Latitude, Longitude;
	String lat, lon;
	Device_GPS_Coords obj;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	GoogleCloudMessaging gcm;
	static String regid = "";
	Context context;
	static String SENDER_ID = "344765759058";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.verification);
		tv1 = (TextView) findViewById(R.id.TVverification);
		tv2 = (TextView) findViewById(R.id.TVcode);
		tv3 = (TextView) findViewById(R.id.TVreceive);
		tv4 = (TextView) findViewById(R.id.TVfrom);
		Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
		Typeface font1 = Typeface.createFromAsset(getAssets(), "rcb.ttf");
		tv1.setTypeface(font1);
		tv2.setTypeface(font);
		tv3.setTypeface(font);
		tv4.setTypeface(font);
		obj = new Device_GPS_Coords(Verification.this);
		obj.locationContinousUpdate();
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		editor = users.edit();
		PhoneNumber = users.getString("Phonenumber", null);
		context = Verification.this;
		Verfication = (EditText) findViewById(R.id.etVerfication);
		Verify = (Button) findViewById(R.id.btnVerify);
		Verfication.setTypeface(font);
		Verify.setTypeface(font);
		Verify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				VerficationCode = Verfication.getText().toString();
				if (VerficationCode.length() == 7) {
					if (CheckNetwork.isInternetAvailable(Verification.this)) {
						new SetConnection().execute();

					} else {

						Toast.makeText(getApplicationContext(),
								"No Internet Connection", Toast.LENGTH_SHORT)
								.show();

					}
				} else
					Verfication.setError("Invalid Verification Code");
			}

		});

		 registergcm();
		
	}

	private void doverifiy(String PhoneNumber, String VerficationCode) {

		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.Verification(PhoneNumber,
				VerficationCode);
		Log.v("Error", json.toString());
		// WebApiCaller caller = new WebApiCaller();
		// JSONObject object = caller.SignIn(Username, Password);

		try {
			status = json.getInt("Status");// object.getBoolean("Status");

			if (status == 1) {
				Log.d("Verification", "User Verified");

			} else {
				Log.d("Verification", "Invalid");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startnextactivity() {
		if (status == 1) {
			startActivity(new Intent(Verification.this, MainActivity.class));
		} else {

			Toast.makeText(getApplicationContext(), "ERROR", 5000).show();
		}

	}

	private void sendlocation() {

		Latitude = obj.getUserLat();
		Longitude = obj.getUserLongt();
		lat = String.valueOf(Latitude);
		lon = String.valueOf(Longitude);
		Log.d("lat", lat);
		Log.d("lon", lon);
		// GPSTracker gps = new GPSTracker(Verification.this);
		//
		// do{
		// if (gps.canGetLocation()) {
		//
		// Latitude = gps.getLatitude();
		// Longitude = gps.getLongitude();
		// lat = String.valueOf(Latitude);
		// lon = String.valueOf(Longitude);
		// Log.v("lat", lat);
		// Log.v("lon", lon);
		// }
		// }while((Latitude==0.00000000000000)&&(Longitude==0.00000000000000));

		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.SendLocation(PhoneNumber, lat, lon);
		Log.e("Error", json.toString());
		// WebApiCaller caller = new WebApiCaller();
		// JSONObject object = caller.SignIn(Username, Password);

		try {
			status1 = json.getInt("Status");// object.getBoolean("Status");

			if (status1 == 1) {
				Log.d("Location", "Location Updated");

			} else {
				Log.d("Location", "Location not Updated");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void registergcm() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(Verification.this);
			regid = getRegistrationId(context);
			
			Log.d("RegID", "RegId is intialized");
			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.d("Messiah", "No valid Google Play Services APK found.");
		}

	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			protected String doInBackground(Void... params) {

				String msg = "";
				String GCMregID = "";
				try {

					if (gcm == null) {
						Log.d("Background", "GCM is null");
						gcm = GoogleCloudMessaging.getInstance(context);
						Log.d("GCM", "GCM is intialized");
					}

					
					gcm.unregister();
					GCMregID = gcm.register(SENDER_ID);
					
					Log.d("Background RegID", regid);

					msg = "Device registered, registration ID=" + regid;
					
					storeRegistrationId(GCMregID);

				} catch (IOException ex) {
					// msg = "Error :" + ex.getMessage();
					Log.e("GCM", ex.toString());
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}
		}.execute(null, null, null);

	}

	private void sendRegistrationId(String regid) {
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.SendGCMId(PhoneNumber, regid);
	}

	private void storeRegistrationId(String regid) {
		editor.putString("registration_id", regid);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		String registrationId = users.getString("registration_id", "");
		if (registrationId.isEmpty()) {

			return "";
		}

		return registrationId;
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.d("checkPlayServices", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private class SetConnection extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Verification.this);
			dialog.setMessage("Please Wait...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			

			doverifiy(PhoneNumber, VerficationCode);

			if (status == 1) {
				editor.putBoolean("Verfication" , true).commit();
				sendlocation();
				sendRegistrationId(users.getString("registration_id", ""));
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				startnextactivity();
			}

		}

	}

	@Override
	public void onBackPressed() {

	}
}