package com.example.mohammed.programmingquotesapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.mohammed.programmingquotesapp.sync.QuoteAuthenticator;

/**
 * Created by Mohammed on 27/07/2017.
 */

public class QuoteAuthenticatorService extends Service {
    private QuoteAuthenticator mAuthenticator;
    private static   String ACOUNT_TYPE;
    private static  String ACOUNT_NAME;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new QuoteAuthenticator(this);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
