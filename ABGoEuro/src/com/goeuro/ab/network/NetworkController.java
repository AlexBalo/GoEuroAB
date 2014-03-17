package com.goeuro.ab.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.goeuro.ab.utilities.Utilities;

public class NetworkController {

    // Singleton instance
    private static NetworkController sInstance;
    private ApiClient mApiClient;
    private RequestQueue mRequestQueue;
    private boolean mIsInitialized;

    private NetworkController() {
    }

    // Singleton getter
    public static NetworkController getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkController();
        }
        return sInstance;
    }

    // Get instance of ApiClient
    public ApiClient getApiClient() {
        return mApiClient;
    }

    /**
     * we initialize Volley with a TinStack that handles authentication in header params
     */
    public boolean init(Context context) {
        if (mIsInitialized) {
            return true;
        }

        HttpStack stack = new HurlStack();
        mRequestQueue = Volley.newRequestQueue(context, stack);
        mApiClient = new ApiClient(mRequestQueue);


        Utilities.log("Creating bitmap cache");
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8nd of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        Utilities.log("MaxMemory: " + maxMemory);
        Utilities.log("CacheSize: " + cacheSize);

        mIsInitialized = true;
        return true;
    }

    public boolean finish(Context context) {
        return true;
    }

    //////////////////////////////////////////////////////////////////////
    //
    //  Actions
    //
    /////////////////////////////////////////////////////////////////////

    public void executeRequest(JsonRequest<?> request) {
        try {
            request.getHeaders();
        } catch (AuthFailureError authFailureError) {
        	authFailureError.getMessage();
        }
        mRequestQueue.add(request);
    }

    public void cancelRequest() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}