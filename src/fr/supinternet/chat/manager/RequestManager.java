package fr.supinternet.chat.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import fr.supinternet.chat.factory.json.ContactsResponseJSONFactory;
import fr.supinternet.chat.factory.json.TokenResponseJSONFactory;
import fr.supinternet.chat.model.ContactsResponse;
import fr.supinternet.chat.model.Response;
import fr.supinternet.chat.model.Token;
import fr.supinternet.chat.model.TokenResponse;
import fr.supinternet.chat.model.User;
import fr.supinternet.chat.request.ContactsRequest;
import fr.supinternet.chat.request.CreateUserRequest;
import fr.supinternet.chat.request.LoginRequest;

public class RequestManager {

	private static final String TAG = "RequestManager";

	/**
	 * The unique instance of the manager.
	 */
	private static RequestManager SINGLETON = null;

	/**
	 * The lock for thread safety.
	 */
	private static final Object __synchronizedObject = new Object();

	private Context context;

	private static int requestId = -1;

	public static RequestManager getInstance(Context context) {

		if (SINGLETON == null) {
			synchronized (__synchronizedObject) {
				if (SINGLETON == null) {
					SINGLETON = new RequestManager(context);
				}
			}
		}
		return SINGLETON;
	}

	private RequestManager(Context context) {
		this.context = context;
	}

	public static int getRequestId(){
		return requestId++;
	}

	public void createUser(User user, final Listener<Response> listener, ErrorListener errorListener) throws JSONException {

		CreateUserRequest request = new CreateUserRequest(context, user, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				Response response = null;
				try {
					response = storeToken(arg0);
				} catch (JSONException e) {
					Log.e(TAG, "An error occurred parsing create user response", e);
				}

				if (listener != null){
					listener.onResponse(response);
				}
			}
		}, errorListener);
		request.start();
	}

	public void login(User user, final Listener<TokenResponse> listener, ErrorListener errorListener) throws JSONException {

		LoginRequest request = new LoginRequest(context, user, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				TokenResponse response = null;
				try {
					response = storeToken(jsonResponse);
				} catch (JSONException e) {
					Log.e(TAG, "An error occurred parsing create user response", e);
				}

				if (listener != null){
					listener.onResponse(response);
				}
			}
		}, errorListener);
		request.start();
	}

	protected TokenResponse storeToken(JSONObject arg0) throws JSONException {
		TokenResponse response = TokenResponseJSONFactory.parseFromJSONObject(arg0);
		AuthenticationManager.getInstance(context).setToken(response.getToken());
		return response;
	}
	
	public void retrieveContacts(final Listener<ContactsResponse> listener, ErrorListener errorListener) throws JSONException {

		Token token = new Token();
		token.setTokenValue(AuthenticationManager.getInstance(context).getToken());
		ContactsRequest request = new ContactsRequest(context, token, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				ContactsResponse response = null;
				try {
					response = ContactsResponseJSONFactory.parseFromJSONObject(jsonResponse);
				} catch (JSONException e) {
					Log.e(TAG, "An error occurred parsing create user response", e);
				}

				if (listener != null){
					listener.onResponse(response);
				}
			}
		}, errorListener);
		request.start();
	}

}
