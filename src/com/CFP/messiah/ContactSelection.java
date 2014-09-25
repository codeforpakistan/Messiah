package com.CFP.messiah;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSelection extends Activity implements OnItemClickListener {
	List < String > name1 = new ArrayList < String > ();
	List < String > phno1 = new ArrayList < String > ();
	List < String > array = new ArrayList < String > ();

	MyAdapter ma;
	ListView lv;
	static int global_pos = 53;@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactview);

		getAllContacts(this.getContentResolver());
		lv = (ListView) findViewById(R.id.list);
		ma = new MyAdapter(ContactSelection.this, name1);
		lv.setAdapter(ma);
		lv.setOnItemClickListener(this);
		//		lv.setTextFilterEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView <? > arg0, View v, int arg2, long arg3) {
		ImageView image = (ImageView) v.findViewById(R.id.ivContact);
		String pos = String.valueOf(arg2);
		for (int i = 0; i < array.size(); i++) {
			if (pos.equals(array.get(i).contains(pos))) {
				array.remove(i);
				image.setImageDrawable(getResources().getDrawable(R.drawable.contacts_selection_empty));
			} else {
				array.add(String.valueOf(arg2));
				image.setImageDrawable(getResources().getDrawable(R.drawable.contacts_selection_fill));
				break;
			}
		}
		if (array.size() == 0) array.add(String.valueOf(arg2));
		image.setImageDrawable(getResources().getDrawable(R.drawable.contacts_selection_fill));

	}

	public void getAllContacts(ContentResolver cr) {

		Cursor phones = cr.query(
		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
		null, null);
		while (phones.moveToNext()) {
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			name1.add(name);
			phno1.add(phoneNumber);
		}

		phones.close();
	}

	class MyAdapter extends ArrayAdapter implements
	OnCheckedChangeListener {
		private SparseBooleanArray mCheckStates;
		LayoutInflater mInflater;
		TextView tv1, tv;
		ImageView img;
		CheckBox cb;

		MyAdapter(Context context, List < String > values) {
			super(context, 0, values);
			mCheckStates = new SparseBooleanArray(name1.size());
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name1.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View vi = convertView;
			if (convertView == null) vi = mInflater.inflate(R.layout.contactview_listitem, null);
			tv = (TextView) vi.findViewById(R.id.tvContact);
			img = (ImageView) vi.findViewById(R.id.ivContact);
			//tv1 = (TextView) vi.findViewById(R.id.tvContact);
			cb = (CheckBox) vi.findViewById(R.id.cbContact);
			tv.setText(name1.get(position));
			//tv1.setText("Phone No :" + phno1.get(position));
			cb.setTag(position);
			cb.setChecked(mCheckStates.get(position, false));

			cb.setOnCheckedChangeListener(this);

			return vi;
		}
		public boolean isChecked(int position) {

			return mCheckStates.get(position, false);

		}

		public void setChecked(int position, boolean isChecked) {
			//        	ContactSelection.this.global_pos = position;
			mCheckStates.put(position, isChecked);
		}

		public void toggle(int position) {
			//global_pos = position;
			setChecked(position, !isChecked(position));
		}@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			//			int x =  (Integer) buttonView.getTag();
			//			Toast.makeText(getApplicationContext(),String. valueOf(x), 5000).show();
			mCheckStates.put((Integer) buttonView.getTag(), isChecked);

		}
	}
}