package com.example.test;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	Button signin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		signin = (Button) findViewById(R.id.btnSignIn);
		signin.setOnClickListener(new View.OnClickListener() {
			
			
			
			
			@Override
			public void onClick(View v) {
			
			startActivity(new Intent(LoginActivity.this,GrievanceIDActivity.class));	
			}
		});
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
