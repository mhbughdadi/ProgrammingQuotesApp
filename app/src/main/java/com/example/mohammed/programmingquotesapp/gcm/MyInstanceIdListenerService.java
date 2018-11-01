package com.example.mohammed.programmingquotesapp.gcm;

import android.content.Intent;
import android.util.Log;

import com.example.mohammed.programmingquotesapp.R;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

/**
 * Created by Mohammed on 09/08/2017.
 */

public class MyInstanceIdListenerService extends InstanceIDListenerService {
    private final String TAG=MyInstanceIdListenerService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String authorizedEntity = getString(R.string.gcm_defaultSenderId); // Project id from Google Developer Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.
        try {
            String token = InstanceID.getInstance(getApplicationContext()).getToken(authorizedEntity,scope);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startService(new Intent(this,RegisterationIntentService.class));
    }

}
