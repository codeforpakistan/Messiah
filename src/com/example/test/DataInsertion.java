package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DataInsertion {

	private DbHelper Db;
	private SQLiteDatabase database;
	
	
	

	
	
	public DataInsertion(Context ctx ,String Contact_Name, String Phone_Number,String Message){
		Db = new DbHelper(ctx);
		database = Db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("CONTACT_NAME", Contact_Name);
		values.put("PHONE_NUMBER", Phone_Number);
		values.put("BODY", Message);
		database.insert(Db.Table, "", values);
		database.close();
		//TEsting
		Db = new DbHelper(ctx);
		database = Db.getReadableDatabase();
		Cursor cur = Db.query(database, "SELECT * FROM TBL_BACKUP");
		cur.moveToFirst();
//		  body = cur.getString(cur.getColumnIndex("BODY"));
//		phone = cur.getString(cur.getColumnIndex("PHONE_NUMBER"));
//		 date= cur.getInt(cur.getColumnIndex("DATE_TIME"));
//		
		cur.close();
	
		
		
	}
	
}
