package com.example.mohammed.programmingquotesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.QuoteContract;
import com.example.mohammed.programmingquotesapp.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;



/**
 * Created by Mohammed on 25/07/2017.
 */

public class QuoteAppSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = QuoteAppSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public QuoteAppSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public QuoteAppSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_OK,STATUS_SERVER_DOWN,NO_NETWORK})
    public  @interface Status{}
    public static  final int STATUS_OK=0;
    public static  final int STATUS_SERVER_DOWN=1;
    public static  final int NO_NETWORK=2;

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Starting sync");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String quoteJsonStr = null;
        try {
            final String QUOTE_BASE_URL =
                   getContext().getResources().getString(R.string.url_qoutes);
            Uri builtUri = Uri.parse(QUOTE_BASE_URL).buildUpon()
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            quoteJsonStr = buffer.toString();
            getQuoteDataFromJson(quoteJsonStr);

        } catch (ProtocolException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }
    private void getQuoteDataFromJson(String quoteJsonStr)
            throws JSONException {

        final String QUOTE_AUTHER = "author";
        final String QUOTE_ID = "id";
        final String QUOTE = "quote";
        final String QUOTE_PERMALINK = "permalink";
        try {
            Log.d(TAG,quoteJsonStr);
            if(quoteJsonStr.length()==0){
                Utility.setNetworkState(getContext(),STATUS_SERVER_DOWN);
            }
//            JSONObject forecastJson = new JSONObject(quoteJsonStr);
            JSONArray quoteArray=new JSONArray(quoteJsonStr);
//            if(forecastJson.has(OWM_MESSAGE_CODE)){
//                int  code=forecastJson.getInt(OWM_MESSAGE_CODE);
//                switch(code){
//                    case HttpURLConnection.HTTP_OK:
//                        break;
//                    case HttpURLConnection.HTTP_NOT_FOUND:
//                        setLocationStatus(getContext(),LOCATION_STATUS_SERVER_INVALID);
//                        break;
//                    default:
//                        setLocationStatus(getContext(),LOCATION_STATUS_SERVER_DOWN);
//                        break;
//                }
//            }
            Vector<ContentValues> cVVector = new Vector<ContentValues>(quoteArray.length());
            // Insert the new weather information into the database
            for(int i = 0; i < quoteArray.length(); i++) {
                // These are the values that will be collected.
               String quote;
                String id;
                String author;
                String permalink;
                JSONObject jsonQuote = quoteArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                quote=jsonQuote.getString(QUOTE);
                id=jsonQuote.getString(QUOTE_ID);
                permalink=jsonQuote.getString(QUOTE_PERMALINK);
                author=jsonQuote.getString(QUOTE_AUTHER);
                Log.d(TAG,quote);
                ContentValues weatherValues = new ContentValues();

                weatherValues.put(QuoteContract.Items.ID,id);
                weatherValues.put(QuoteContract.Items.AUTHOR,author);
                weatherValues.put(QuoteContract.Items.QUOTE,quote);
                weatherValues.put(QuoteContract.Items.PERALINK,permalink);

                cVVector.add(weatherValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                getContext().getContentResolver().delete(QuoteContract.Items.buildDirUri(),null,null);
               for(int i=0;i<cVVector.size();i++) {

                   Uri uri=getContext().getContentResolver().insert(QuoteContract.Items.buildDirUri(), cVVector.get(i));
                   Log.d(TAG,"row "+i+"inserted ");
                   getContext().getContentResolver().notifyChange(uri,null);

                }

//                updateWidgets();
//                notifyWeather();
            }

//            Log.d(TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }
    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        QuoteAppSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }


}

