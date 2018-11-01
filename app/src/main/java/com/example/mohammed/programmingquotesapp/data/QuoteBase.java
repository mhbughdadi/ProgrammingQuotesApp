package com.example.mohammed.programmingquotesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohammed on 25/07/2017.
 */

public class QuoteBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "xyzreader.db";
    private static final int DATABASE_VERSION = 2;

    public QuoteBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + QuoteContract.TABLE + " ( "
                + QuoteContract.QuoteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + QuoteContract.QuoteColumns.AUTHOR + " TEXT NOT NULL,"
                + QuoteContract.QuoteColumns.ID + " TEXT NOT NULL,"
                + QuoteContract.QuoteColumns.QUOTE + " TEXT NOT NULL,"
                + QuoteContract.QuoteColumns.PERALINK + " TEXT NOT NULL "
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuoteContract.TABLE);
        onCreate(db);
    }
}
