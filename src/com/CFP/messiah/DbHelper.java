package com.CFP.messiah;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	public static final String TAG = "DbHelper";
	public static final String DB_NAME = "Messiah_DB.db";
	public static final int DB_ver = 1;
	public final String Table = "TBL_CONTACT";
	public final String Table1 = "TBL_CALL";
	public static final boolean Debug = false;
	
	

	public DbHelper(Context context) {
		super(context,DB_NAME,null,DB_ver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "Create Table " + Table + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTACT_NAME TEXT, PHONE_NUMBER TEXT,BODY TEXT)";
		db.execSQL(query);
		String query1 = "Create Table " + Table1 + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTACT_NAME TEXT, PHONE_NUMBER TEXT)";
		db.execSQL(query1);

	}
public Cursor query(SQLiteDatabase db,String query){
	Cursor cur = db.rawQuery(query, null);
	return cur;
	
}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXIST %s", Table));
		db.execSQL(String.format("DROP TABLE IF EXIST %s", Table1));
		if(Debug){
			
			Log.d(TAG, "Droping table");
			
			
		}
		
		this.onCreate(db);
		
	}

}
