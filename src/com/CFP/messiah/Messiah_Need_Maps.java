package com.CFP.messiah;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Messiah_Need_Maps extends FragmentActivity {

	private GoogleMap googleMap;
	BitmapDescriptor source_markericon, dest_markericon;
	AppLocationService appLocationService;
	Location location;
	double Latitude, Longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messiah_need_maps);
		source_markericon = BitmapDescriptorFactory
				.fromResource(R.drawable.map_loc);
		dest_markericon = BitmapDescriptorFactory
				.fromResource(R.drawable.red_loc);
		
		GetLocation();
		Bundle bundle = getIntent().getExtras();
		double sourcelat = Latitude;
		double sourcelog = Longitude;
		String destLatitude = bundle.getString("Latitude");
		double destlat = Double.parseDouble(destLatitude);
		String destLongitude = bundle.getString("Longitude");
		double destlog = Double.parseDouble(destLongitude);
		final String PhoneNumber = bundle.getString("PhoneNumber");
		initilizeMap();
		googleMap.setMyLocationEnabled(false);
		Marker source_marker = googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(sourcelat, sourcelog)).title("")
				.icon(source_markericon));
		final Marker dest_marker = googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(destlat, destlog)).title("")
				.icon(dest_markericon));
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				if (marker.getId().equals(dest_marker.getId())) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + PhoneNumber));
					startActivity(callIntent);
				}

				return true;

			}
		});
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(Latitude, Longitude), 18));
		String urlPass = makeURL(sourcelat, sourcelog, destlat, destlog);
		new connectAsyncTask(urlPass).execute();

		// JSONParser jParser = new JSONParser();
		// String json = jParser.getJSONFromUrl(url);
		// drawPath(json);
	}

	private void GetLocation() {

		// Latitude = location.getLatitude();
		// Longitude = location.getLongitude();
		// lat = String.valueOf(Latitude);
		// lon = String.valueOf(Longitude);
		// Log.v("lat", lat);
		// Log.v("lon", lon);
		GPSTracker gps = new GPSTracker(Messiah_Need_Maps.this);
		do {
			if (gps.canGetLocation()) {

				Latitude = gps.getLatitude();
				Longitude = gps.getLongitude();
			}
		} while ((Latitude == 0.00000000000000)
				&& (Longitude == 0.00000000000000));
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map1)).getMap();
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

	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	public void drawPath(String result) {

		try {
			// Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			for (int z = 0; z < list.size() - 1; z++) {
				LatLng src = list.get(z);
				LatLng dest = list.get(z + 1);
				Polyline line = googleMap.addPolyline(new PolylineOptions()
						.add(new LatLng(src.latitude, src.longitude),
								new LatLng(dest.latitude, dest.longitude))
						.width(4).color(Color.BLUE).geodesic(true));
			}

		} catch (JSONException e) {

		}
	}

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	private class connectAsyncTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog progressDialog;
		String url;

		connectAsyncTask(String urlPass) {
			url = urlPass;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(Messiah_Need_Maps.this);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.hide();
			if (result != null) {
				drawPath(result);
			}
		}
	}
}
