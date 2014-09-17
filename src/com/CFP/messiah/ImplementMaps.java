package com.CFP.messiah;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ImplementMaps extends FragmentActivity {

	private GoogleMap googleMap;
	Context mcontext;
	double Longitude, Latitude;
	LocationManager mLocationManager;
	String lat, lon;
	String PhoneNumber;
	SharedPreferences users;
	AppLocationService appLocationService;
	Location location;
	JSONArray jsonarray;
	HashMap<String, String> data;
	String NotificationStatus;
	String hisPhoneNumber;
	BitmapDescriptor markericon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		PhoneNumber = users.getString("Phonenumber", null);
		// appLocationService = new AppLocationService(ImplementMaps.this);
		// location = NWmethod();
		markericon = BitmapDescriptorFactory.fromResource(R.drawable.map_loc);
		try {
			// Loading map
			initilizeMap();
			GetLocation();
			new SetConnection().execute();
			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					String m = marker.getId();
					Iterator it = data.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						if (m.equals(pairs.getKey())) {
							hisPhoneNumber = pairs.getValue().toString();
//							Toast.makeText(getApplicationContext(), hisPhoneNumber, Toast.LENGTH_SHORT).show();
//							hisPhoneNumber.substring(1,hisPhoneNumber.length());
//							Toast.makeText(getApplicationContext(), hisPhoneNumber, Toast.LENGTH_SHORT).show();
							new SendNotification().execute();
						}

						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					return true;
				}
			});
			// Temp();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getnearbymessiah() {
		if (CheckNetwork.isInternetAvailable(ImplementMaps.this)) {
			Log.v("lat", lat);
			Log.v("lon", lon);
			UserFunctions userFunction = new UserFunctions();
			jsonarray = userFunction.GetNearbyMessiah(PhoneNumber, lat, lon);

			Log.v("jsontostring", jsonarray.toString());
			Log.v("json length", String.valueOf(jsonarray.length()));

			// jsonarray = new JSONArray(jsonobj.toString());
			//
			// JSONObject jsonobj1 = new JSONObject();
			// for (int i = 0; i < jsonarray.length(); i++) {
			//
			// jsonobj1 = jsonarray.getJSONObject(i);
			// String phonenumber = jsonobj1.getString("PhoneNumber");
			// String Fullname = jsonobj1.getString("FullName");
			// double lat = jsonobj1.getDouble("Latitude");
			// double lon = jsonobj1.getDouble("Longitude");
			// Log.v("converted json", phonenumber + Fullname + lat + lon);
		}

	}

	private void ShowNearbyMessiah() {
		data = new HashMap<String, String>();
		for (int i = 0; i < jsonarray.length(); i++) {

			Log.v("MY Array", jsonarray.toString());
			// JSONArray array1 = jsonarray.getJSONArray(i);
			JSONObject jsonobj = null;
			try {
				jsonobj = jsonarray.getJSONObject(i);
				String PhoneNumber = jsonobj.getString("PhoneNumber");
				PhoneNumber = PhoneNumber.substring(1);
				String FullName = jsonobj.getString("FullName");
				String Latitude = jsonobj.getString("Latitude");
				String Longitude = jsonobj.getString("Longitude");
				Marker m = googleMap.addMarker(new MarkerOptions().position(
						new LatLng(Double.parseDouble(Latitude), Double
								.parseDouble(Longitude))).title(FullName)
								.icon(markericon));
				
				data.put(m.getId(), PhoneNumber);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void GetLocation() {

		// Latitude = location.getLatitude();
		// Longitude = location.getLongitude();
		// lat = String.valueOf(Latitude);
		// lon = String.valueOf(Longitude);
		// Log.v("lat", lat);
		// Log.v("lon", lon);
		GPSTracker gps = new GPSTracker(ImplementMaps.this);
		do {
			if (gps.canGetLocation()) {

				Latitude = gps.getLatitude();
				Longitude = gps.getLongitude();
				lat = String.valueOf(Latitude);
				lon = String.valueOf(Longitude);
				System.out.print(lon + lat);
				Toast.makeText(getApplicationContext(), lat + lon, 5000).show();
			}
		} while ((Latitude == 0.00000000000000)
				&& (Longitude == 0.00000000000000));
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(Latitude, Longitude), 16));
//		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
//				Latitude, Longitude));
//		CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
//
//		googleMap.moventerCamera(c
//		googleMap.animateCamera(zoom);
//		Marker m = googleMap.addMarker(new MarkerOptions().position(
//				new LatLng(Latitude,Longitude)).title("Zahid"));
//		m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon));
		
	}

	private void Temp() {
		GPSTracker gps = new GPSTracker(ImplementMaps.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			googleMap.addMarker(new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Hello world"));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(latitude, longitude), 16));
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			//
			googleMap.setMyLocationEnabled(true);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
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
		Toast.makeText(getApplicationContext(),
				String.valueOf(gps_enabled) + String.valueOf(network_enabled),
				5000).show();
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
public void nomessiahfound(){
	if(jsonarray.length()==0)
		Toast.makeText(getApplicationContext(), "No Messiah found nearby", 5000).show();
	
}
	private class SetConnection extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			getnearbymessiah();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(jsonarray.length()>0)
			ShowNearbyMessiah();
			
			nomessiahfound();
			super.onPostExecute(result);
		}
	}
	private class SendNotification extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ImplementMaps.this);
			progressDialog.setMessage("Sending message to Messiah, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			sendnotification();
			
			return null;
		}
@Override
protected void onPostExecute(Void result) {
	super.onPostExecute(result);
	progressDialog.setMessage("Message sent to Messiah");
	progressDialog.hide();
}
		}
	private void sendnotification() {
		UserFunctions userfunction = new UserFunctions();
		JSONObject obj = userfunction.sendSendNotification(PhoneNumber,hisPhoneNumber);
		try {
			NotificationStatus = obj.getString("Status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
    return true;
	}
}
