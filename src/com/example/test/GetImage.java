package com.example.test;

import com.example.test.SimpleGestureFilter.SimpleGestureListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;



public class GetImage extends Activity implements SimpleGestureListener{
	ImageView imgFavorite;
	private SimpleGestureFilter detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_image);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		detector = new SimpleGestureFilter(this, this);
		imgFavorite = (ImageView)findViewById(R.id.imageView1);
		
		imgFavorite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			open_camera();	
			}

			
		});
	
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}
	public void open_camera() {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	      startActivityForResult(intent, 0);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 Bitmap bp = (Bitmap) data.getExtras().get("data");
	      imgFavorite.setImageBitmap(bp);
	    
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_image, menu);
		return true;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onSwipe(int direction) {
		switch (direction) {

		case SimpleGestureFilter.SWIPE_RIGHT:
			startActivity(new Intent(GetImage.this,GrievanceIDActivity.class));
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			startActivity(new Intent(GetImage.this,VerificationActivity.class));
						
			
			break;

	}}
	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}


}
