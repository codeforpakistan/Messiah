package com.CFP.messiah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreen extends Activity{
	Button AddContacts;
	SharedPreferences users;
	TextView tv1,tv2,tv3,tv4;
	@Override
	protected void onStart() {
		super.onStart();
		
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		if(users.contains("Phonenumber")){
			startActivity(new Intent(WelcomeScreen.this,MainActivity.class));
			
		}
		
		tv1 = (TextView)findViewById(R.id.TVWelcome);
		tv2 = (TextView)findViewById(R.id.TVInsantly);
		tv3 = (TextView)findViewById(R.id.TVyour);
		tv4 = (TextView)findViewById(R.id.TVemergency);
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
			//startActivity(new Intent(WelcomeScreen.this,ContactView.class));
				startActivity(new Intent(WelcomeScreen.this,MessiahRegistertion.class));
			}
		});
		showDialog(0);
	}
	protected Dialog onCreateDialog(int id){
    	// show disclaimer....
        // for example, you can show a dialog box... 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Messiah can be inaccurate and incomplete to some extent. The availability and accuracy of GPS and related services are dependent among other things, on wireless networks and satellite systems. They may not function on all areas or at all times. Therefore never rely solely on the aforementioned material and services e.g. for essential communications like emergencies. This product and its content are made for non-commercial private use only.")
               .setCancelable(false)
               .setTitle("Disclaimer")
               .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // and, if the user accept, you can execute something like this:
                       // We need an Editor object to make preference changes.
                       // All objects are from android.context.Context
                     //  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                       //SharedPreferences.Editor editor = settings.edit();
                   //    editor.putBoolean("accepted", true);
                       // Commit the edits!
                     //  editor.commit();                    
                   }
               })
               .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //nm.cancel(R.notification.running); // cancel the NotificationManager (icon)
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
