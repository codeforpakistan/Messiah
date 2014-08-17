package com.CFP.messiah;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ImplementMaps extends FragmentActivity {

	private GoogleMap googleMap;
	Context mcontext;
	double Longitude, Latitude;
	LocationManager mLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		try {
			// Loading map
			initilizeMap();
			Temp();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void Temp() {
		GPSTracker gps = new GPSTracker(ImplementMaps.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			googleMap.addMarker(new MarkerOptions()        
	        .position(new LatLng(latitude,longitude))       
	                .title("Hello world"));
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
	
}
