package fr.supinternet.chat.activity;

import eu.erikw.PullToRefreshListView;
import fr.supinternet.chat.R;
import android.app.Activity;
import android.os.Bundle;

public class ContactsActivity extends Activity{
	
	private PullToRefreshListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		initViews();
	}

	private void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.activity_contacts_list);
	}

}
