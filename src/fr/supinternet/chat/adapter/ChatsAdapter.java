package fr.supinternet.chat.adapter;

import java.util.ArrayList;

import org.json.JSONException;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import fr.supinternet.chat.R;
import fr.supinternet.chat.fragment.ChatsFragment;
import fr.supinternet.chat.manager.RequestManager;
import fr.supinternet.chat.model.Chat;
import fr.supinternet.chat.model.ChatsResponse;
import fr.supinternet.chat.model.ContactsResponse;
import fr.supinternet.chat.model.User;

public class ChatsAdapter extends BaseAdapter{
	
	private static final String TAG = "ChatsAdapter";
	
	private ArrayList<Chat> chats;
	
	private LayoutInflater inflater;
	private RequestManager manager;
	private ChatsFragment fragment;
	
	private boolean selectable = false;
	
	public ChatsAdapter(ChatsFragment fragment) {
		inflater = LayoutInflater.from(fragment.getActivity());
		manager = RequestManager.getInstance(fragment.getActivity());
		this.fragment = fragment;
	}
	
	public void loadData(){
		try {
			manager.retrieveChats(new Listener<ChatsResponse>() {
				

				@Override
				public void onResponse(ChatsResponse response) {
					if (response != null){
						chats = response.getChats();
					}else{
						Log.e(TAG, "Response null for retrieve contacts");
					}
					notifyDataSetChanged();
					fragment.dataLoaded();
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
				}
			});
		} catch (JSONException e) {
		}
	}
	
	@Override
	public int getCount() {
		return (chats == null ? 0 : chats.size());
	}

	@Override
	public Object getItem(int position) {
		return (chats == null ? null : chats.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder{
		TextView pseudo;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		if (convertView == null){
			holder = new ViewHolder();
			if (selectable){
				convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
				holder.pseudo = (TextView) convertView.findViewById(android.R.id.text1);
			}else{
				convertView = inflater.inflate(R.layout.activity_contacts_item, parent, false);
				holder.pseudo = (TextView) convertView.findViewById(R.id.fragment_contacts_item_pseudo);
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Chat chat = (Chat) getItem(position);
		
		holder.pseudo.setText(chat.getChatName());
		
		
		
		try {
			manager.retrieveChatUsers(chat.getChatID(), new Listener<ContactsResponse>(){

				@Override
				public void onResponse(ContactsResponse arg0) {
					holder.pseudo.setText(chat.getChatName() + " : " + constructUsersList(arg0));
				}
				
			}, new ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError arg0) {
					holder.pseudo.setText(chat.getChatName() + " : unable to retrieve users");
				}
				
			});
		} catch (JSONException e) {
			Log.e(TAG, "Unable to retrieve users", e);
		}
		return convertView;
	}

	protected String constructUsersList(ContactsResponse response) {
		StringBuilder builder = new StringBuilder();
		if (response != null && response.getUsers() != null){
			for (User u : response.getUsers()){
				builder.append(u.getUserPseudo());
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
		notifyDataSetChanged();
	}
	
}
