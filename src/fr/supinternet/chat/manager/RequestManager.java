package fr.supinternet.chat.manager;

import android.content.Context;

public class RequestManager {
	
	/**
	 * The unique instance of the manager.
	 */
	private static RequestManager SINGLETON = null;

	/**
	 * The lock for thread safety.
	 */
	private static final Object __synchronizedObject = new Object();

	private Context context;

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

}
