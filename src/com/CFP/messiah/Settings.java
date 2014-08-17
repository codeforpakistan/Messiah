package com.CFP.messiah;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends ExpandableListActivity {
	ExpandableListView expListView;
	boolean urprofile = true;
	boolean notification = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		ExampleAdapter adapter = new ExampleAdapter(this, getLayoutInflater());
		setListAdapter(adapter);
		expListView = (ExpandableListView) findViewById(android.R.id.list);
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (groupPosition == 0) {
					if (urprofile) {
						urprofile = false;
						parent.expandGroup(0);
					}
					else if (!urprofile) {
						urprofile = true;
						parent.collapseGroup(0);
					}
				}
				if (groupPosition == 1) {
					startActivity(new Intent(Settings.this,
							ListViewDemoActivity.class));
				}
				if (groupPosition == 2) {

					Toast.makeText(getApplicationContext(),
							"Accident Detection Mode is On", Toast.LENGTH_LONG)
							.show();
				}
				if (groupPosition == 3) {
					if (notification) {
						notification = false;
						parent.expandGroup(3);
					}
					else if (!notification) {
						notification = true;
						parent.collapseGroup(3);
					}
				}

				return true;
			}
		});

	}

}
