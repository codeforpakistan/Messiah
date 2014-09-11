package com.CFP.messiah;

import android.app.Activity;
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
	}
	

}
