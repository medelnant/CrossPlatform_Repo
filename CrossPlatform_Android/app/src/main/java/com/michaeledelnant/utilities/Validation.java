package com.michaeledelnant.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Validation {

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidStringLength(String checkString) {

        boolean isValidLength = false;

        if(checkString.length() >= 3) {
            isValidLength = true;
        }

        return isValidLength;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean activeConnection = false;

        if(connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            activeConnection = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        }

        return activeConnection;
    }
}
