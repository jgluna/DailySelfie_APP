package com.github.jgluna.dailyselfie.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class SelfiesProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.github.jgluna.dailyselfie";
    private static final String URL = "content://" + PROVIDER_NAME + "/selfies";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int ALL_SELFIES = 1;
    private static final int SINGLE_SELFIE = 2;
    private static final int MODIFIED_SELFIES = 3;
    private static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "selfies", ALL_SELFIES);
        uriMatcher.addURI(PROVIDER_NAME, "selfies/#", SINGLE_SELFIE);
        uriMatcher.addURI(PROVIDER_NAME, "selfies/*", ALL_SELFIES);
        uriMatcher.addURI(PROVIDER_NAME, "selfies/modified/*", MODIFIED_SELFIES);
        uriMatcher.addURI(PROVIDER_NAME, "selfies/modified", MODIFIED_SELFIES);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DBHelper.SELFIES_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_SELFIES:
                qb.setProjectionMap(values);
                break;
            case SINGLE_SELFIE:
                qb.appendWhere(DBHelper.SELFIES_ID_COLUMN + "=" + uri.getLastPathSegment());
                break;
            case MODIFIED_SELFIES:
                qb.appendWhere(DBHelper.SELFIES_IS_MODIFIED_COLUMN + "=1");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = DBHelper.SELFIES_CREATION_DATE_COLUMN;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DBHelper.SELFIES_TABLE, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case SINGLE_SELFIE:
                String id = uri.getLastPathSegment();
                count = db.delete(DBHelper.SELFIES_TABLE, DBHelper.SELFIES_ID_COLUMN + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case SINGLE_SELFIE:
                count = db.update(DBHelper.SELFIES_TABLE, values, DBHelper.SELFIES_ID_COLUMN +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
