package com.example.mohammed.programmingquotesapp.widgets;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;


import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.ui.activities.MainActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Mohammmed Hassan on 08/08/2017.
 */

public class WidgetIntentService extends IntentService {

    public static final String TAG=WidgetIntentService.class.getSimpleName();
    public WidgetIntentService(String name) {
        super(name);
    }

    public WidgetIntentService(){
        super(TAG);

    }
    OkHttpClient client = new OkHttpClient();

    private Quote getQuote(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        Quote quote=null;
        Response response= null;
        try {
            response = client.newCall(request).execute();
            String json=response.body().string();
            JSONObject jsonObject=new JSONObject(json);
            Log.d(TAG,json);
            quote=new Quote(jsonObject.getString("author"),
                    jsonObject.getString("quote"),
                    jsonObject.getString("permalink"),
                    jsonObject.getString("id"));
        } catch (IOException e) {
            Log.e(TAG,getString(R.string.error_get_json_string)+e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG,getString(R.string.error_transform_json)+e.getMessage());
        }
        return quote;
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this.getApplicationContext());
        int[] appWidgetIds=appWidgetManager.getAppWidgetIds(new ComponentName(this,WidgetProvider.class));
//        int[] appWidgetIds=intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        Quote quote=getQuote(getResources().getString(R.string.url_qoute_random));
        if (quote ==null){
            Log.d(TAG,getString(R.string.quote_is_null));
            return;
        }

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_layout);

            // Add the data to the RemoteViews
//            views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, getResources().getString(R.string.widget_views_description));
            }
            views.setTextViewText(R.id.widget_tv_author, quote.getAuthor());
            views.setTextViewText(R.id.widget_tv_quote, quote.getQuote());

            // Create an Intent to launch MainActivity
            Intent clickIntent = new Intent(Intent.ACTION_VIEW);
            clickIntent.setData(Uri.parse(quote.getPeraLink()));
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_btn_open_browser, pendingIntent);


            Intent intentNext = new Intent(this,WidgetIntentService.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, intentNext,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_btn_next, pendingIntentNext);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget, description);
    }


}
