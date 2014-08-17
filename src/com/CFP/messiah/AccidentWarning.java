package com.CFP.messiah;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class AccidentWarning extends Activity {
	 MediaPlayer mp ;
	 String lat=null;
	 String lon=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accident_warning);
		
	mp	= MediaPlayer.create(AccidentWarning.this, R.raw.alarm);	
		notification();
		
		
		
	}

	private void notification() {
	mp.setLooping(true);
	mp.start();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Emergeny Message"); // Set Alert dialog title
											// here
		 alert.setMessage("Is everything OK?"); 

		
		
		
		 
		alert.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.cancel();
						mp.stop();
						AccidentWarning.this.finish();
					}
					// End of onClick(DialogInterface dialog, int
					// whichButton)
				}); // End of alert.setPositiveButton
		alert.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
					SendSMS();
					mp.stop();
					AccidentWarning.this.finish();
						
					}
				}); // End of alert.setNegativeButton
		AlertDialog alertDialog = alert.create();
		alertDialog.show();

Toast.makeText(getApplicationContext(), "accident", Toast.LENGTH_SHORT).show();				

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accident_warning, menu);
		return true;
	}
	public void SendSMS()
	{
		 
		try{
			GPSTracker gps = new GPSTracker(AccidentWarning.this);
			if (gps.canGetLocation()) {

				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();
			lat = String.valueOf(latitude);
			lon = String.valueOf(longitude);
			
			}
//			Location nwLocation = appLocationService
//					.getLocation(LocationManager.NETWORK_PROVIDER);
//
//			if (nwLocation != null) {
//				double latitude = nwLocation.getLatitude();
//				double longitude = nwLocation.getLongitude();
//				
//				lon = String.valueOf(longitude);
//				lat = String.valueOf(latitude);
//				String add = GetAddress(lat, lon);
//				Toast.makeText(
//						getApplicationContext(),
//						"Mobile Location (NW): \nLatitude: " + "34.755139"
//								+ "\nLongitude: " + + " " + add,
//						Toast.LENGTH_SHORT).show();
//			
//		}

			String pinpoint = "http://www.maps.google.com/maps?q=" + lat + ","+ lon ;
		//	String address = GetAddress(lat, lon);
			DataInsertion obj = new DataInsertion();
			String[] phonenumber = obj.getphonenumbers(getApplicationContext());
			String[] messages = obj.getmessages(getApplicationContext());
			for(int i = 0; i<= phonenumber.length;i++ )
			SmsManager.getDefault().sendTextMessage(phonenumber[i], null, messages[i] + "Im at: " +  " " + pinpoint, null, null);
		}
		catch(Exception ex)
		{
			Log.d("SMS Error: ", ex.getMessage().toString());
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

}
