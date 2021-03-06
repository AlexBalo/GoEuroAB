package com.goeuro.ab.network;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.goeuro.ab.utilities.Utilities;

public class ApiClient {

    private RequestQueue mRequestQueue;
    private Uri mApiBaseUri;

    public ApiClient(RequestQueue queue) {
        mRequestQueue = queue;
        Uri.Builder builder = new Uri.Builder()
        						.scheme(NetworkConfig.API_SCHEME)
        						.authority(NetworkConfig.API_DOMAIN)
        						.appendPath(NetworkConfig.API_APPENDIX)
        						.appendPath(NetworkConfig.API_VERSION);
        
        mApiBaseUri = builder.build();
        Utilities.log("Building API Client with baseUrl: " + mApiBaseUri.toString());
    }

    public void execute(Request<?> request) {
    	Utilities.log("API Client execute request method: " + request.getMethod() + " url: " + request.getUrl());
        mRequestQueue.add(request);
    }

    public Uri getBaseUrl() {
        return mApiBaseUri;
    }
}