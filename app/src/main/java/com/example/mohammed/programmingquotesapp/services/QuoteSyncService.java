package com.example.mohammed.programmingquotesapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mohammed.programmingquotesapp.sync.QuoteAppSyncAdapter;

/**
 * Created by Mohammed on 27/07/2017.
 */

public class QuoteSyncService extends Service {
    private static final String TAG=QuoteSyncService.class.getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static QuoteAppSyncAdapter mQuoteSyncAdapter ;

    @Override
    public void onCreate() {
        Log.d(TAG, "oCreate ");
        synchronized (sSyncAdapterLock) {
            if (mQuoteSyncAdapter == null) {
                mQuoteSyncAdapter = new QuoteAppSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mQuoteSyncAdapter.getSyncAdapterBinder();
    }

}
