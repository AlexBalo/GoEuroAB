package com.goeuro.ab.request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.toolbox.JsonObjectRequest;
import com.goeuro.ab.endpoints.LocationEndpoint;
import com.goeuro.ab.listeners.OnSearchLocationRequestListener;
import com.goeuro.ab.model.Position;
import com.goeuro.ab.network.ApiClient;
import com.goeuro.ab.network.parsers.LocationParser;
import com.goeuro.ab.utilities.Utilities;

public class LocationRequest {

	private static final String POSITION = "position";
	private static final String SUGGEST = "suggest";
	
    private final ApiClient mApiClient;
    private final String mLocale;
    private final String mParamToSearch;
    private final OnSearchLocationRequestListener mCallback;
    private JsonObjectRequest mRequest;
    private Location mUserLocation;
    private Exception mError;
    private ArrayList<Position> mPositions;

    public LocationRequest(ApiClient apiClient, String locale, String paramToSearch, Location userLocation,
    		final OnSearchLocationRequestListener callback) {
        mApiClient = apiClient;
        mLocale = locale;
        mParamToSearch = paramToSearch;
        mUserLocation = userLocation;
        mCallback = callback;
    }

    public void execute() {
    	mError = null;
        Utilities.log("Invoking Search Location request");
//        mRequest = LocationEndpoint.requestLocation(mApiClient, mLocale, mParamToSearch,
//        		new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//                        Utilities.log("Received Successful response for search location");
//                        ArrayList<Position> positions = LocationParser.parsePositionJson(response);
//                        mCallback.onSearchLocationSuccess(positions);
//					}
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Utilities.log("Received FAILED response for search location with msg: " + error.getMessage());
//                        mCallback.onSearchLocationError(error);
//                    }
//                }
//        );
        
        /**
         * 	IMPORTANT NOTE
         *  This solution ignore the certificate, can't be used in production code
         */
		new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... params) {
                
                Uri requestLocationUri = mApiClient.getBaseUrl().buildUpon()
        				.appendPath(POSITION).appendPath(SUGGEST).appendPath(mLocale)
        				.appendPath(mParamToSearch).build();
                
            	HttpURLConnection conn = null;
        		URL url;
        		try {
        			url = new URL(requestLocationUri.toString());
        			if (url.getProtocol().toLowerCase().equals("https")) {
        				LocationEndpoint.trustAllHosts();
        				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
        				https.setHostnameVerifier(LocationEndpoint.DO_NOT_VERIFY);
        				conn = https;
        			} else {
        				conn = (HttpURLConnection) url.openConnection();
        			}

        	        String response= "";
        	        Scanner inStream = new Scanner(conn.getInputStream());
        	        while(inStream.hasNextLine()) {
        	        	response += inStream.nextLine();
        	        }
        	        
        	        if (response != null) {
        	        	JSONObject positions = new JSONObject(response);
        	        	mPositions = LocationParser.parsePositionJson(positions, mUserLocation);
        	        }

        		} catch (MalformedURLException e) {
        			e.printStackTrace();
        			mError = e;
        			return false;
        		} catch (IOException e) {
        			e.printStackTrace();
        			mError = e;
        			return false;
        		} catch (JSONException e) {
					e.printStackTrace();
        			mError = e;
        			return false;
				}
        		return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                    if (success) {
                    	if (mCallback != null) {
                    		mCallback.onSearchLocationSuccess(mPositions);
                    	}
                    } else {
                    	if (mCallback != null) {
                    		mCallback.onSearchLocationError(mError);
                    	}
                    }
            }
        }.execute();
    }

    public void cancel() {
        if (mRequest != null && !mRequest.isCanceled()) {
            mRequest.cancel();
        }
    }
}