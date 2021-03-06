package fr.supinternet.chat.activity;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import fr.supinternet.chat.R;
import fr.supinternet.chat.manager.AuthenticationManager;
import fr.supinternet.chat.manager.RequestManager;
import fr.supinternet.chat.model.TokenResponse;
import fr.supinternet.chat.model.User;

public class ChatsActivity extends Activity{
	
	private static final String TAG = "ChatsActivity";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	private String SENDER_ID = "320402423527";
	private GoogleCloudMessaging gcm;
	private String regid;

	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		
		context = getApplicationContext();

		if (checkPlayServices()) {
			Log.i(TAG, "OK");
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
			sendRegistrationIdToBackend();
		}else{
			Log.i(TAG, "NOK");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu_chat, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_chat){
			goToCreateChatActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void goToCreateChatActivity() {
		Intent intent = new Intent(this, CreateChatActivity.class);
		startActivity(intent);
	}
	
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    return getSharedPreferences(ChatsActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    checkPlayServices();
	}
	
	
	private void registerInBackground() {
		
	    new AsyncTask<Void, Integer, Boolean>() {
	    	
	    	

			@Override
			protected Boolean doInBackground(Void... params) {
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                Log.i(TAG, "regId " + regid);
	                sendRegistrationIdToBackend();
	                storeRegistrationId(context, regid);
	            } catch (IOException ex) {
	            	Log.i(TAG, "can't find registration id cause " + ex.getMessage(), ex);
	            }
	            return null;
			}

	    }.execute(null, null, null);
	}
	
	private void sendRegistrationIdToBackend() {
		
		try {
			User user = new User();
			user.setUserPushID(regid);
			user.setUserPseudo(AuthenticationManager.getInstance(this).getPseudo());
			user.setUserHash(AuthenticationManager.getInstance(this).getHash());
			RequestManager.getInstance(this).login(user, new Listener<TokenResponse>() {

				@Override
				public void onResponse(TokenResponse arg0) {
					Log.i(TAG, "Push id sent");
				}
			}, new ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Log.i(TAG, "Push id not sent");
				}
				
			});
		} catch (JSONException e) {
			Log.i(TAG, "Push id not sent", e);
		}
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}

}
