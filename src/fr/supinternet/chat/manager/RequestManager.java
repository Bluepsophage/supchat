package fr.supinternet.chat.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import fr.supinternet.chat.activity.LoginActivity;
import fr.supinternet.chat.factory.json.ChatsResponseJSONFactory;
import fr.supinternet.chat.factory.json.ContactsResponseJSONFactory;
import fr.supinternet.chat.factory.json.ResponseJSONFactory;
import fr.supinternet.chat.factory.json.TokenResponseJSONFactory;
import fr.supinternet.chat.model.ChatData;
import fr.supinternet.chat.model.ChatsResponse;
import fr.supinternet.chat.model.ContactsResponse;
import fr.supinternet.chat.model.Response;
import fr.supinternet.chat.model.ResponseCode;
import fr.supinternet.chat.model.Token;
import fr.supinternet.chat.model.TokenResponse;
import fr.supinternet.chat.model.User;
import fr.supinternet.chat.request.ChatUsersRequest;
import fr.supinternet.chat.request.ChatsRequest;
import fr.supinternet.chat.request.ContactsRequest;
import fr.supinternet.chat.request.CreateChatRequest;
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

	public void createUser(final User user, final Listener<Response> listener, ErrorListener errorListener) throws JSONException {

		CreateUserRequest request = new CreateUserRequest(context, user, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				Response response = null;
				try {
					response = storeToken(arg0);
					storeCredentials(user.getUserPseudo(), user.getUserHash());
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

	public void login(final User user, final Listener<TokenResponse> listener, ErrorListener errorListener) throws JSONException {

		LoginRequest request = new LoginRequest(context, user, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				TokenResponse response = null;
				try {
					response = storeToken(jsonResponse);
					storeCredentials(user.getUserPseudo(), user.getUserHash());
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

	public void retrieveContacts(final Listener<ContactsResponse> listener, final ErrorListener errorListener) throws JSONException {

		Token token = new Token();
		token.setTokenValue(AuthenticationManager.getInstance(context).getToken());
		ContactsRequest request = new ContactsRequest(context, token, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				ContactsResponse response = null;
				Log.i(TAG, "response " + jsonResponse);
				try {
					response = ContactsResponseJSONFactory.parseFromJSONObject(jsonResponse);

					if (response == null || response.getCode().equals(ResponseCode.TOKEN_INVALID)){
						autoLogin(new Listener<TokenResponse>(){

							@Override
							public void onResponse(TokenResponse response) {
								if (response != null && response.getCode().equals(ResponseCode.OK)){
									try {
										retrieveContacts(listener, errorListener);
									} catch (JSONException e) {
									}
								}else{
									goToLoginActivity();
								}
							}

						}, errorListener);
					}

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
	
	public void retrieveChatUsers(final long chatID, final Listener<ContactsResponse> listener, final ErrorListener errorListener) throws JSONException {

		Token token = new Token();
		token.setTokenValue(AuthenticationManager.getInstance(context).getToken());
		ChatUsersRequest request = new ChatUsersRequest(context, token, chatID, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				ContactsResponse response = null;
				Log.i(TAG, "response " + jsonResponse);
				try {
					response = ContactsResponseJSONFactory.parseFromJSONObject(jsonResponse);

					if (response == null || response.getCode().equals(ResponseCode.TOKEN_INVALID)){
						autoLogin(new Listener<TokenResponse>(){

							@Override
							public void onResponse(TokenResponse response) {
								if (response != null && response.getCode().equals(ResponseCode.OK)){
									try {
										retrieveChatUsers(chatID, listener, errorListener);
									} catch (JSONException e) {
									}
								}else{
									goToLoginActivity();
								}
							}

						}, errorListener);
					}

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

	public void retrieveChats(final Listener<ChatsResponse> listener, final ErrorListener errorListener) throws JSONException {

		Token token = new Token();
		token.setTokenValue(AuthenticationManager.getInstance(context).getToken());
		ChatsRequest request = new ChatsRequest(context, token, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				ChatsResponse response = null;
				Log.i(TAG, "response " + jsonResponse);

				try {
					response = ChatsResponseJSONFactory.parseFromJSONObject(jsonResponse);
					if (response == null || response.getCode().equals(ResponseCode.TOKEN_INVALID)){
						autoLogin(new Listener<TokenResponse>(){

							@Override
							public void onResponse(TokenResponse response) {
								if (response != null && response.getCode().equals(ResponseCode.OK)){
									try {
										retrieveChats(listener, errorListener);
									} catch (JSONException e) {
									}
								}else{
									goToLoginActivity();
								}
							}

						}, errorListener);
					}
					
				} catch (JSONException e) {
					Log.e(TAG, "An error occurred parsing list chat response", e);
				}

				if (listener != null){
					listener.onResponse(response);
				}
			}
		}, errorListener);
		request.start();
	}

	public void createChat(final ChatData data, final Listener<Response> listener, final ErrorListener errorListener) throws JSONException {

		CreateChatRequest request = new CreateChatRequest(context, data, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject json) {
				Response response = null;
				try {
					response = ResponseJSONFactory.parseFromJSONObject(json);
					
					if (response == null || response.getCode().equals(ResponseCode.TOKEN_INVALID)){

						autoLogin(new Listener<TokenResponse>(){

							@Override
							public void onResponse(TokenResponse response) {
								if (response != null && response.getCode().equals(ResponseCode.OK)){
									try {
										createChat(data, listener, errorListener);
									} catch (JSONException e) {
									}
								}else{
									goToLoginActivity();
								}
							}

						}, errorListener);
					}

				} catch (JSONException e) {
					Log.e(TAG, "An error occurred parsing create chat response", e);
				}

				if (listener != null){
					listener.onResponse(response);
				}
			}
		}, errorListener);
		request.start();
	}

	private void autoLogin(final Listener<TokenResponse> listener, final ErrorListener errorListener) throws JSONException{
		
		final User user = new User();
		
		user.setUserPseudo(AuthenticationManager.getInstance(context).getPseudo());
		user.setUserHash(AuthenticationManager.getInstance(context).getHash());
		
		Log.i(TAG, "user sent " + user);
		
		LoginRequest request = new LoginRequest(context, user, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject jsonResponse) {
				TokenResponse response = null;
				try {
					response = storeToken(jsonResponse);
					storeCredentials(user.getUserPseudo(), user.getUserHash());
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

	private void storeCredentials(String pseudo, String hash){
		AuthenticationManager.getInstance(context).setPseudo(pseudo);
		AuthenticationManager.getInstance(context).setHash(hash);
	}

	protected TokenResponse storeToken(JSONObject arg0) throws JSONException {
		TokenResponse response = TokenResponseJSONFactory.parseFromJSONObject(arg0);
		AuthenticationManager.getInstance(context).setToken(response.getToken());
		return response;
	}
	
	private void goToLoginActivity() {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
