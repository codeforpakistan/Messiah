package com.CFP.messiah;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;
	private static String IP = "107.170.26.223";//"192.168.1.4:8383/Messiah-Web";
	private static String RegisterationURL = "http://" + IP + "/inc/api.registration_controller.php/";// "http://messiah-pdma.herokuapp.com/inc/api.login_controller.php";
	private static String VerificationURL = "http://" + IP + "/inc/api.verification_controller.php/";
	private static String InsertLocationURL = "http://" + IP + "/inc/api.current_location_controller.php/";
	private static String GetMessiah = "http://" + IP + "/inc/api.get_nearby_messiah_controller.php/";
	private static String SendGCMId = "http://" + IP + "/inc/api.gcm_reg_controller.php/";
	private static String SendHelpRequest = "http://" + IP + "/inc/api.gcm_request_controller.php/";
	SharedPreferences users;
	Editor editor;
		// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
		
	}
	
	public JSONObject Registertion(String FullName, String PhoneNumber) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		// params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("FullName", FullName));
		params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));
		JSONObject json = jsonParser.getJSONFromUrl(RegisterationURL, params);
		// return json
		Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject Verification(String PhoneNumber,String VerificationCode) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		// params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("VerificationCode", VerificationCode));
		params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));
		JSONObject json = jsonParser.getJSONFromUrl(VerificationURL, params);
		// return json
		Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject SendLocation(String PhoneNumber, String lat, String lon) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>(4);
		// params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("Operation", "Insert"));
		params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));
		params.add(new BasicNameValuePair("Longitude", lon));
		params.add(new BasicNameValuePair("Latitude", lat));
		JSONObject json = jsonParser.getJSONFromUrl(InsertLocationURL, params);
		// return json
		Log.e("JSON", json.toString());
		return json;

	}

	public JSONArray GetNearbyMessiah(String PhoneNumber, String lat,
			String lon) {
		// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>(3);
				// params.add(new BasicNameValuePair("tag", login_tag));
				//params.add(new BasicNameValuePair("Operation", "Insert"));
				params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));
				params.add(new BasicNameValuePair("Longitude", lon));
				params.add(new BasicNameValuePair("Latitude", lat));
				JSONArray json = jsonParser.getJSONArrayFromUrl(GetMessiah, params);
				// return json
				Log.e("JSON", json.toString());
				return json;
		
	}
	public JSONObject SendGCMId(String PhoneNumber, String regid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		// params.add(new BasicNameValuePair("tag", login_tag));
		//params.add(new BasicNameValuePair("Operation", "Insert"));
		params.add(new BasicNameValuePair("PhoneNumber", PhoneNumber));
		params.add(new BasicNameValuePair("GCMID", regid));
		JSONObject json = jsonParser.getJSONFromUrl(SendGCMId, params);
		// return json
		Log.e("JSON", json.toString());
		return json;
	}
	public JSONObject sendSendNotification(String PhoneNumber, String hisPhoneNumber) {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("MyPhoneNumber", PhoneNumber));
		params.add(new BasicNameValuePair("HisPhoneNumber", hisPhoneNumber));
		JSONObject json = jsonParser.getJSONFromUrl(SendHelpRequest, params);
		return json;
	}

}