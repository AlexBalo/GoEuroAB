package com.goeuro.ab.endpoints;

import org.json.JSONObject;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goeuro.ab.network.ApiClient;

public class LocationEndpoint {
	
    private static final String POSITION = "position";
    private static final String SUGGEST = "suggest";

    /**
     * Executes a location request based on locale and passed param
     *
     * @return The submitted request.
     */
    public static JsonObjectRequest requestLocation(ApiClient client, String locale, String paramToSearch, 
    		Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        Uri requestLocationUri = client.getBaseUrl()
        		.buildUpon()
        		.appendPath(POSITION)
        		.appendPath(SUGGEST)
        		.appendPath(locale)
        		.appendPath(paramToSearch)
        		.build();
        
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestLocationUri.toString(), null, listener, errorListener);
        client.execute(request);
        
        return request;
    }
}
