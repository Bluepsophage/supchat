package fr.supinternet.chat.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import fr.supinternet.chat.factory.json.TokenJSONFactory;
import fr.supinternet.chat.model.Token;

public class ChatUsersRequest extends AbstractRequest{

	private static final String LOGIN = "list_user_for_chat";
	
	// TODO check values

	public ChatUsersRequest(Context context, int method, Token data, long chatID, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context, method, constructUrl(chatID), constructJSONObject(data), listener, errorListener);
	}

	public ChatUsersRequest(Context context, Token data, long chatID, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context,constructUrl(chatID), constructJSONObject(data), listener, errorListener);
	}

	private static String constructUrl(long chatID){
		return SERVER_URL + LOGIN + "?chatId=" + chatID;
	}

	private static JSONObject constructJSONObject(Token data) throws JSONException{
		JSONObject json = TokenJSONFactory.getJSONObject(data);
		return json;
	}


}
