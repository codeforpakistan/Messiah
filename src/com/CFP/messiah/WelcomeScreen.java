package com.CFP.messiah;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends Activity{
	Button AddContacts;
	SharedPreferences users;
	@Override
	protected void onStart() {
		super.onStart();
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		if(users.contains("Phonenumber")){
			startActivity(new Intent(WelcomeScreen.this,MainActivity.class));
			
		}
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		AddContacts = (Button) findViewById(R.id.btnAddContactswelcome);
		AddContacts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//startActivity(new Intent(WelcomeScreen.this,ContactView.class));
				startActivity(new Intent(WelcomeScreen.this,MainActivity.class));
			}
		});
	}
	

}
