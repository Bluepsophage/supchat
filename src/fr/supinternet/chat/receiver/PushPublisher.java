package fr.supinternet.chat.receiver;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class PushPublisher {

	public interface OnPushReceivedListener {
		public void onMessageReceived();
	}

	private static final int MSG_MESSAGE_RECEIVED = 1;

	/**
	 * The unique instance of the manager.
	 */
	private static PushPublisher SINGLETON = null;
	
	/**
	 * The lock for thread safety.
	 */
	private static final Object __synchonizedObject = new Object();

	private static ArrayList<OnPushReceivedListener> listeners = new ArrayList<PushPublisher.OnPushReceivedListener>();

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (listeners != null && listeners.size() > 0) {
				synchronized (listeners) {
					for (OnPushReceivedListener listener : listeners) {
						switch (msg.what) {
						case MSG_MESSAGE_RECEIVED:
							listener.onMessageReceived();
							break;
						}
					}
				}
			}
		}
	};

	public static PushPublisher getInstance() {

		if (SINGLETON == null) {
			synchronized (__synchonizedObject) {
				if (SINGLETON == null) {
					SINGLETON = new PushPublisher();
				}
			}
		}
		return SINGLETON;
	}

	private PushPublisher() {
	}

	public void registerListener(OnPushReceivedListener listener) {
		synchronized (listeners) {
			if (listener != null && !listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	public void unregisterListener(OnPushReceivedListener listener) {
		synchronized (listeners) {
			if (listener != null && listeners.contains(listener)) {
				listeners.remove(listener);
			}
		}
	}
	
	public void publishMessage(){
		handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_RECEIVED));
	}
	
}
