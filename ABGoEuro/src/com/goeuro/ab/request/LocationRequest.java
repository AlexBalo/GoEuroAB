package com.goeuro.ab.request;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goeuro.ab.endpoints.LocationEndpoint;
import com.goeuro.ab.listeners.OnSearchLocationRequestListener;
import com.goeuro.ab.model.Position;
import com.goeuro.ab.network.ApiClient;
import com.goeuro.ab.network.parsers.LocationParser;
import com.goeuro.ab.utilities.Utilities;

public class LocationRequest {

    private final ApiClient mApiClient;
    private final String mLocale;
    private final String mParamToSearch;
    private final OnSearchLocationRequestListener mCallback;
    private JsonObjectRequest mRequest;

    public LocationRequest(ApiClient apiClient, String locale, String paramToSearch, 
    		final OnSearchLocationRequestListener callback) {
        mApiClient = apiClient;
        mLocale = locale;
        mParamToSearch = paramToSearch;
        mCallback = callback;
    }

    public void execute() {
        Utilities.log("Invoking Search Location request");
        mRequest = LocationEndpoint.requestLocation(mApiClient, mLocale, mParamToSearch,
        		new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
                        Utilities.log("Received Successful response for search location");
                        ArrayList<Position> positions = LocationParser.parsePositionJson(response);
                        mCallback.onSearchLocationSuccess(positions);
					}
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utilities.log("Received FAILED response for search location with msg: " + error.getMessage());
                        mCallback.onSearchLocationError(error);
                    }
                }
        );
    }

    public void cancel() {
        if (mRequest != null && !mRequest.isCanceled()) {
            mRequest.cancel();
        }
    }
}