package com.CFP.messiah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class WelcomeScreen extends Activity {
	Button AddContacts;
	String phoneName = null;
	String phoneNumber = null;
	String message = null;
	String[] Contacts;
	String[] Numbers;
	DataInsertion datainsertion;
	SharedPreferences users;
	SharedPreferences prefs;
	Editor editor;
	TextView tv1, tv2, tv3, tv4;

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.welcome_screen);
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		prefs = getSharedPreferences("Settings", 0);
		editor = prefs.edit();
		editor.putBoolean("Accident", false).commit();
		if (users.getBoolean("Verfication", false)) {
			startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
			this.finish();
		}
		tv1 = (TextView) findViewById(R.id.TVWelcome);
		tv2 = (TextView) findViewById(R.id.TVInsantly);
		tv3 = (TextView) findViewById(R.id.TVyour);
		tv4 = (TextView) findViewById(R.id.TVemergency);
		Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
		Typeface font1 = Typeface.createFromAsset(getAssets(), "rcb.ttf");
		tv1.setTypeface(font1);
		tv2.setTypeface(font);
		tv3.setTypeface(font);
		tv4.setTypeface(font);
		AddContacts = (Button) findViewById(R.id.btnAddContactswelcome);
		AddContacts.setTypeface(font);
		AddContacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//startActivity(new Intent(WelcomeScreen.this,ContactSelection.class));
				//WelcomeScreen.this.finish();
				Startprocess();
			}
		});
		showDialog(0);
	}
	public void Startprocess() {
		// TODO Auto-generated method stub
		Intent i = new Intent(android.content.Intent.ACTION_PICK);
		i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		int request_Code = 0;
		startActivityForResult(i, request_Code);

	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {


	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	int RESULT_PICK_CONTACT = 0;
	if (requestCode == RESULT_PICK_CONTACT && resultCode == RESULT_OK) {

		Uri dataUri = data.getData();
		Cursor contacts = managedQuery(dataUri, null, null, null, null);
		if (contacts.moveToFirst()) {
			String name;
			int nameColumn = contacts
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			name = contacts.getString(nameColumn);
			Cursor phones = getContentResolver().query(dataUri, null, null,
					null, null);
			if (phones.moveToFirst()) {
				phoneName = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				phoneNumber = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			}
			if (checklist()) {

				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Emergeny Message"); // Set Alert dialog
													// title
													// here
				// alert.setMessage("Enter Your Name Here"); //Message here

				// Set an EditText view to get user input
				final EditText input = new EditText(this);
				input.setText("I am in trouble");
				alert.setView(input);

				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// You will get as string input data in this
								// variable.
								// here we convert the input to a string and
								// show in a toast.
								message = input.getEditableText()
										.toString();

								DataInsertion datainsertion = new DataInsertion(
										getApplicationContext(), phoneName,
										phoneNumber, message);
								startActivity(new Intent(WelcomeScreen.this,MessiahRegistertion.class));
								WelcomeScreen.this.finish();

							}
							// End of onClick(DialogInterface dialog, int
							// whichButton)
						}); // End of alert.setPositiveButton
				alert.setNegativeButton("CANCEL",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
								dialog.cancel();
							}
						}); // End of alert.setNegativeButton
				AlertDialog alertDialog = alert.create();
				alertDialog.show();
				phones.close();
			}
		}
		if (!checklist()) {
			Startprocess();
			Toast.makeText(getApplicationContext(),
					"Contact already exists", Toast.LENGTH_LONG).show();

		}
	}
	}
private boolean checklist() {
	DataInsertion datainsertion = new DataInsertion();
	boolean flag = datainsertion.checkdatabase(getApplicationContext(),
			phoneName);

	return flag;
}
	protected Dialog onCreateDialog(int id) {
		// show disclaimer....
		// for example, you can show a dialog box...
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Messiah can be inaccurate and incomplete to some extent. The availability and accuracy of GPS and related services are dependent among other things, on wireless networks and satellite systems. They may not function on all areas or at all times. Therefore never rely solely on the aforementioned material and services e.g. for essential communications like emergencies. This product and its content are made for non-commercial private use only.")
				.setCancelable(false)
				.setTitle("Disclaimer")
				.setPositiveButton("Agree",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DataInsertion datainsertion = new DataInsertion();
								datainsertion.removeallcontact(WelcomeScreen.this);
								editor.putBoolean("AD", true).commit();
								editor.putBoolean("BM", true).commit();
								editor.putBoolean("DT", true).commit();
							}
						})
				.setNegativeButton("Disagree",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// nm.cancel(R.notification.running); // cancel
								// the NotificationManager (icon)
								System.exit(0);
							}
						});
		AlertDialog alert = builder.create();
		return alert;
	}

	@Override
	public void onBackPressed() {

		System.exit(0);
	}
}
