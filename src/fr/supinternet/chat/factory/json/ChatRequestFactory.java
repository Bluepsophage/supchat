package fr.supinternet.chat.factory.json;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.supinternet.chat.model.ChatData;
import fr.supinternet.chat.model.User;

public class ChatRequestFactory {
	
private static final String TAG = "ChatRequestFactory";
	
	public static JSONObject getJSONObject(ChatData t) throws JSONException{
		
		if (t == null){
			Log.e(TAG, "Unable to create JSONObject from ChatRequest caused by User null");
			return null;
		}
		
		JSONObject result = new JSONObject();
		result.accumulate("chat", ChatJsonFactory.getJSONObject(t.getChat()));
		result.accumulate("users", UserJsonFactory.getJSONArray((ArrayList<User>) t.getUsers()));
		result.accumulate("token", t.getToken());
		return result;
	}
	
}
