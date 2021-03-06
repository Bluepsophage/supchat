package fr.supinternet.chat.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import fr.supinternet.chat.R;
import fr.supinternet.chat.adapter.ContactsAdapter;
import fr.supinternet.chat.model.User;

public class ContactsFragment extends Fragment{
	
	private View view;
	private PullToRefreshListView listView;
	
	private boolean selectable;
	private ContactsAdapter adapter;
	
	private ArrayList<User> usersSelected = new ArrayList<User>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_contacts, container, false);
		initViews();
		return view;
	}
	
	private void initViews() {
		listView = (PullToRefreshListView) view.findViewById(R.id.fragment_contacts_list);
		adapter = new ContactsAdapter(this);
		listView.setAdapter(adapter);
		adapter.loadData();
		
		listView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				adapter.loadData();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long itemId) {
//				view.setSelected(true);
//				view.setActivated(false);
				
				User user = (User) adapter.getItem(position);
				
				if (usersSelected.contains(user)){
					usersSelected.remove(user);
				}else{
					usersSelected.add(user);
				}
			}
		});
		
	}
	
	public void dataLoaded(){
		if (listView != null){
			listView.onRefreshComplete();
		}
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
		adapter.setSelectable(selectable);
	}

	public ArrayList<User> getUsersSelected() {
		return usersSelected;
	}

	public void setUsersSelected(ArrayList<User> usersSelected) {
		this.usersSelected = usersSelected;
	}

}
