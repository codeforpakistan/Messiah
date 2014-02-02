package com.example.test;

import java.util.List;

import com.example.test.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class Settings extends Activity implements OnClickListener{
Button ShowContacts;
Button ShowContacts2;
Button ShowContact3;
Button back;
TextView txt1,txt2,txt3;
Button Message1,Message2,Message3;

Button Save;
int counter = 0;

String[] number;
String[] names;

TableLayout mTab;

String strName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		Save = (Button) findViewById(R.id.Save);
		ShowContacts = (Button) findViewById(R.id.SC);
		txt1 = (TextView) findViewById(R.id.Number1);
		txt2 = (TextView) findViewById(R.id.Number2);
		txt3 = (TextView) findViewById(R.id.Number3);
		
		ShowContacts2 = (Button) findViewById(R.id.SC2);
		ShowContacts2.setOnClickListener(this);
		ShowContact3 = (Button) findViewById(R.id.SC3);
		ShowContact3.setOnClickListener(this);
		
		back = (Button) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		back.setVisibility(1);
		
		Message1 = (Button) findViewById(R.id.SM);
		Message1.setOnClickListener(this);
		Message2 = (Button) findViewById(R.id.SM1);
		Message2.setOnClickListener(this);
		Message3 = (Button) findViewById(R.id.SM2);
		Message3.setOnClickListener(this);
		counter = 0;
		number = new String[3];
		names = new String[3];
		
		ShowContacts.setOnClickListener(this);
		
		Save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SharedPreferences prefs = getSharedPreferences("N1", MODE_PRIVATE);
				Editor edit = prefs.edit();
				
				for(int i =0; i<3;i++)
				{
					String num = number[i];
					edit.putString("contactNumber"+i, num);
					String contact = names[i];
					edit.putString("contactName"+i, contact);
				}
				
				edit.commit();
				
//				Intent intent = new Intent(Settings.this, MainActivity.class);
//				startActivity(intent);
				Settings.this.finish();
			}
		});
	}
	
	public void NewActivity()
	{
		AlertDialog.Builder build = new Builder(this);
		build.setMessage("Dont you want to Add a contact?");
		build.setTitle("Confirm");
		build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
//				Intent intent = new Intent(Settings.this, MainActivity.class);
//				startActivity(intent);
				Settings.this.finish();
			}
		});
		build.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface d, int arg1) {
				d.dismiss();
			}
		});
		
		AlertDialog alert = build.create();
		alert.show();
		
	}
	
	public String[] getContacts(){
		return names;
	}
	
	public void Startprocess() {
		// TODO Auto-generated method stub
		Intent i = new
				Intent(android.content.Intent.ACTION_PICK);
				i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			int request_Code = 0;
			startActivityForResult(i,request_Code);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		int RESULT_PICK_CONTACT = 0;
		if (requestCode == RESULT_PICK_CONTACT && resultCode == RESULT_OK) {
	        
	        String strPhone = "";

	        Uri dataUri = data.getData();
	        Cursor contacts = managedQuery(dataUri, null, null, null, null);
	        if (contacts.moveToFirst()) {
	            String name;
	            int nameColumn = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	            name = contacts.getString(nameColumn);
	            Cursor phones = getContentResolver().query(dataUri, null, null, null, null);
	            if (phones.moveToFirst()) {
	                String phoneName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                names[counter] = new String(name);
	                strName = names[counter];
	                number[counter] = new String(phoneNumber);
	                counter++;
	                
	                if(counter == 1){
	        	        txt1.setText(strName);
	        	        }
	        	        else if(counter == 2){
	        	        	txt2.setText(strName);
	        	        }
	        	        else if(counter == 3)
	        	        {
	        	        	txt3.setText(strName);
	        	        }
	                
	            }
	            phones.close();
	        }

//	        if(counter == 0){
//	        txt1.setText(names[counter].toString());
//	        }
//	        else if(counter == 1){
//	        	txt2.setText(names[counter].toString());
//	        }
//	        else if(counter == 2)
//	        {
//	        	txt3.setText(names[counter].toString());
//	        }
	        
	        
	        
	}
	

}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.SC:
			Startprocess();
			txt1.setText(strName);
			break;
		case R.id.SC2:
			Startprocess();
			txt2.setText(strName);
			break;
		case R.id.SC3:
			Startprocess();
			txt3.setText(strName);
			break;
		case R.id.SM:
			StartMessageActivity(0);
			break;
		case R.id.SM1:
			StartMessageActivity(1);
			break;
		case R.id.SM2:
			StartMessageActivity(2);
			break;
		case R.id.btnBack:
			NewActivity();
			break;
		}
	}

	private void StartMessageActivity(int i) {
		// TODO Auto-generated method stub
		Intent d = new Intent(Settings.this , Message.class);
		d.putExtra("conName", number[i]);
		startActivity(d);
	}}
