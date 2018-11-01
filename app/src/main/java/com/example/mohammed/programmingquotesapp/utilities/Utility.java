package com.example.mohammed.programmingquotesapp.utilities;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.mohammed.programmingquotesapp.R;

/**
 * Created by Mohammed on 31/07/2017.
 */

public class Utility {

    public static void setNotificationState(Context context,boolean state){
        SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=shp.edit();
        editor.putBoolean(context.getResources().getString(R.string.pref_enable_notifications_key),state).apply();

    }

    public static void setNetworkState(Context context,int state){
        SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=shp.edit();
        editor.putInt(context.getResources().getString(R.string.network_state_key),state).apply();

    }

    public static int getNetworkState(Context context,int state){
        SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getInt(context.getResources().getString(R.string.network_state_key),state);
    }
    public static boolean getNotificationState(Context context,boolean state){
        SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(context.getResources().getString(R.string.pref_enable_notifications_key),state);
    }
    public static boolean isNetworkConnected(Context c){
        ConnectivityManager cm= (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= cm.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
    }
}
