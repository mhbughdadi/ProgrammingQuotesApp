package com.example.mohammed.programmingquotesapp.data;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by Mohammed on 25/07/2017.
 */

public class QuoteContract {
    public static final String CONTENT_AUTHORITY ="com.example.mohammed.programmingquotesapp";
    public static final Uri BASE_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String TABLE = "quotes";
    private static final String url="content://"+CONTENT_AUTHORITY;
    public static final String QUOTE_PATH = "quotes";

    interface QuoteColumns {
        String _ID = "_id";
        String AUTHOR = "author";
        String ID = "id";
        String QUOTE = "quote";
        String PERALINK = "peralink";

    }

    public static class Items implements QuoteColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + QUOTE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + QUOTE_PATH;


        /** Matches: /quotes/ */
        public static Uri buildDirUri() {
             Uri uri=Uri.parse(url).buildUpon().appendEncodedPath(QUOTE_PATH).build();
            return uri;
        }
        /** Matches: /quotes/[_id]/ */
        public static Uri buildItemUri(long _id) {
            Uri uri=Uri.parse(url).buildUpon().appendEncodedPath(QUOTE_PATH).appendEncodedPath(""+_id).build();
            return uri;
        }

        /** Read item ID item detail URI. */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }

    private QuoteContract() {
    }
}
