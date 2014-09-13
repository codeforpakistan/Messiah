package com.CFP.messiah;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactView extends Activity {
	ListView list;
	// String[] names;
	ArrayList<String> names;
	ArrayList<String> phone;
	ArrayList<String> insertnumber;
	String val;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactview);
		list = (ListView) findViewById(R.id.list);
		names = new ArrayList<String>();
		phone = new ArrayList<String>();
		insertnumber = new ArrayList<String>();
		new setview().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_contactview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int itemId = item.getItemId();
		if (itemId == R.id.action_done) {
			addtodatabase();
			//startActivity(new Intent(ContactView.this,MainActivity.class));
			startActivity(new Intent(ContactView.this,MessiahRegistertion.class));
		}
		
		return true;
	}

	private void addtodatabase() {
		for (int i = 0; i < insertnumber.size(); i++){
			String pos = insertnumber.get(i);
			String name = names.get(Integer.parseInt(pos));
			String phonenumber = phone.get(Integer.parseInt(pos)); 
			DataInsertion datainsertion = new DataInsertion(getApplicationContext(), name, phonenumber, "I am in Emergency help me and i am at ");
		}
		
	}

	public void updateadapter() {
		listrow adapter = new listrow(getApplicationContext(), names);
		list.setAdapter(adapter);
	}

	private class listrow extends ArrayAdapter<String> {
		private final Context context;

		public listrow(Context context, ArrayList<String> names) {
			super(context, R.layout.contactview, names);
			this.context = context;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View v = inflater.inflate(R.layout.contactview_listitem,
					parent, false);
			Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
			TextView tv = (TextView) v.findViewById(R.id.tvContact);
			tv.setTypeface(font);
			tv.setText(names.get(position));
			CheckBox cb = (CheckBox) v.findViewById(R.id.cbContact);
			final int pos = position;

			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						insertnumber.add(String.valueOf(pos));
						Toast.makeText(getApplicationContext(),
								pos + "inserted", Toast.LENGTH_SHORT).show();
					} else {
						removenumber(String.valueOf(pos));

					}
				}

			});

			return v;

		}

		private void removenumber(String pos) {
			for (int i = 0; i < insertnumber.size(); i++) {
				String val = insertnumber.get(i);
				if (val.equals(pos)) {
					insertnumber.remove(i);
					Toast.makeText(getApplicationContext(), pos + "removed",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}

		}
	}

	private class setview extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			getContacts();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			updateadapter();
			super.onPostExecute(result);
		}

		public void getContacts() {

			ContentResolver cr = getContentResolver();
			Cursor cursor = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					null, null, null);
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
				String number = cursor.getString(cursor
						.getColumnIndexOrThrow(Phone.NUMBER));
				names.add(name);
				phone.add(number);
			}

		}

	}

}
