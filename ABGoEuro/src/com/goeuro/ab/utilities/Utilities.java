package com.goeuro.ab.utilities;

import android.util.Log;

public class Utilities {

	private static final String LOG_TAG = "GOEUROTEST";
	
	public static void log(String logMessage) {
		if (Constants.IS_DEVELOPING) {
			Log.d(LOG_TAG, logMessage);
		}
	}
}
