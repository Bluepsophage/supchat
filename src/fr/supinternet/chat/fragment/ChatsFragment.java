package fr.supinternet.chat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import fr.supinternet.chat.R;
import fr.supinternet.chat.adapter.ChatsAdapter;
import fr.supinternet.chat.receiver.PushPublisher;
import fr.supinternet.chat.receiver.PushPublisher.OnPushReceivedListener;

public class ChatsFragment extends Fragment implements OnPushReceivedListener{
	
	private View view;
	private PullToRefreshListView listView;
	private ChatsAdapter adapter;
	
	private PushPublisher pushPublisher;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_chats, container, false);
		initViews();
		return view;
	}
	
	private void initViews() {
		listView = (PullToRefreshListView) view.findViewById(R.id.fragment_chat_list);
		adapter = new ChatsAdapter(this);
		listView.setAdapter(adapter);
		adapter.loadData();
		
		listView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				adapter.loadData();
			}
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		pushPublisher = PushPublisher.getInstance();
		pushPublisher.registerListener(this);
		
		if (adapter != null){
			adapter.loadData();
		}
	}
	
	@Override
	public void onPause() {
		pushPublisher.unregisterListener(this);
		super.onPause();
	}
	
	public void dataLoaded(){
		if (listView != null){
			listView.onRefreshComplete();
		}
	}

	@Override
	public void onMessageReceived() {
		if (adapter != null){
			adapter.loadData();
		}
	}

}
