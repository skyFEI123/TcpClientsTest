package com.example.tcpclientstm32;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.tcpclientstm32.provider";
    public static final String TEMP_PATH = "string/person_info/temp";
    public static final String TEMP_THRESHOLD_PATH = "string/person_info/tempThreshold";
    public static final String HEART_PATH = "string/person_info/heart";
    public static final String HEART_THRESHOLD_PATH = "string/person_info/heartThreshold";
    public static final String PRESS_PATH = "string/person_info/press";
    public static final String PRESS_THRESHOLD_PATH = "string/person_info/pressThreshold";
    private static final int PERSON_INFO = 1;

    private UriMatcher uriMatcher;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        return null;
    }

    @Override
    public boolean onCreate() {
        String uri = "content://" + AUTHORITY;
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TEMP_PATH, PERSON_INFO);
        uriMatcher.addURI(AUTHORITY, TEMP_THRESHOLD_PATH, PERSON_INFO);
        uriMatcher.addURI(AUTHORITY, HEART_PATH, PERSON_INFO);
        uriMatcher.addURI(AUTHORITY, HEART_THRESHOLD_PATH, PERSON_INFO);
        uriMatcher.addURI(AUTHORITY, PRESS_PATH, PERSON_INFO);
        uriMatcher.addURI(AUTHORITY, PRESS_THRESHOLD_PATH, PERSON_INFO);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        return 0;
    }
}