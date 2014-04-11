package com.example.test;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DataInsertion {

	private DbHelper Db;
	private SQLiteDatabase database;
	private ArrayList<String> ContactName = new ArrayList<String>();

	public DataInsertion(Context ctx, String Contact_Name, String Phone_Number,
			String Message) {
		Db = new DbHelper(ctx);
		database = Db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("CONTACT_NAME", Contact_Name);
		values.put("PHONE_NUMBER", Phone_Number);
		values.put("BODY", Message);
		database.insert(Db.Table, "", values);
		database.close();
		// TEsting
		Db = new DbHelper(ctx);
		database = Db.getReadableDatabase();
		Cursor cur = Db.query(database, "SELECT * FROM TBL_CONTACT");
		cur.moveToFirst();
		String body = cur.getString(cur.getColumnIndex("BODY"));
		String phone = cur.getString(cur.getColumnIndex("PHONE_NUMBER"));
		String name = cur.getString(cur.getColumnIndex("CONTACT_NAME"));
		Toast.makeText(ctx, body + phone + name, Toast.LENGTH_SHORT).show();
		cur.close();

	}

	public DataInsertion() {

	}

	public DataInsertion(Context ctx, String name,String message) {
		Db = new DbHelper(ctx);
		database = Db.getWritableDatabase();
		database.execSQL("UPDATE  TBL_CONTACT  SET BODY = '"+ message + "' WHERE CONTACT_NAME = '"+ name + "'");
		Toast.makeText(ctx, "Message Updated", Toast.LENGTH_LONG).show();
		
	}

	public String[] displayData(Context ctx) {
		Db = new DbHelper(ctx);
		database = Db.getWritableDatabase();
		Cursor cur = Db.query(database, "SELECT * FROM TBL_CONTACT");
		String[] array = new String[cur.getCount()];
		int i = 0;
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {

				String name = cur.getString(cur.getColumnIndex("CONTACT_NAME"));
				array[i] = name;
				i++;
			} while (cur.moveToNext());
		}
		cur.close();
		return array;

	}

	public boolean checkdatabase(Context ctx, String name) {
		Db = new DbHelper(ctx);
		database = Db.getReadableDatabase();
		Cursor cur = Db
				.query(database,
						"SELECT * FROM TBL_CONTACT WHERE CONTACT_NAME = '"
								+ name + "'");
		int count = cur.getCount();
		if (count > 0) {

			return false;
		}
cur.close();
		return true;
	}

	public void removecontact(Context ctx, String listItemName) {
		Db = new DbHelper(ctx);
		database = Db.getWritableDatabase();
		database.execSQL("DELETE FROM TBL_CONTACT WHERE CONTACT_NAME = '"+ listItemName + "'");
		
		Toast.makeText(ctx, listItemName + "Deleted ", Toast.LENGTH_LONG).show();
	
	}

	public String showmessage(Context ctx, String listItemName) {
		String msg= null;
		Db = new DbHelper(ctx);
		database = Db.getReadableDatabase();
		Cursor cur = 
				Db.query(database,
				"SELECT * FROM TBL_CONTACT WHERE CONTACT_NAME = '"
						+ listItemName + "'");
		cur.moveToFirst();
		if (cur.moveToFirst()) {
			do {

				msg = cur.getString(cur.getColumnIndex("BODY"));
			} while (cur.moveToNext());
		}
		cur.close();
		return msg;

	}
}
