package com.stores.dallas.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Asif on 1/14/16.
 */
public class StoreUtils {

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
