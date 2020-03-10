package sbts.dmw.com.sbtrackingsystem.classes;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;


public class SingletonClass {

    private static SingletonClass mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private SingletonClass(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized SingletonClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());

        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
