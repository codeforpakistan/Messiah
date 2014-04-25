package com.example.test;

import com.example.test.SimpleGestureFilter.SimpleGestureListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class VerificationActivity extends Activity implements SimpleGestureListener{
	private SimpleGestureFilter detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		detector = new SimpleGestureFilter(this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verification, menu);
		return true;
	}
@Override
public boolean dispatchTouchEvent(MotionEvent me) {
	this.detector.onTouchEvent(me);
	return super.dispatchTouchEvent(me);
}
	@Override
	public void onSwipe(int direction) {
		switch (direction) {

		case SimpleGestureFilter.SWIPE_RIGHT:
			startActivity(new Intent(VerificationActivity.this,GetImage.class));
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			
						
			
			break;

	}
		
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

}
