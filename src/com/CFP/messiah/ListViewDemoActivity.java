package com.CFP.messiah;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListViewDemoActivity extends Activity {
	int count;
	String phoneName = null;
	String phoneNumber = null;
	String message = null;
	private String[] Contacts;
	DataInsertion datainsertion;
	Button AddContact;
	TextView nocontacts;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ShowMessiahContacts();
		count = datainsertion.countcontacts(getApplicationContext());
		nocontacts = (TextView) findViewById(R.id.empty);
		if (count > 0) {
			nocontacts.setText("");
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
		AddContact = (Button) findViewById(R.id.btnAddContact);
		AddContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Startprocess();
			}
		});

	}

	private void ShowMessiahContacts() {
		datainsertion = new DataInsertion();
		Contacts = datainsertion.displayData(getApplicationContext());
		Arrays.sort(Contacts);

		ListView list = (ListView) findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listitem, Contacts);
		list.setAdapter(adapter);
		registerForContextMenu(list);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(Contacts[info.position]);
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();

		String listItemName = Contacts[info.position];
		menuopertion(menuItemIndex, listItemName);
		return true;
	}

	private void menuopertion(int menuItemIndex, final String listItemName) {
		DataInsertion datainsertion = new DataInsertion();

		if (menuItemIndex == 0) {
			// remove contact
			datainsertion.removecontact(getApplicationContext(), listItemName);
			ShowMessiahContacts();
			count = datainsertion.countcontacts(getApplicationContext());
			if (count > 0) {
				nocontacts.setText("");
			}
			if(count == 0){
				nocontacts.setText("No Contact Added");
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
							ShowMessiahContacts();

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
									ShowMessiahContacts();
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

	@Override
	protected void onResume() {
		super.onResume();
		count = datainsertion.countcontacts(getApplicationContext());
		if (count > 0) {
			nocontacts.setText("");
		}
	}
}
