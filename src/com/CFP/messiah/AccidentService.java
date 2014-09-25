package com.CFP.messiah;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class AccidentService extends Service implements SensorEventListener {
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private final float NOISE = (float) 1.0;
	SharedPreferences users, prefs;
	Editor editor;
	float X;
	float Y;
	float Z;

	@Override
	public void onCreate() {
		// Toast.makeText(getApplicationContext(), "started",
		// Toast.LENGTH_SHORT).show();
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		prefs = getSharedPreferences("Settings", 0);
		editor = users.edit();

		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);

		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);

			X = deltaX;
			Y = deltaY;
			Z = deltaZ;

			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			double g = 9.81;
			double gMagnitude = 4;

			float gx = deltaX;
			float gy = deltaY;
			float gz = deltaZ;

			double accl = Math.sqrt(gx * gx + gy * gy + gz * gz);

			if (accl > gMagnitude * g) {
				// Toast.makeText(getApplicationContext(), "AD"
				// +String.valueOf(prefs.getBoolean("AD", false)), 5000).show();
				// Toast.makeText(getApplicationContext(), "Acident"
				// +String.valueOf(users.getBoolean("Accident", false)),
				// 5000).show();
				if (prefs.getBoolean("AD", false)) {
					// if (users.getBoolean("Accident", false)) {
					Intent i = new Intent();
					i.setClass(this, AccidentWarning.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// editor.putBoolean("Accident", false).commit();
					startActivity(i);
					// Toast.makeText(getApplicationContext(),String.valueOf(users.getBoolean("Accident",false)),Toast.LENGTH_SHORT).show();
					// }
				}
			}
		}
	}

}
