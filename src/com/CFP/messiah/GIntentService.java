/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.CFP.messiah;

import java.net.URLDecoder;
import org.json.JSONObject;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	SharedPreferences prefs;
	Editor editor;

	public GIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			Bundle extras = intent.getExtras();
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
			// The getMessageType() intent parameter must be the intent you
			// received
			// in your BroadcastReceiver.
			String messageType = gcm.getMessageType(intent);

			if (!extras.isEmpty()) { // has effect of unparcelling Bundle
				/*
				 * Filter messages based on message type. Since it is likely
				 * that GCM will be extended in the future with new message
				 * types, just ignore any message types you're not interested
				 * in, or that you don't recognize.
				 */
				if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
						.equals(messageType)) {
					sendNotification("Send error: " + extras.toString());
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
						.equals(messageType)) {
					sendNotification("Deleted messages on server: "
							+ extras.toString());
					// If it's a regular GCM message, do some work.
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
						.equals(messageType)) {
					// This loop represents the service doing some work.
					for (int i = 0; i < 5; i++) {
						Log.i(TAG, "Working... " + (i + 1) + "/5 @ "
								+ SystemClock.elapsedRealtime());
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
					}
					Log.i(TAG,
							"Completed work @ " + SystemClock.elapsedRealtime());
					// Post notification of received message.

					sendNotification(intent);
					Log.i(TAG, "Received: "
							+ extras.getString("message").toString());
				}
			}
			// Release the wake lock provided by the WakefulBroadcastReceiver.
			GcmBroadcastReceiver.completeWakefulIntent(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Intent data) {
		try {
			Intent notificationIntent = null;
			prefs = getSharedPreferences("Settings", 0);
			editor = prefs.edit();
			String message = data.getExtras().getString("Message");
			String Status = data.getExtras().getString("Status");
			String Longitude = data.getExtras().getString("Longitude");
			String Latitude = data.getExtras().getString("Latitude");
			String PhoneNumber = data.getExtras().getString("PhoneNumber");
			
			if (Status.equals("0")) {
				 notificationIntent = new Intent(this, MainActivity.class);
			}
			else if(Status.equals("1")){
				 notificationIntent = new Intent(this, Messiah_Need_Maps.class);
				notificationIntent.putExtra("Longitude", Longitude);
				notificationIntent.putExtra("Latitude", Latitude);
				notificationIntent.putExtra("PhoneNumber", PhoneNumber);
			}
			else if(Status.equals("2")){
				 notificationIntent = new Intent(this, MainActivity.class);
				editor.putBoolean("TIPcheck", true).commit();
				editor.putString("TIP", message).commit();
				message = "Tip of the Day";
			}
			
			int icon = R.drawable.notification_icon;
			long when = System.currentTimeMillis();
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(icon, message, when);

			String title = this.getString(R.string.app_name);

			//Intent notificationIntent = new Intent(this, MainActivity.class);
			// set intent so it does not start a new activity
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(this, title, message, intent);

			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

			// Play default notification sound
			notification.defaults |= Notification.DEFAULT_SOUND;

			// Vibrate if vibrate is enabled
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			
			if((Status.equals("1"))&&(prefs.getBoolean("BM", false)))
			notificationManager.notify(0, notification);
			else if(Status.equals("0"))
				notificationManager.notify(0, notification);
			else if((Status.equals("2"))&&(prefs.getBoolean("DT", false)))
				notificationManager.notify(0, notification);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void sendNotification(String message) {
		try {
			int icon = android.R.drawable.stat_notify_more;
			long when = System.currentTimeMillis();
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(icon, message, when);

			String title = this.getString(R.string.app_name);

			Intent notificationIntent = new Intent(this, MainActivity.class);
			// set intent so it does not start a new activity
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(this, title, message, intent);

			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

			// Play default notification sound
			notification.defaults |= Notification.DEFAULT_SOUND;

			// Vibrate if vibrate is enabled
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(0, notification);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
