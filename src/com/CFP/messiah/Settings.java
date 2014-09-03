package com.CFP.messiah;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends Activity{
	ListView list;
	String array[];
	ArrayList<String> SettingsArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		list = (ListView) findViewById(R.id.Settingslist);
		array   = getResources().getStringArray(R.array.Setting_Array);
		SettingsArray = new ArrayList<String>(array.length);
		for(String value: array) {
			SettingsArray.add(value);
		}
		listrow adapter = new listrow(getApplicationContext(), SettingsArray);
		list.setAdapter(adapter);
		
	}
	
	
	private class listrow extends ArrayAdapter<String> {
		private final Context context;
		public listrow(Context context, ArrayList<String> names) {
			super(context, R.layout.settings_listitem, names);
			this.context = context;

		}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	final View v = inflater.inflate(R.layout.settings_listitem,
			parent, false);
	TextView tv = (TextView) v.findViewById(R.id.tvSettings);
	tv.setText(SettingsArray.get(position));
	return v;
}
	}
}