package com.example.mohammed.programmingquotesapp.sync.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mohammed on 25/07/2017.
 */

public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();
    private static  String URI_QUOTES="http://quotes.stormconsultancy.co.uk/quotes.json";
    private static  String URI_POPULAR="http://quotes.stormconsultancy.co.uk/popular.json";
    private static  String URI_QUOTE="http://quotes.stormconsultancy.co.uk/quotes/1.json";

    static {
        URL url = null;
        try {
            url = new URL(URI_QUOTES );
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
