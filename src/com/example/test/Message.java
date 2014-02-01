package com.example.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Message extends Activity implements OnClickListener{
Button taketext, btnBack;
EditText text;
TextView screen;
String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagebox);
		
		taketext= (Button) findViewById(R.id.btnShow);
		text = (EditText) findViewById(R.id.M);
		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("conName");
		
		
		taketext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try{
					new SaveMsg().execute();
				}
				catch (Exception e) {
		            text.setText(e.toString());
		        }
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btnBack:
			this.finish();
			break;
		}
	}
	
	private class SaveMsg extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog dialog = new ProgressDialog(Message.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait..");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(dialog.isShowing())
			{
				dialog.dismiss();
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String msg = text.getText().toString();
			SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
			Editor edit = prefs.edit();
		
			edit.putString(name+ "Mes", msg);
			edit.commit();
			return null;
		}
		
	}
	
}