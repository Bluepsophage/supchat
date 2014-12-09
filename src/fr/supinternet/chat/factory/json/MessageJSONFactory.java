package fr.supinternet.chat.factory.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.supinternet.chat.model.Message;

public class MessageJSONFactory {
	
	private static final String TAG = "UserJsonFactory";
	
	public static JSONObject getJSONObject(Message m) throws JSONException{
		
		if (m == null){
			Log.e(TAG, "Unable to create JSONObject from Message caused by Message null");
			return null;
		}
		
		JSONObject result = new JSONObject();
		result.accumulate("messageText", m.getMessageText());
		result.accumulate("messageDate", m.getMessageDate());
		return result;
	}
	
	public static JSONArray getJSONArray(ArrayList<Message> messages) throws JSONException {
		if (messages == null){
			Log.e(TAG, "Unable to create JSONArray from Message list caused by Message list null");
			return null;
		}
		JSONArray result = new JSONArray();
		for (Message m : messages){
			result.put(MessageJSONFactory.getJSONObject(m));
		}
		return result;
	}
	
	public static Message parseFromJSONObject(JSONObject json) throws JSONException{
		
		if (json == null){
			Log.e(TAG, "Unable to create Message from Json caused by json null");
			return null;
		}
		
		Message result = new Message();
		
		result.setMessageDate(json.getLong("messageDate"));
		result.setMessageID(json.getLong("messageID"));
		result.setMessageText(json.getString("messageText"));
		
		return result;
	}
	
	public static ArrayList<Message> parseFromJSONArray(JSONArray array) throws JSONException{

		if (array == null){
			Log.e(TAG, "Unable to create Message List from Json caused by json null");
			return null;
		}
		
		ArrayList<Message> result = new ArrayList<Message>();
		int length = array.length();
		for (int i = 0 ; i < length ; i++){
			result.add(MessageJSONFactory.parseFromJSONObject(array.getJSONObject(i)));
		}
		return  result;
		
	}

}
