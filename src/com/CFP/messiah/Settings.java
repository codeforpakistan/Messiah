package com.CFP.messiah;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {
	ImageView AD, BM, DT;
	TextView tvAD, tvBM, tvDT;
	SharedPreferences prefs;
	Editor editor;
	Intent i;
	Boolean ad, bm, dt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		AD = (ImageView) findViewById(R.id.IVAD);
		BM = (ImageView) findViewById(R.id.IVBM);
		DT = (ImageView) findViewById(R.id.IVDT);
		tvAD = (TextView) findViewById(R.id.textView1);
		tvBM = (TextView) findViewById(R.id.textView2);
		tvDT = (TextView) findViewById(R.id.textView3);
		Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
		tvAD.setTypeface(font);
		tvBM.setTypeface(font);
		tvDT.setTypeface(font);
		prefs = getSharedPreferences("Settings", 0);
		editor = prefs.edit();
		i = new Intent(Settings.this, AccidentService.class);

		ad = prefs.getBoolean("AD", false);
		bm = prefs.getBoolean("BM", false);
		dt = prefs.getBoolean("DT", false);
		if (ad) {
			AD.setImageDrawable(getResources().getDrawable(R.drawable.on));
			startService(i);
		} else {
			AD.setImageDrawable(getResources().getDrawable(R.drawable.off));
			startService(i);
			stopService(i);
		}
		if (bm) {
			BM.setImageDrawable(getResources().getDrawable(R.drawable.on));
		} else {
			BM.setImageDrawable(getResources().getDrawable(R.drawable.off));
		}
		if (dt) {
			DT.setImageDrawable(getResources().getDrawable(R.drawable.on));
		} else {
			DT.setImageDrawable(getResources().getDrawable(R.drawable.off));
		}
		AD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ad = prefs.getBoolean("AD", false);
				if (ad) {
					AD.setImageDrawable(getResources().getDrawable(
							R.drawable.off));
					startService(i);
					stopService(i);
					editor.putBoolean("AD", false).commit();
					Toast.makeText(getApplicationContext(),
							"Accident Detection mode off", Toast.LENGTH_SHORT)
							.show();
				} else {
					AD.setImageDrawable(getResources().getDrawable(
							R.drawable.on));
					startService(i);
					editor.putBoolean("AD", true).commit();
					Toast.makeText(getApplicationContext(),
							"Accident Detection mode on", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		BM.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				bm = prefs.getBoolean("BM", false);
				if (bm) {
					BM.setImageDrawable(getResources().getDrawable(
							R.drawable.off));
					editor.putBoolean("BM", false).commit();
					Toast.makeText(getApplicationContext(), "No more Messiah",
							Toast.LENGTH_SHORT).show();
				} else {
					BM.setImageDrawable(getResources().getDrawable(
							R.drawable.on));
					editor.putBoolean("BM", true).commit();
					Toast.makeText(getApplicationContext(), "I am Messiah ",
							Toast.LENGTH_SHORT).show();
					
				}

			}
		});
		DT.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dt = prefs.getBoolean("DT", false);
				if (dt) {
					DT.setImageDrawable(getResources().getDrawable(
							R.drawable.off));
					editor.putBoolean("DT", false).commit();
					Toast.makeText(getApplicationContext(),
							"Daily Tips off", Toast.LENGTH_SHORT)
							.show();
				} else {
					DT.setImageDrawable(getResources().getDrawable(
							R.drawable.on));
					editor.putBoolean("DT", true).commit();
					Toast.makeText(getApplicationContext(),
							"Daily Tips on", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {onBackPressed();
    return true;
	}
}