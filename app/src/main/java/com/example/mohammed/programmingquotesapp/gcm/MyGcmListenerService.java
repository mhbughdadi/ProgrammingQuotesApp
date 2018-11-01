package com.example.mohammed.programmingquotesapp.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.ui.activities.MainActivity;
import com.example.mohammed.programmingquotesapp.utilities.Utility;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed on 09/08/2017.
 */

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG=MyGcmListenerService.class.getSimpleName();
    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        if (bundle.isEmpty()){
            if(getString(R.string.gcm_defaultSenderId).equals(s)){
                JSONObject root= null;
                try {

                    root = new JSONObject(bundle.getString("hfht"));
                    String quote=root.getString("quote");
                    Log.d(TAG,bundle.getString("hfht"));
                    if(Utility.getNotificationState(getApplicationContext(),getResources().getBoolean(R.bool.pref_enable_notifications_default)));
                        sendNotification(quote);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(getString(R.string.notifiction_hint_error),bundle.toString());
            }
        }

    }
    private void sendNotification(String alert){
        NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pIntent=PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),0);
        Bitmap largIcon= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largIcon)
                .setContentText(alert)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.notification_title))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pIntent);
        nm.notify(12354,builder.build());

    }
}
