package com.example.mohammed.programmingquotesapp.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.ui.activities.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Mohammed on 09/08/2017.
 */

public class RegisterationIntentService extends IntentService {
    private static final String TAG=RegisterationIntentService.class.getSimpleName();
    public RegisterationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(this);
        try {
            synchronized (TAG){
                InstanceID instanceID =InstanceID.getInstance(this);
                String token=instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
                sendRegistrationToServer(token);
                shp.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER,true).apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
            shp.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER,false).apply();
        }

    }
    private void sendRegistrationToServer(String token) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, bowlingJson(token));
        Request request = new Request.Builder()
                .url(getResources().getString(R.string.url_qoute_random))
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.d(TAG, String.valueOf(response));
            Log.d(TAG, "Response" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String bowlingJson(String token) {
        return "{'registrationId': " + token + "}";

    }
}
