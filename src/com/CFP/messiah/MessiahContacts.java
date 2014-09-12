package com.CFP.messiah;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessiahContacts extends Activity {
	String phoneName = null;
	String phoneNumber = null;
	String message = null;
	String[] Contacts;
	String[] Numbers;
	DataInsertion datainsertion;
	Button AddContact;
	TextView nocontacts;
	ListView list;
	int count;
	SharedPreferences users;
	Editor editor;
	ArrayList<String> ContactsArray;
	ArrayList<String> PhoneArray;
	public static int pos;
	final int CONTEXT_MENU_VIEW = 1;
	final int CONTEXT_MENU_EDIT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
		editor = users.edit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		list = (ListView) findViewById(R.id.Contactlist);
		ShowMessiahContact();
		// registerForContextMenu(list);
		AddContact = (Button) findViewById(R.id.btnAddContact);
		Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
		AddContact.setTypeface(font);
		AddContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Startprocess();

			}
		});

	}

	public void Startprocess() {
		// TODO Auto-generated method stub
		Intent i = new Intent(android.content.Intent.ACTION_PICK);
		i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		int request_Code = 0;
		startActivityForResult(i, request_Code);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		int RESULT_PICK_CONTACT = 0;
		if (requestCode == RESULT_PICK_CONTACT && resultCode == RESULT_OK) {

			Uri dataUri = data.getData();
			Cursor contacts = managedQuery(dataUri, null, null, null, null);
			if (contacts.moveToFirst()) {
				String name;
				int nameColumn = contacts
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				name = contacts.getString(nameColumn);
				Cursor phones = getContentResolver().query(dataUri, null, null,
						null, null);
				if (phones.moveToFirst()) {
					phoneName = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				}
				if (checklist()) {

					AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setTitle("Emergeny Message"); // Set Alert dialog
														// title
														// here
					// alert.setMessage("Enter Your Name Here"); //Message here

					// Set an EditText view to get user input
					final EditText input = new EditText(this);
					input.setText("I am in trouble");
					alert.setView(input);

					alert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// You will get as string input data in this
									// variable.
									// here we convert the input to a string and
									// show in a toast.
									message = input.getEditableText()
											.toString();

									DataInsertion datainsertion = new DataInsertion(
											getApplicationContext(), phoneName,
											phoneNumber, message);
									ShowMessiahContact();
									count = datainsertion
											.countcontacts(getApplicationContext());
									if (count > 0) {
										nocontacts.setText("");
									}

								}
								// End of onClick(DialogInterface dialog, int
								// whichButton)
							}); // End of alert.setPositiveButton
					alert.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
									dialog.cancel();
								}
							}); // End of alert.setNegativeButton
					AlertDialog alertDialog = alert.create();
					alertDialog.show();
					phones.close();
				}
			}
			if (!checklist()) {
				Startprocess();
				Toast.makeText(getApplicationContext(),
						"Contact already exists", Toast.LENGTH_LONG).show();

			}
		}
		count = datainsertion.countcontacts(getApplicationContext());
		if (count > 0) {
			nocontacts.setText("");
		}
	}

	private boolean checklist() {
		DataInsertion datainsertion = new DataInsertion();
		boolean flag = datainsertion.checkdatabase(getApplicationContext(),
				phoneName);

		return flag;
	}

	public void ShowMessiahContact() {
		datainsertion = new DataInsertion();
		count = datainsertion.countcontacts(getApplicationContext());
		nocontacts = (TextView) findViewById(R.id.empty);
		Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
		nocontacts.setTypeface(font);
		if (count > 0) {
			nocontacts.setText("");
			nocontacts.setVisibility(View.INVISIBLE);
			Contacts = datainsertion.displayData(getApplicationContext());

			ContactsArray = new ArrayList<String>(count);
			for (String value : Contacts) {
				ContactsArray.add(value);
			}
			Numbers = datainsertion.getphonenumbers(getApplicationContext());
			PhoneArray = new ArrayList<String>(count);
			for (String value : Numbers) {
				PhoneArray.add(value);
			}
			listrow adapter = new listrow(getApplicationContext(),
					ContactsArray);
			list.setAdapter(adapter);
		}

	}

	private class listrow extends ArrayAdapter<String> {
		private final Context context;
		public int positionx;

		public listrow(Context context, ArrayList<String> names) {
			super(context, R.layout.messiah_contacts, names);
			this.context = context;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int temp = position;
			positionx = temp;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View v = inflater.inflate(R.layout.messiah_contacts, parent,
					false);
			Typeface font = Typeface.createFromAsset(getAssets(), "rcl.ttf");
			TextView tv = (TextView) v.findViewById(R.id.tvName);
			tv.setTypeface(font);
			tv.setText(ContactsArray.get(position));
			ImageView dial = (ImageView) v.findViewById(R.id.IVDial);
			ImageView edit = (ImageView) v.findViewById(R.id.IVEdit);

			dial.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					String phoneNumber = PhoneArray.get(temp);
					callIntent.setData(Uri.parse("tel:" + phoneNumber));
					startActivity(callIntent);

				}
			});
			edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					editor.putInt("position", temp).commit();
					registerForContextMenu(view);
					openContextMenu(view);

				}
			});
			return v;
		}
	}

	public void callme() {

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CONTEXT_MENU_VIEW: {
			int menuItemIndex = 0;
			
			String listItemName = Contacts[users.getInt("position", 0)];
			menuopertion(menuItemIndex, listItemName);
		}
			break;
		case CONTEXT_MENU_EDIT: {
			int menuItemIndex = 1;
			String listItemName = Contacts[users.getInt("position", 0)];
			menuopertion(menuItemIndex, listItemName);

		}
			break;
		}

		return super.onContextItemSelected(item);
		// AdapterView.AdapterContextMenuInfo info =
		// (AdapterView.AdapterContextMenuInfo) item
		// .getMenuInfo();
		// int menuItemIndex = item.getItemId();
		//
		// String listItemName = Contacts[info.position];
		// menuopertion(menuItemIndex, listItemName);
		// return true;
	}

	private void menuopertion(int menuItemIndex, final String listItemName) {
		DataInsertion datainsertion = new DataInsertion();

		if (menuItemIndex == 0) {
			// remove contact
			datainsertion.removecontact(getApplicationContext(), listItemName);
			count = datainsertion.countcontacts(getApplicationContext());
			if (count > 0) {
				nocontacts.setVisibility(View.INVISIBLE);
			}
			if (count == 0) {
				nocontacts.setText("No Contacts Added");
				nocontacts.setVisibility(View.VISIBLE);
			}

		} else {

			// View Message
			String msg = datainsertion.showmessage(getApplicationContext(),
					listItemName);
			// showing in alert box
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Emergeny Message"); // Set Alert dialog title
												// here
			// alert.setMessage("Enter Your Name Here"); //Message here

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			input.setText(msg);
			alert.setView(input);

			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// You will get as string input data in this
							// variable.
							// here we convert the input to a string and
							// show in a toast.
							message = input.getEditableText().toString();

							DataInsertion datainsertion = new DataInsertion(
									getApplicationContext(), listItemName,
									message);
							ShowMessiahContact();

						}
						// End of onClick(DialogInterface dialog, int
						// whichButton)
					}); // End of alert.setPositiveButton
			alert.setNegativeButton("CANCEL",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
							dialog.cancel();
						}
					}); // End of alert.setNegativeButton
			AlertDialog alertDialog = alert.create();
			alertDialog.show();

		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//
		// if (v.getId() == R.id.list) {
		// AdapterView.AdapterContextMenuInfo info =
		// (AdapterView.AdapterContextMenuInfo) menuInfo;
		// menu.setHeaderTitle(Contacts[info.position]);
		// String[] menuItems = getResources().getStringArray(R.array.menu);
		// for (int i = 0; i < menuItems.length; i++) {
		// menu.add(Menu.NONE, i, i, menuItems[i]);
		// }
		// }

		menu.setHeaderTitle("My Context Menu");
		menu.add(Menu.NONE, CONTEXT_MENU_VIEW, Menu.NONE, "Remove Contact");
		menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit Message");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}
}
