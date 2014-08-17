package com.CFP.messiah;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExampleAdapter implements ExpandableListAdapter {
	Context context;
	LayoutInflater layoutInflater;
	ImageView ADM, TOD, BM;
	Boolean ADMstatus = true;
	Boolean TODstatus = true;
	Boolean BMstatus = true;

	public ExampleAdapter(Context context, LayoutInflater layoutInflater) {
		this.context = context;
		this.layoutInflater = layoutInflater;

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v = convertView.inflate(context, R.layout.expandgroup, null);
		v.setPadding(0, 30, 0, 0);
		TextView txtView = (TextView) v.findViewById(R.id.txt1);
		if (groupPosition == 0) {
			txtView.setText("Your Profile");
			txtView.setTextSize(20f);
		}
		if (groupPosition == 1) {
			txtView.setText("Add Contacts");
			txtView.setTextSize(20f);
		}
		if (groupPosition == 2) {
			v = convertView.inflate(context, R.layout.accidentdetection, null);
			v.setPadding(0, 30, 0, 0);
			TextView ADMtv = (TextView) v.findViewById(R.id.tvADM);
			ADMtv.setTextSize(20f);
			ADM = (ImageView) v.findViewById(R.id.IVADM);
			ADM.setImageResource(R.drawable.off);
			ADM.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (ADMstatus) {
						ADM.setImageResource(R.drawable.on);
						Toast.makeText(context,
								"Accident Detection Mode is On", Toast.LENGTH_LONG)
								.show();
						ADMstatus = false;
					} else {
						ADMstatus = true;
						Toast.makeText(context,
								"Accident Detection Mode is Off", Toast.LENGTH_LONG)
								.show();
						ADM.setImageResource(R.drawable.off);
						// stopService(i);

					}

				}
			});

		}
		if (groupPosition == 3) {
			txtView.setText("Notification");
			txtView.setTextSize(20f);
		}
		v.invalidate();
		return v;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = null;
		if (groupPosition == 0) {
			v = View.inflate(context, R.layout.yourprofile_child, null);

		}
		if (groupPosition == 1) {
			// v = View.inflate(context, R.layout, null);
			Toast.makeText(context, "Contacts", 5000).show();
		}
		if (groupPosition == 2) {
			Toast.makeText(context, "Contacts", 5000).show();
			// v = View.inflate(context, R.layout.expandable_child_2_layout,
			// null);
		}
		if (groupPosition == 3) {
			v = View.inflate(context, R.layout.notifications_child, null);
			TOD = (ImageView) v.findViewById(R.id.IVTOD);
			BM = (ImageView) v.findViewById(R.id.IVBM);
			TOD.setImageResource(R.drawable.off);
			TOD.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (TODstatus) {
						TOD.setImageResource(R.drawable.on);
						// startService(i);
						TODstatus = false;
					} else {
						TODstatus = true;
						TOD.setImageResource(R.drawable.off);
						// stopService(i);

					}

				}
			});
			BM.setImageResource(R.drawable.off);
			BM.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (BMstatus) {
						BM.setImageResource(R.drawable.on);
						// startService(i);
						BMstatus = false;
					} else {
						BMstatus = true;
						BM.setImageResource(R.drawable.off);
						// stopService(i);

					}

				}
			});

		}
		v.invalidate();
		return v;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}
}
