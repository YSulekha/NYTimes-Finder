package com.codepath.alse.nytimessearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Utility class for Networking Calls
public class NetworkingCalls {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
