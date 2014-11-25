package fr.supinternet.chat.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

<<<<<<< HEAD
import fr.supinternet.chat.factory.json.TokenJSONFactory;
=======
import fr.supinternet.chat.factory.json.TokenJsonFactory;
>>>>>>> Fork project
import fr.supinternet.chat.model.Token;

public class ContactsRequest extends AbstractRequest{

	private static final String LOGIN = "list_users";

	public ContactsRequest(Context context, int method, Token data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context, method, constructUrl(), constructJSONObject(data), listener, errorListener);
	}

	public ContactsRequest(Context context, Token data, Listener<JSONObject> listener, ErrorListener errorListener) throws JSONException {
		super(context,constructUrl(), constructJSONObject(data), listener, errorListener);
	}

	private static String constructUrl(){
		return SERVER_URL + LOGIN;
	}

	private static JSONObject constructJSONObject(Token data) throws JSONException{
		JSONObject json = TokenJSONFactory.getJSONObject(data);
		return json;
	}


}
