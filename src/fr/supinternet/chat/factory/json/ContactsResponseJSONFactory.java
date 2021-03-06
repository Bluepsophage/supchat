package fr.supinternet.chat.factory.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.supinternet.chat.model.ContactsResponse;
import fr.supinternet.chat.model.ResponseCode;

public class ContactsResponseJSONFactory {
	
private static final String TAG = "ContactsResponseJSONFactory";
	
	public static ContactsResponse parseFromJSONObject(JSONObject json) throws JSONException{
		
		if (json == null){
			Log.e(TAG, "Unable to create ContactsResponse from Json caused by json null");
			return null;
		}
		
		ContactsResponse result = new ContactsResponse();
		
		result.setCode(ResponseCode.valueOf(json.getString("code")));
		result.setStatus(json.getString("status"));
		result.setUsers(UserJSONFactory.parseFromJSONArray(json.getJSONArray("users")));
		
		return result;
	}
	
}
