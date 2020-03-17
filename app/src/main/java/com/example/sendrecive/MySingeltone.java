package com.example.sendrecive;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingeltone {
    private static MySingeltone mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;
    private MySingeltone(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingeltone getmInstance(Context context)
    {

        if (mInstance == null)
        {
            mInstance= new MySingeltone(context);
        }
        return mInstance;
    }
    public <T>void addToRequestQueue(Request<T> requst)
    {
        requestQueue.add(requst);
    }
}
