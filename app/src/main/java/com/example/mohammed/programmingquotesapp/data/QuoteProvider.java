package com.example.mohammed.programmingquotesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mohammed.programmingquotesapp.data.QuoteContract;


/**
 * Created by Mohammed on 26/07/2017.
 */

public class QuoteProvider extends ContentProvider {
    private SQLiteOpenHelper mOpenHelper;

    private static final String TAG=QuoteProvider.class.getSimpleName();
    private static final int ITEMS = 1;
    private static final int ITEMS__ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(QuoteContract.CONTENT_AUTHORITY, "quotes", ITEMS);
        uriMatcher.addURI(QuoteContract.CONTENT_AUTHORITY, "quotes/#", ITEMS__ID);
    }
     static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QuoteContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, "/"+QuoteContract.QUOTE_PATH, ITEMS);
        matcher.addURI(authority, "/"+QuoteContract.QUOTE_PATH + "/#", ITEMS__ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper=new QuoteBase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;
        int m=uriMatcher.match(uri);
        Log.d(TAG,m+"");
        int match=m;
        switch (match){
            case ITEMS: {
                cursor= getQuotes(uri, projection);
                break;
            }
            case ITEMS__ID: {
                cursor= getQuotesById(uri, projection);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }
        return cursor;
    }

    private Cursor getQuotesById(Uri uri, String[] projection) {
        SQLiteDatabase db=mOpenHelper.getReadableDatabase();
        String selection=QuoteContract.Items._ID+"=?";
        String selectionArgs=QuoteContract.Items.getItemId(uri)+"";
        return db.query(QuoteContract.TABLE,projection,selection,new String[]{selectionArgs},null,null,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = buildUriMatcher().match(uri);
        switch (match) {
            case ITEMS:
                return QuoteContract.Items.CONTENT_TYPE;
            case ITEMS__ID:
                return QuoteContract.Items.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final long _id = db.insertOrThrow(QuoteContract.TABLE, null, values);
        if(_id>0){ getContext().getContentResolver().notifyChange(uri, null);
        return QuoteContract.Items.buildItemUri(_id);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match=uriMatcher.match(uri);
        int res=0;
        switch (match){
            case ITEMS: {
                db.execSQL("delete from "+ QuoteContract.TABLE);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            case ITEMS__ID: {
                String selec=QuoteContract.Items._ID+"=?";
                String selecArgs=QuoteContract.Items.getItemId(uri)+"";
                res=db.delete(QuoteContract.TABLE,selec,new String[]{selecArgs});
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }





        return res;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
       final UriMatcher matcher=buildUriMatcher();
        String selec=QuoteContract.Items._ID+"=?";
        String selecArgs=QuoteContract.Items.getItemId(uri)+"";
        getContext().getContentResolver().notifyChange(uri, null);
        return db.update(QuoteContract.TABLE,values,selec,new String[]{selecArgs});
    }
    public  Cursor getQuotes(Uri uri, String[] projection){
        SQLiteDatabase db=mOpenHelper.getReadableDatabase();

       Cursor cursor= db.query(QuoteContract.TABLE,projection,null,null,null,null,null);
        return cursor;
    }

}
