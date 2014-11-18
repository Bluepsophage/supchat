package fr.supinternet.chat.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import fr.supinternet.chat.R;
import fr.supinternet.chat.fragment.ContactsFragment;
import fr.supinternet.chat.model.User;

public class CreateChatActivity extends Activity{
	
	private ContactsFragment fragment;
	private EditText nameEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_chat);
		
		FragmentManager fragmentManager = getFragmentManager();
		fragment = (ContactsFragment) fragmentManager.findFragmentById(R.id.fragment_contacts);
		fragment.setSelectable(true);
		
		initViews();
		
	}
	
	private void initViews() {
		nameEdit = (EditText) findViewById(R.id.activity_create_chat_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu_validate, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.menu_validate){
			if (!checkName()){
				Toast.makeText(this, R.string.activity_create_chat_no_name_error, Toast.LENGTH_SHORT).show();
				return true;
			}
			
			if (!checkUserList()){
				Toast.makeText(this, R.string.activity_create_chat_no_users_error, Toast.LENGTH_SHORT).show();
				return true;
			}
			// Create chat
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean checkUserList() {
		ArrayList<User> usersSelected = fragment.getUsersSelected();
		if (usersSelected == null || usersSelected.size() == 0){
			return false;
		}
		return true;
	}

	private boolean checkName() {
		String chatName = nameEdit.getText().toString();
		
		if (chatName == null || chatName.length() == 0){
			return false;
		}
		return true;
	}

}
