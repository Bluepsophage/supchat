package fr.supinternet.chat.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import fr.supinternet.chat.factory.json.MessageDataJSONFactory;
import fr.supinternet.chat.manager.AuthenticationManager;
import fr.supinternet.chat.model.MessageData;

public class CreateMessagesRequest extends AbstractRequest{
	
	private static final String CREATE_CHAT = "create_message";

	public CreateMessagesRequest(Context context, int method, MessageData data, long chatId, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context, method, constructUrl(chatId), constructJSONObject(data), listener, errorListener);
	}

	public CreateMessagesRequest(Context context, MessageData data, long chatId, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context,constructUrl(chatId), constructJSONObject(data), listener, errorListener);
	}

	private static String constructUrl(long chatId){
		return SERVER_URL + CREATE_CHAT + "?chatId=" + chatId;
	}

	private static JSONObject constructJSONObject(MessageData data) throws JSONException{
		data.setToken(AuthenticationManager.getInstance(mContext).getToken());
		JSONObject json = MessageDataJSONFactory.getJSONObject(data);
		return json;
	}

}
