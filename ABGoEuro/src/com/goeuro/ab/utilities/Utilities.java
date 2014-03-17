package com.goeuro.ab.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utilities {

	private static final String LOG_TAG = "GOEUROTEST";
	
	public static void log(String logMessage) {
		if (Constants.IS_DEVELOPING) {
			Log.d(LOG_TAG, logMessage);
		}
	}
	
    public static boolean isInternetConnectionAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
