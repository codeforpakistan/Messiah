package com.CFP.messiah;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

public class Device_GPS_Coords {
	
	

	LocationManager manager;
	
	private double userLat;
	private double userLongt;
	
	locationListner listener;
	
	Context context;
	
	
	
	public Device_GPS_Coords(Context con)
	{
		context =con;
		manager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		
		listener = new locationListner();
		
		
		//Toast.makeText(con, "finding GPS", Toast.LENGTH_SHORT).show();
	}
	
	
	// for single update
	public void locationSingleUpdate()
	{
		//manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener); // minium time between location updates is 2 secs
		// minimum distance beween location updates is 10 meter
	
		manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, Looper.getMainLooper());
	}
	
	
	
	// for continous gps activity
	public void locationContinousUpdate()
	{
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 500, listener); // minium time between location updates is 10 secs
		// minimum distance beween location updates is 1 KM
		
	}
	
	
	public double getUserLat()
	{
		return userLat;
	}
	
	public double getUserLongt()
	{
		return userLongt;
	}
	
	
	
	private class locationListner implements LocationListener 
	{
		
		@Override
		public void onLocationChanged(Location location) {
		
			userLat = location.getLatitude();
			userLongt = location.getLongitude();
			
			//Toast.makeText(context, "Location Chagne", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}