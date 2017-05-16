package com.example.bogdan.qrcodeapp.server.comms;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bogdan on 5/16/17.
 */

public class RequestQueueSingleton {

    private static RequestQueueSingleton ourInstance = null;

    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestQueueSingleton(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static RequestQueueSingleton getInstance(Context ctx) {
        if (ourInstance == null) {
            ourInstance = new RequestQueueSingleton(ctx);
        }
        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}