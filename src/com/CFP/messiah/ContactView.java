package com.CFP.messiah;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactView extends Activity {
	ListView list;
	// String[] names;
	ArrayList<String> names;
	ArrayList<String> phone;
	ArrayList<String> insertnumber;
	private Contact[] contact;
	String val;
	int counter = 0;
	ArrayList<String> array;
	private ArrayAdapter<Contact> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactview);

		list = (ListView) findViewById(R.id.list);
		names = new ArrayList<String>();
		phone = new ArrayList<String>();
		insertnumber = new ArrayList<String>();
		getContacts();
		// new setview().execute();
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				Contact planet = listAdapter.getItem(position);
				planet.toggleChecked();
				ContactViewHolder viewHolder = (ContactViewHolder) item
						.getTag();
				viewHolder.getCheckBox().setChecked(planet.isChecked());

			}
		});
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		contactList.addAll(Arrays.asList(contact));

		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new ContactArrayAdapter(this, contactList);
		list.setAdapter(listAdapter);

	}

	private static class Contact {
		private String name = "";
		private boolean checked = false;

		public Contact() {
		}

		public Contact(String name) {
			this.name = name;
		}

		public Contact(String name, boolean checked) {
			this.name = name;
			this.checked = checked;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String toString() {
			return name;
		}

		public void toggleChecked() {
			checked = !checked;
		}
	}

	private static class ContactViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public ContactViewHolder() {
		}

		public ContactViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
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
			if (counter > 0) {
				addtodatabase();
				// startActivity(new
				// Intent(ContactView.this,MainActivity.class));
				startActivity(new Intent(ContactView.this,
						MessiahRegistertion.class));
				ContactView.this.finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"Select atleast one Messiah", Toast.LENGTH_LONG).show();

			}
		}

		return true;
	}

	private void addtodatabase() {
		for (int i = 0; i < insertnumber.size(); i++) {
			String pos = insertnumber.get(i);
			String name = names.get(Integer.parseInt(pos));
			String phonenumber = phone.get(Integer.parseInt(pos));
			DataInsertion datainsertion = new DataInsertion(
					getApplicationContext(), name, phonenumber,
					"I am in Emergency help me and i am at ");
		}

	}

	public void updateadapter() {
		// listrow adapter = new listrow(getApplicationContext(), names);
		// list.setAdapter(adapter);
	}

	private static class ContactArrayAdapter extends ArrayAdapter<Contact> {
		private LayoutInflater inflater;

		public ContactArrayAdapter(Context context, List<Contact> contactlist) {
			super(context, R.layout.contactview_listitem, R.id.tvContact,
					contactlist);
			inflater = LayoutInflater.from(context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Contact contact = (Contact) this.getItem(position);
			CheckBox checkBox;
			TextView textView;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contactview_listitem,
						null);

				// Find the child views.
				textView = (TextView) convertView.findViewById(R.id.tvContact);
				//checkBox = (CheckBox) convertView.findViewById(R.id.cbContact);

				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
//				convertView.setTag(new ContactViewHolder(textView, checkBox));
//				checkBox.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						CheckBox cb = (CheckBox) v;
//						Contact contact = (Contact) cb.getTag();
//						contact.setChecked(cb.isChecked());
//
//					}
//				});
		}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				ContactViewHolder viewHolder = (ContactViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}
	//		checkBox.setTag(contact);

			// Display planet data
		//	checkBox.setChecked(contact.isChecked());
			textView.setText(contact.getName());

			return convertView;
		}

	}

	/*
	 * private class listrow extends ArrayAdapter<String> { private final
	 * Context context;
	 * 
	 * public listrow(Context context, ArrayList<String> names) { super(context,
	 * R.layout.contactview, names); this.context = context;
	 * 
	 * }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { LayoutInflater inflater = (LayoutInflater) context
	 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); final View v =
	 * inflater.inflate(R.layout.contactview_listitem, parent, false); Typeface
	 * font = Typeface.createFromAsset(getAssets(), "rcl.ttf"); TextView tv =
	 * (TextView) v.findViewById(R.id.tvContact); final ImageView
	 * contact_selection = (ImageView) v .findViewById(R.id.ivContact); if
	 * (contact_selection.getTag().equals(0)) {
	 * contact_selection.setTag(R.drawable.contacts_selection_empty); } final
	 * int pos = position; tv.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { ImageView contact_selection =
	 * (ImageView) v .findViewById(R.id.ivContact); int TAG = (Integer)
	 * contact_selection.getTag(); if (TAG == 1) { contact_selection
	 * .setImageDrawable(getResources().getDrawable(
	 * R.drawable.contacts_selection_fill));
	 * insertnumber.add(String.valueOf(pos)); array.add(String.valueOf(pos));
	 * counter++; contact_selection.setTag(0);
	 * 
	 * } else {
	 * 
	 * contact_selection.setImageDrawable(getResources() .getDrawable(
	 * R.drawable.contacts_selection_empty)); contact_selection.setTag(1);
	 * counter--;
	 * 
	 * removenumber(String.valueOf(pos)); }
	 * 
	 * } }); contact_selection.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) {
	 * 
	 * ImageView contact_selection = (ImageView) v
	 * .findViewById(R.id.ivContact); int TAG = (Integer)
	 * contact_selection.getTag(); if (TAG == 1) { contact_selection
	 * .setImageDrawable(getResources().getDrawable(
	 * R.drawable.contacts_selection_fill));
	 * insertnumber.add(String.valueOf(pos)); counter++;
	 * contact_selection.setTag(0); } else {
	 * 
	 * contact_selection.setImageDrawable(getResources() .getDrawable(
	 * R.drawable.contacts_selection_empty)); contact_selection.setTag(1);
	 * counter--; removenumber(String.valueOf(pos)); }
	 * 
	 * } }); tv.setTypeface(font); tv.setText(names.get(position)); // CheckBox
	 * cb = (CheckBox) v.findViewById(R.id.cbContact); // // // // // // //
	 * cb.setOnCheckedChangeListener(new OnCheckedChangeListener() { // //
	 * 
	 * @Override // public void onCheckedChanged(CompoundButton buttonView, //
	 * boolean isChecked) { // if (isChecked) { // ImageView contact_selection =
	 * (ImageView) // v.findViewById(R.id.ivContact); //
	 * contact_selection.setImageDrawable
	 * (getResources().getDrawable(R.drawable.contacts_selection_fill)); //
	 * insertnumber.add(String.valueOf(pos)); // // //
	 * contact_selection.setImageDrawable
	 * (getResources().getDrawable(R.drawable.contacts_selection_fill)); // //
	 * Toast.makeText(getApplicationContext(), // // pos + "inserted",
	 * Toast.LENGTH_SHORT).show(); // } else { // ImageView contact_selection =
	 * (ImageView) // v.findViewById(R.id.ivContact); //
	 * contact_selection.setImageDrawable
	 * (getResources().getDrawable(R.drawable.contacts_selection_empty)); // //
	 * removenumber(String.valueOf(pos)); // // } // } // // });
	 * 
	 * return v;
	 * 
	 * }
	 * 
	 * private void removenumber(String pos) { for (int i = 0; i <
	 * insertnumber.size(); i++) { String val = insertnumber.get(i); if
	 * (val.equals(pos)) { insertnumber.remove(i); //
	 * contact_selection.setImageDrawable
	 * (getResources().getDrawable(R.drawable.contacts_selection_fill)); //
	 * Toast.makeText(getApplicationContext(), pos + "removed", //
	 * Toast.LENGTH_SHORT).show(); break; } }
	 * 
	 * } }
	 */
	public void getContacts() {

		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		int i = 0;
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
			String number = cursor.getString(cursor
					.getColumnIndexOrThrow(Phone.NUMBER));
			contact[i] = new Contact(name);
			names.add(name);
			phone.add(number);

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
			// updateadapter();
			super.onPostExecute(result);
		}

	}

}
