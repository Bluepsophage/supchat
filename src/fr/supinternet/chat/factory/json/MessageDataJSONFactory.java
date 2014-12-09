package fr.supinternet.chat.factory.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.supinternet.chat.model.MessageData;

public class MessageDataJSONFactory {

	private static final String TAG = "MessageDataJSONFactory";

	public static JSONObject getJSONObject(MessageData t) throws JSONException{

		if (t == null){
			Log.e(TAG, "Unable to create JSONObject from MessageData caused by MessageData null");
			return null;
		}

		JSONObject result = new JSONObject();
		result.accumulate("message", MessageJSONFactory.getJSONObject(t.getMessage()));
		return result;
	}

}
