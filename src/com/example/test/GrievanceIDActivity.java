package com.example.test;

import com.example.test.SimpleGestureFilter.SimpleGestureListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class GrievanceIDActivity extends Activity implements SimpleGestureListener{
static public String G_ID = null;
private SimpleGestureFilter detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grievance_id);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		detector = new SimpleGestureFilter(this, this);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grievance_id, menu);
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
		
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			startActivity(new Intent(GrievanceIDActivity.this,GetImage.class));
						
			
			break;
			
		}

		
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

}
